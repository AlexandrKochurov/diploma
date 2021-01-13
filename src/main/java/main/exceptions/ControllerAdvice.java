package main.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundPostsException.class)
    ResponseEntity<ExceptionMessage> handleNotFoundPostsException(){
        return new ResponseEntity<>(new ExceptionMessage("There is no such posts"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundPostByIdException.class)
    ResponseEntity<ExceptionMessage> handleNotFoundPostByIdException(){
        return new ResponseEntity<>(new ExceptionMessage("There is no such post"), HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    private static class ExceptionMessage {
        private String message;
    }
}
