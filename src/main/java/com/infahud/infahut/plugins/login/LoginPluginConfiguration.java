package com.infahud.infahut.plugins.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infahud.infahut.plugins.core.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LoginPluginConfiguration implements CommandLineRunner {

  @Autowired private PluginManager pluginManager;

  @Bean
  public LoginApiPlugin loginApiPlugin() {

    return new LoginApiPlugin();
  }

  @Bean
  public RetrieveSessionIdPlugin retrieveSessionIdPlugin() {

    return new RetrieveSessionIdPlugin();
  }

  @Bean
  public RefreshSessionIdPlugin refreshSessionIdPlugin() {

    return new RefreshSessionIdPlugin();
  }

  @Bean
  public RestTemplate restTemplate() {

    return new RestTemplate();
  }

  // @Bean
  // public ObjectMapper objectMapper() {

  //   return new ObjectMapper();
  // }

  @Override
  public void run(String... args) {

    // Register plugins using beans

    pluginManager.registerPlugin(loginApiPlugin());

    pluginManager.registerPlugin(retrieveSessionIdPlugin());

    pluginManager.registerPlugin(refreshSessionIdPlugin());

    // Initialize and start plugins

    pluginManager.startAllPlugins();
  }
}
