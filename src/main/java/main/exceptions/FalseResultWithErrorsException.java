package main.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FalseResultWithErrorsException extends RuntimeException {
    boolean result;
    Map<String, String> errors;
}
