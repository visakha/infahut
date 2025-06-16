package com.infahud.infahut.plugins.login;

import com.infahud.infahut.plugins.core.AbstractPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshSessionIdPlugin extends AbstractPlugin {

  @Autowired private LoginApiPlugin loginApiPlugin;

  public RefreshSessionIdPlugin() {
    super("plugin-RefreshSessionId", "1.0.0");
  }

  @Override
  protected void doInitialize() {
    // Nothing to initialize
  }

  @Override
  protected void doStart() {
    // Plugin is now active and ready to refresh on schedule
  }

  @Override
  protected void doStop() {
    // Nothing to stop (scheduled method will check plugin state)
  }

  @Override
  protected void doDestroy() {
    // Nothing to destroy
  }

  @Scheduled(fixedRate = 3600000) // Refresh every hour
  public void refreshSession() {
    if (getState() == com.infahud.infahut.plugins.core.PluginState.STARTED) {
      loginApiPlugin.performLogin();
    }
  }

  public void manualRefresh() {
    loginApiPlugin.performLogin();
  }
}
