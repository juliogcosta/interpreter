package com.yc.core.common.infrastructure.exception;

public class PlatformCascadingException extends Exception {

  private static final long serialVersionUID = -5547159189515197450L;

  public PlatformCascadingException(Exception e) {
    super(e);
  }

  public PlatformCascadingException(String message) {
    super(message);
  }

  public PlatformCascadingException(String message, Exception e) {
    super(message, e);
  }
}