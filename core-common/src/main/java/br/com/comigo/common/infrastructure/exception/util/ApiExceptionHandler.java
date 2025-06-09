package br.com.comigo.common.infrastructure.exception.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
	
	@Value("${exception.handle.stackTrace}")
	private String printStackTrace = null;
	
	public ApiExceptionHandler() {
		log.info("ApiExceptionHandler initialized.");
	}

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ProblemDetails> handleException(Exception ex, HttpServletRequest request) {
        log.info("Exception catched [Message: {}][PrintStackTrace: {}].", 
        		ex.getMessage() == null ? "" : ex.getMessage(), 
        		this.printStackTrace == null ? "" : this.printStackTrace);
        if (this.printStackTrace != null && this.printStackTrace.equals("print")) {
        	ex.printStackTrace();
        }
        return new ResponseEntity<>(ExceptionUtil.getProblemDetails(request, ex), HttpStatus.BAD_REQUEST);
    }
}