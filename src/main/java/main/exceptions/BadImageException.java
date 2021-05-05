package main.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadImageException extends RuntimeException{
    private boolean result;
    private Map<String, String> errors;
}
