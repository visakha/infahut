package com.infahud.infahut.web.controller;

import com.infahud.infahut.plugins.core.PluginManager;
import com.infahud.infahut.plugins.login.LoginApiPlugin;
import com.infahud.infahut.plugins.login.RefreshSessionIdPlugin;
import com.infahud.infahut.web.service.DashboardService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
  private final PluginManager pluginManager;
  private final LoginApiPlugin loginApiPlugin;
  private final RefreshSessionIdPlugin refreshSessionIdPlugin;
  private final DashboardService dashboardService;

  public MainController(
      PluginManager pluginManager,
      LoginApiPlugin loginApiPlugin,
      RefreshSessionIdPlugin refreshSessionIdPlugin,
      DashboardService dashboardService) {
    this.pluginManager = pluginManager;
    this.loginApiPlugin = loginApiPlugin;
    this.refreshSessionIdPlugin = refreshSessionIdPlugin;
    this.dashboardService = dashboardService;
  }

  @GetMapping("/")
  public String index(Model model) {

    model.addAttribute("loginInfo", dashboardService.getLoginInfo());

    model.addAttribute("plugins", pluginManager.getAllPlugins());

    return "index";
  }

  @PostMapping("/api/login")
  @ResponseBody
  public Map<String, Object> performLogin() {

    try {

      loginApiPlugin.performLogin();

      return Map.of(
          "success",
          true,
          "message",
          "Login successful",
          "loginInfo",
          dashboardService.getLoginInfo());

    } catch (Exception e) {

      return Map.of("success", false, "message", "Login failed: " + e.getMessage());
    }
  }

  @PostMapping("/api/refresh")
  @ResponseBody
  public Map<String, Object> refreshSession() {

    try {

      refreshSessionIdPlugin.manualRefresh();

      return Map.of(
          "success",
          true,
          "message",
          "Session refreshed successfully",
          "loginInfo",
          dashboardService.getLoginInfo());

    } catch (Exception e) {

      return Map.of("success", false, "message", "Refresh failed: " + e.getMessage());
    }
  }

  @GetMapping("/api/status")
  @ResponseBody
  public Map<String, Object> getStatus() {

    return Map.of(
        "loginInfo", dashboardService.getLoginInfo(),
        "plugins",
            pluginManager.getAllPlugins().stream()
                .map(
                    plugin ->
                        Map.of(
                            "name", plugin.getName(),
                            "version", plugin.getVersion(),
                            "state", plugin.getState().toString()))
                .toList());
  }
}
