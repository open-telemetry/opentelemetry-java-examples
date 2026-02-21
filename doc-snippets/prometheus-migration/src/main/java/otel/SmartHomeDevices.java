package otel;

/** Stubs representing smart home device APIs. */
class SmartHomeDevices {
  /**
   * Returns cumulative energy consumed in joules since installation, as reported by the smart
   * energy meter device.
   */
  static double totalEnergyJoules() {
    return 0.0; // In production, this would query the device API.
  }

  /** Returns the current temperature in Celsius from the living room sensor. */
  static double livingRoomTemperatureCelsius() {
    return 0.0; // In production, this would query the device API.
  }

  /** Returns the current temperature in Celsius from the bedroom sensor. */
  static double bedroomTemperatureCelsius() {
    return 0.0; // In production, this would query the device API.
  }

  /** Returns the number of device commands currently waiting to be processed. */
  static long pendingCommandCount() {
    return 0L; // In production, this would query the command queue.
  }
}
