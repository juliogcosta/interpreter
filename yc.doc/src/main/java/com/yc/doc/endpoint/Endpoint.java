package com.yc.doc.endpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, 
    ElementType.CONSTRUCTOR})
public @interface Endpoint
{
  public String name() default "";
  public String description() default "";
  public String url() default "";
  public String method() default "";
  public EndpointRequest bodyRequest();
  public EndpointResponse bodyResponse();
  public String [] successStatusCode() default {""};
  public String [] errorStatusCode() default {""};
  public String [] authorizedRoles() default {""};
  public boolean ignore() default false;
  public EndpointCode code();
}
