package com.devsouzx.planner.infra;

import com.devsouzx.planner.infra.exceptions.ActivityException;
import com.devsouzx.planner.infra.exceptions.TripException;
import com.devsouzx.planner.infra.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(TripException.class)
    public ResponseEntity<RestErrorMethod> invalidDateHandler(TripException exception) {
        RestErrorMethod threatResponse = new RestErrorMethod(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(threatResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestErrorMethod> notFound(EntityNotFoundException exception) {
        RestErrorMethod threatResponse = new RestErrorMethod(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.getStatus()).body(threatResponse);
    }

    @ExceptionHandler(ActivityException.class)
    public ResponseEntity<RestErrorMethod> invalidDate(ActivityException exception) {
        RestErrorMethod threatResponse = new RestErrorMethod(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.getStatus()).body(threatResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RestErrorMethod> runTimeError(RuntimeException exception) {
        RestErrorMethod threatResponse = new RestErrorMethod(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(threatResponse.getStatus()).body(threatResponse);
    }
}
