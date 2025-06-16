package com.infahud.infahut.plugins.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infahud.infahut.core.model.LoginCredential;
import com.infahud.infahut.core.singleton.LoginCred;
import com.infahud.infahut.plugins.core.AbstractPlugin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoginApiPlugin extends AbstractPlugin {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private RestTemplate restTemplate;

  @Autowired private ObjectMapper objectMapper;

  @Value("${informatica.login.url:https://dmp-us.informaticacloud.com/saas/public/core/v3/login}")
  private String loginUrl;

  @Value("${informatica.username:o}")
  private String username;

  @Value("${informatica.password:o}")
  private String password;

  private static final DateTimeFormatter TIMESTAMP_FORMAT =
      DateTimeFormatter.ofPattern("MMMM dd -- hh:mm a 'CST'");

  public LoginApiPlugin() {

    super("plugin-LoginAPI", "1.0.0");
  }

  @Override
  protected void doInitialize() {

    createTableIfNotExists();
  }

  @Override
  protected void doStart() {

    performLogin();
  }

  @Override
  protected void doStop() {

    // Nothing to stop

  }

  @Override
  protected void doDestroy() {

    // Cleanup if needed

  }

  public LoginCredential performLogin() {

    try {

      HttpHeaders headers = new HttpHeaders();

      headers.setContentType(MediaType.APPLICATION_JSON);

      headers.set("Accept", "application/json");

      headers.set("User-Agent", "InfaHut/1.0.0");

      Map<String, String> loginRequest =
          Map.of(
              "username", username,
              "password", password);

      HttpEntity<Map<String, String>> entity = new HttpEntity<>(loginRequest, headers);

      ResponseEntity<String> response =
          restTemplate.exchange(loginUrl, HttpMethod.POST, entity, String.class);

      String responseBody = response.getBody();

      LoginCredential credential = objectMapper.readValue(responseBody, LoginCredential.class);

      // Store in database

      storeLoginResponse(responseBody);

      // Update singleton

      LoginCred.getInstance().updateCredential(credential);

      return credential;

    } catch (Exception e) {

      throw new RuntimeException("Failed to perform login for user=" + username , e);
    }
  }

  private void createTableIfNotExists() {
    String sql =
        """
            CREATE SCHEMA IF NOT EXISTS hut;
            CREATE TABLE IF NOT EXISTS hut.login_sessions (
                id SERIAL PRIMARY KEY,
                seq INTEGER,
                session_id VARCHAR(255),
                base_api_url VARCHAR(255),
                response TEXT,
                timestamp VARCHAR(255)
            );
        """;
    jdbcTemplate.execute(sql);
  }

  private void storeLoginResponse(String response) throws Exception {

    LoginCredential credential = objectMapper.readValue(response, LoginCredential.class);

    Long nextSeq = getNextSequence();

    String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

    String sessionId = credential.userInfo().sessionId();

    String baseApiUrl =
        !credential.products().isEmpty() ? credential.products().get(0).baseApiUrl() : "";

    String sql =
        """

            INSERT INTO login_sessions (seq, session_id, base_api_url, response, timestamp)

            VALUES (?, ?, ?, ?, ?)

            """;

    jdbcTemplate.update(sql, nextSeq, sessionId, baseApiUrl, response, timestamp);
  }

  private Long getNextSequence() {

    String sql = "SELECT COALESCE(MAX(seq), 0) + 1 FROM login_sessions";

    return jdbcTemplate.queryForObject(sql, Long.class);
  }
}
