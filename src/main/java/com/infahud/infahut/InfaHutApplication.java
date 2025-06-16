package com.infahud.infahut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.ApplicationModule;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ApplicationModule(
    allowedDependencies = {"core :: plugins", "web :: core", "web :: plugins", "plugins :: core"})
public class InfaHutApplication {
  public static void main(String[] args) {
    SpringApplication.run(InfaHutApplication.class, args);
  }
}
