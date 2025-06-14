package com.yc.core.common.infrastructure.exception;

public class ForbiddenRequestException extends Exception {

  private static final long serialVersionUID = -5547159189515197450L;

  public ForbiddenRequestException(Exception e) {
    super(e);
  }

  public ForbiddenRequestException(String message) {
    super(message);
  }

  public ForbiddenRequestException(String message, Exception e) {
    super(message, e);
  }
}