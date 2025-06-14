package com.yc.doc.endpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EndpointResponse
{
  public String formatOnSuccess() default "";
  public String formatOnError() default "";
  public String description() default "";
}
