package io.opentelemetry.example.graal;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SqlExecutor {

  private final JdbcTemplate jdbcTemplate;

  public SqlExecutor(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void loadData() {
    jdbcTemplate.execute("create table test_table (id bigint not null, primary key (id))");
  }
}
