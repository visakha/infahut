package com.infahud.infahut.plugins.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PluginManager {
  private final Map<String, Plugin> plugins = new ConcurrentHashMap<>();
  private final Map<String, List<String>> dependencies = new ConcurrentHashMap<>();

  @Value("${plugins.root:plugin-LoginAPI}")
  private String rootPlugins;

  @Value("${plugins.child:plugin-RetrieveSessionId,plugin-RefreshSessionId}")
  private String childPlugins;

  public void registerPlugin(Plugin plugin) {
    plugins.put(plugin.getName(), plugin);
  }

  public void loadPlugin(String pluginName) {
    Plugin plugin = plugins.get(pluginName);
    if (plugin != null && plugin.getState() == PluginState.UNLOADED) {
      plugin.initialize();
    }
  }

  public void startPlugin(String pluginName) {
    System.out.println("\n### Starting plugin: " + pluginName);
    Plugin plugin = plugins.get(pluginName);
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin not found: " + pluginName);
    }

    if (plugin != null) {
      plugin.start();
    }
  }

  public void stopPlugin(String pluginName) {
    Plugin plugin = plugins.get(pluginName);
    if (plugin != null) {
      plugin.stop();
    }
  }

  public void unloadPlugin(String pluginName) {
    Plugin plugin = plugins.get(pluginName);
    if (plugin != null) {
      plugin.destroy();
    }
  }

  public Collection<Plugin> getAllPlugins() {
    return plugins.values();
  }

  public Plugin getPlugin(String name) {
    return plugins.get(name);
  }

  public void startAllPlugins() {
    // Start root plugins first
    Arrays.stream(rootPlugins.split(",")).map(String::trim).forEach(this::startPlugin);

    // Then start child plugins
    Arrays.stream(childPlugins.split(",")).map(String::trim).forEach(this::startPlugin);
  }
}
