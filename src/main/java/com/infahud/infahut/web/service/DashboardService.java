package com.infahud.infahut.web.service;

import com.infahud.infahut.core.model.LoginCredential;
import com.infahud.infahut.core.singleton.LoginCred;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

  public Map<String, Object> getLoginInfo() {
    LoginCred loginCred = LoginCred.getInstance();
    LoginCredential credential = loginCred.getCredential();

    Map<String, Object> info = new HashMap<>();

    if (credential != null) {
      info.put("isLoggedIn", true);
      info.put("sessionId", credential.userInfo().sessionId());
      info.put("username", credential.userInfo().name());
      info.put("orgName", credential.userInfo().orgName());
      info.put("baseApiUrl", loginCred.getBaseApiUrl());
      info.put("elapsedTime", loginCred.getElapsedTime());
      info.put("lastUpdated", loginCred.getLastUpdated());
      info.put("status", credential.userInfo().status());
    } else {
      info.put("isLoggedIn", false);
      info.put("sessionId", null);
      info.put("username", null);
      info.put("orgName", null);
      info.put("baseApiUrl", null);
      info.put("elapsedTime", "N/A");
      info.put("lastUpdated", null);
      info.put("status", "Not logged in");
    }

    return info;
  }
}
