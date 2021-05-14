package main.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundPostsException.class)
    ResponseEntity<ExceptionMessage> handleNotFoundPostsException(){
        return new ResponseEntity<>(new ExceptionMessage("Такого поста не существует или он не проверен модератором"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundPostByIdException.class)
    ResponseEntity<ExceptionMessage> handleNotFoundPostByIdException(){
        return new ResponseEntity<>(new ExceptionMessage("Такого поста не существует или он не проверен модератором"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadImageException.class)
    ResponseEntity<WrongImageUploadResponse> handleWrongImageUploadResponse(BadImageException badImageException){
        return new ResponseEntity<>(new WrongImageUploadResponse(badImageException.isResult(), badImageException.getErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FalseResultWithErrorsException.class)
    ResponseEntity<BadResultResponse> handleFalseBadResults(FalseResultWithErrorsException badFalseResultException){
        return new ResponseEntity<>(new BadResultResponse(badFalseResultException.isResult(), badFalseResultException.getErrors()), HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    private static class ExceptionMessage {
        private String message;
    }

    @Data
    @AllArgsConstructor
    private static class WrongImageUploadResponse {
        private boolean result;
        private Map<String, String> errors;
    }

    @Data
    @AllArgsConstructor
    private static class BadResultResponse {
        private boolean result;
        private Map<String, String> errors;
    }

}
