package com.infahud.infahut.plugins.core;

public abstract class AbstractPlugin implements Plugin {
  protected PluginState state = PluginState.UNLOADED;
  protected final String name;
  protected final String version;

  protected AbstractPlugin(String name, String version) {
    this.name = name;
    this.version = version;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public PluginState getState() {
    return state;
  }

  @Override
  public void initialize() {
    if (state == PluginState.UNLOADED) {
      doInitialize();
      state = PluginState.INITIALIZED;
    }
  }

  @Override
  public void start() {
    if (state == PluginState.INITIALIZED || state == PluginState.STOPPED) {
      doStart();
      state = PluginState.STARTED;
    }
  }

  @Override
  public void stop() {
    if (state == PluginState.STARTED) {
      doStop();
      state = PluginState.STOPPED;
    }
  }

  @Override
  public void destroy() {
    if (state != PluginState.UNLOADED) {
      doDestroy();
      state = PluginState.UNLOADED;
    }
  }

  protected abstract void doInitialize();

  protected abstract void doStart();

  protected abstract void doStop();

  protected abstract void doDestroy();
}
