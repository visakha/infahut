package com.infahud.infahut.plugins.core;

public interface Plugin {
  String getName();

  String getVersion();

  void initialize();

  void start();

  void stop();

  void destroy();

  PluginState getState();
}
