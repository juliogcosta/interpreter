package com.yc.persistence.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocController extends Controller
{
  @GetMapping(path = "/open/doc")
  public ResponseEntity<String> getDoc()
  {
    return super.getDoc();
  }
}
