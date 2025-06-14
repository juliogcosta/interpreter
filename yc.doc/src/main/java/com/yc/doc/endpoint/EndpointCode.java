package com.yc.doc.endpoint;

public @interface EndpointCode
{
  public String method() default "";
  public String clazz() default "";
}
