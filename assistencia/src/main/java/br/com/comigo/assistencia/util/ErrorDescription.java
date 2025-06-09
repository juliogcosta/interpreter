package br.com.comigo.assistencia.util;

public class ErrorDescription {
  private String description = null;
  private String code = null;

  public ErrorDescription(String description, String code) {
    this.description = description;
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public String getCode() {
    return code;
  }
}