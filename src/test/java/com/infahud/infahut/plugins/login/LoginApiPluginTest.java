package com.infahud.infahut.plugins.login;

import static org.junit.jupiter.api.Assertions.*;

import com.infahud.infahut.core.model.LoginCredential;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

// @ApplicationModuleTest(verify = false) // Remove or comment out to suppress module verification

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class LoginApiPluginTest {

  @Autowired private LoginApiPlugin plugin;

  @Test
  void shouldInitializePlugin() {

    assertNotNull(plugin);

    assertEquals("plugin-LoginAPI", plugin.getName());

    assertEquals("1.0.0", plugin.getVersion());
    System.out.println("-------------Plugin initialized successfully---------");
    System.out.println("Plugin Name: " + plugin.getName());
    System.out.println("Plugin Version: " + plugin.getVersion());
  }

  @Test
  void testPerformLogin_withValidCredentials_returnsLoginCredential() {

    plugin.doInitialize();

    LoginCredential credential = plugin.performLogin();

    assertNotNull(credential, "LoginCredential should not be null");

    assertNotNull(credential.userInfo(), "UserInfo should not be null");
    assertEquals("org01_vamsi.vegi", credential.userInfo().name(), "UserId should match");
    System.out.println("UserId: " + credential.userInfo().id());
    System.out.println("UserName: " + credential.userInfo().name());
    System.out.println("Session Id: " + credential.userInfo().sessionId());

    assertNotNull(credential.userInfo().sessionId(), "SessionId should not be null");

    assertFalse(credential.products().isEmpty(), "Products list should not be empty");
  }
}
