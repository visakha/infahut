package com.infahud.infahut.plugins.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infahud.infahut.core.model.LoginCredential;
import com.infahud.infahut.core.singleton.LoginCred;
import com.infahud.infahut.plugins.core.AbstractPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetrieveSessionIdPlugin extends AbstractPlugin {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private ObjectMapper objectMapper;

  public RetrieveSessionIdPlugin() {
    super("plugin-RetrieveSessionId", "1.0.0");
  }

  @Override
  protected void doInitialize() {
    // Nothing to initialize
  }

  @Override
  protected void doStart() {
    retrieveAndSetSessionId();
  }

  @Override
  protected void doStop() {
    // Nothing to stop
  }

  @Override
  protected void doDestroy() {
    // Nothing to destroy
  }

  public void retrieveAndSetSessionId() {
    try {
      String sql =
          """
                SELECT response FROM login_sessions
                ORDER BY seq DESC
                LIMIT 1
                """;

      String response = jdbcTemplate.queryForObject(sql, String.class);
      if (response != null) {
        LoginCredential credential = objectMapper.readValue(response, LoginCredential.class);
        LoginCred.getInstance().updateCredential(credential);
      }
    } catch (Exception e) {
      // Handle case where no login sessions exist yet
      System.out.println("No previous login sessions found: " + e.getMessage());
    }
  }
}
