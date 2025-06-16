package com.infahud.infahut.core.singleton;

import com.infahud.infahut.core.model.LoginCredential;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class LoginCred {
  private static LoginCred instance;
  private LoginCredential credential;
  private LocalDateTime lastUpdated;

  private LoginCred() {}

  public static synchronized LoginCred getInstance() {
    if (instance == null) {
      instance = new LoginCred();
    }
    return instance;
  }

  public void updateCredential(LoginCredential credential) {
    this.credential = credential;
    this.lastUpdated = LocalDateTime.now();
  }

  public LoginCredential getCredential() {
    return credential;
  }

  public String getSessionId() {
    return credential != null ? credential.userInfo().sessionId() : null;
  }

  public String getBaseApiUrl() {
    return credential != null && !credential.products().isEmpty()
        ? credential.products().get(0).baseApiUrl()
        : null;
  }

  public String getElapsedTime() {
    if (lastUpdated == null) return "N/A";

    long hours = ChronoUnit.HOURS.between(lastUpdated, LocalDateTime.now());
    long minutes = ChronoUnit.MINUTES.between(lastUpdated, LocalDateTime.now()) % 60;

    return String.format("%d hours %d minutes", hours, minutes);
  }

  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }
}
