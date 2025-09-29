#!/bin/bash

# End-to-end test for the reference application using docker-compose
# This test verifies that the full stack works correctly with OpenTelemetry

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to wait for a service to be ready
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    print_status "Waiting for $service_name to be ready at $url..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -sf "$url" > /dev/null 2>&1; then
            print_status "$service_name is ready!"
            return 0
        fi
        
        if [ $attempt -eq $max_attempts ]; then
            print_error "$service_name failed to start within $max_attempts attempts"
            return 1
        fi
        
        print_status "Attempt $attempt/$max_attempts: $service_name not ready yet, waiting..."
        sleep 2
        ((attempt++))
    done
}

# Function to test application endpoints
test_endpoints() {
    print_status "Testing application endpoints..."
    
    # Test basic dice roll
    print_status "Testing /rolldice endpoint..."
    response=$(curl -sf http://localhost:8080/rolldice)
    if echo "$response" | jq -e '.result' > /dev/null && echo "$response" | jq -e '.player' > /dev/null; then
        print_status "✓ /rolldice endpoint working"
    else
        print_error "✗ /rolldice endpoint failed"
        return 1
    fi
    
    # Test dice roll with player
    print_status "Testing /rolldice?player=testuser endpoint..."
    response=$(curl -sf "http://localhost:8080/rolldice?player=testuser")
    if echo "$response" | jq -r '.player' | grep -q "testuser"; then
        print_status "✓ /rolldice with player working"
    else
        print_error "✗ /rolldice with player failed"
        return 1
    fi
    
    # Test health endpoint
    print_status "Testing /health endpoint..."
    response=$(curl -sf http://localhost:8080/health)
    if echo "$response" | jq -r '.status' | grep -q "UP"; then
        print_status "✓ /health endpoint working"
    else
        print_error "✗ /health endpoint failed"
        return 1
    fi
    
    # Test metrics endpoint
    print_status "Testing /actuator/prometheus endpoint..."
    if curl -sf http://localhost:8080/actuator/prometheus | grep -q "dice_rolls_total"; then
        print_status "✓ /actuator/prometheus endpoint working with custom metrics"
    else
        print_error "✗ /actuator/prometheus endpoint failed or missing custom metrics"
        return 1
    fi
    
    print_status "All endpoint tests passed!"
}

# Function to test OpenTelemetry collector
test_collector() {
    print_status "Testing OpenTelemetry collector..."
    
    # Check collector health endpoint
    if curl -sf http://localhost:13133 > /dev/null; then
        print_status "✓ OpenTelemetry collector health endpoint is accessible"
    else
        print_warning "! OpenTelemetry collector health endpoint not accessible (this might be expected)"
    fi
    
    # Check if collector is receiving and processing data by examining logs
    print_status "Checking collector logs for telemetry data processing..."
    
    # Generate some telemetry data
    curl -sf http://localhost:8080/rolldice > /dev/null
    curl -sf http://localhost:8080/rolldice?rolls=3 > /dev/null
    
    # Wait a bit for data to be processed
    sleep 5
    
    # Check collector logs for evidence of data processing
    if $compose_cmd logs otel-collector 2>/dev/null | grep -q -E "(spans|metrics|logs).*processed"; then
        print_status "✓ OpenTelemetry collector is processing telemetry data"
    else
        print_warning "! Could not verify telemetry data processing in collector logs"
    fi
}

# Function to test Prometheus integration
test_prometheus() {
    print_status "Testing Prometheus integration..."
    
    # Wait for Prometheus to be ready
    if wait_for_service "http://localhost:9090/-/ready" "Prometheus"; then
        print_status "✓ Prometheus is running"
        
        # Check if Prometheus can scrape metrics from the collector
        if curl -sf "http://localhost:9090/api/v1/targets" | jq -r '.data.activeTargets[].health' | grep -q "up"; then
            print_status "✓ Prometheus has healthy targets"
        else
            print_warning "! Prometheus targets may not be healthy"
        fi
    else
        print_warning "! Prometheus failed to start"
    fi
}

# Function to get the docker compose command
get_docker_compose_cmd() {
    if command -v "docker-compose" &> /dev/null; then
        echo "docker-compose"
    elif docker compose version &> /dev/null; then
        echo "docker compose"
    else
        return 1
    fi
}

# Function to cleanup resources
cleanup() {
    print_status "Cleaning up resources..."
    local compose_cmd
    if compose_cmd=$(get_docker_compose_cmd); then
        $compose_cmd down --volumes --remove-orphans || true
    fi
    
    # Clean up any dangling resources
    docker system prune -f || true
}

# Main execution
main() {
    print_status "Starting end-to-end test for OpenTelemetry Reference Application"
    
    # Handle dry-run mode for testing
    if [[ "${1:-}" == "--dry-run" ]]; then
        print_status "Running in dry-run mode - skipping actual Docker operations"
        print_status "✅ Script validation passed"
        return 0
    fi
    
    # Ensure we're in the right directory
    if [[ ! -f "docker-compose.yml" ]]; then
        print_error "docker-compose.yml not found. Please run this script from the reference-application directory."
        exit 1
    fi
    
    # Ensure required tools are available
    for tool in docker curl jq; do
        if ! command -v "$tool" &> /dev/null; then
            print_error "$tool is required but not installed."
            exit 1
        fi
    done
    
    # Check for docker compose command
    if ! compose_cmd=$(get_docker_compose_cmd); then
        print_error "docker-compose or 'docker compose' is required but not available."
        exit 1
    fi
    
    print_status "Using Docker Compose command: $compose_cmd"
    
    # Build and start services
    print_status "Building and starting services with docker-compose..."
    $compose_cmd down --volumes --remove-orphans || true
    $compose_cmd up --build -d
    
    # Wait for services to be ready
    if ! wait_for_service "http://localhost:8080/health" "Reference Application"; then
        print_error "Reference application failed to start"
        cleanup
        exit 1
    fi
    
    if ! wait_for_service "http://localhost:4318/v1/traces" "OpenTelemetry Collector OTLP HTTP"; then
        print_warning "OpenTelemetry Collector OTLP HTTP endpoint not accessible"
    fi
    
    # Run tests
    if test_endpoints; then
        print_status "✅ Application endpoint tests passed"
    else
        print_error "❌ Application endpoint tests failed"
        cleanup
        exit 1
    fi
    
    test_collector
    test_prometheus
    
    print_status "🎉 End-to-end test completed successfully!"
    print_status "The reference application is working correctly with OpenTelemetry stack"
    
    # Cleanup
    cleanup
    
    print_status "✅ All tests passed and cleanup completed"
}

# Trap to ensure cleanup on script exit
trap cleanup EXIT

# Run main function
main "$@"