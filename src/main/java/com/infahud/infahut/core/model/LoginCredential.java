package com.infahud.infahut.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record LoginCredential(
    @JsonProperty("products") List<Product> products,
    @JsonProperty("userInfo") UserInfo userInfo,
    LocalDateTime timestamp) {
  public record Product(
      @JsonProperty("name") String name, @JsonProperty("baseApiUrl") String baseApiUrl) {}

  public record UserInfo(
      @JsonProperty("sessionId") String sessionId,
      @JsonProperty("id") String id,
      @JsonProperty("name") String name,
      @JsonProperty("parentOrgId") String parentOrgId,
      @JsonProperty("orgId") String orgId,
      @JsonProperty("orgName") String orgName,
      @JsonProperty("groups") Map<String, String> groups,
      @JsonProperty("status") String status) {}
}
