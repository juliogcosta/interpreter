package br.com.comigo.gateway.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "yc.security.rules")
public class SecurityRulesConfig {
  private List<PathRole> pathRoles = new ArrayList<>();

  public static class PathRole {
    private String path;
    private List<String> roles;

    // Getters e Setters
    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public List<String> getRoles() {
      return roles;
    }

    public void setRoles(List<String> roles) {
      this.roles = roles;
    }
  }

  // Converte a lista para um Map<String, List<String>>
  public Map<String, List<String>> getPathRolesMap() {
    Map<String, List<String>> map = new HashMap<>();
    for (PathRole entry : pathRoles) {
      map.put(entry.getPath(), entry.getRoles());
    }
    return map;
  }

  // Getter para a lista (usado pelo Spring)
  public List<PathRole> getPathRoles() {
    return pathRoles;
  }

  public void setPathRoles(List<PathRole> pathRoles) {
    this.pathRoles = pathRoles;
  }
}