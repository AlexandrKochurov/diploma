package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@NoArgsConstructor
public class RegisterResponse {
    private boolean result;
    private Map<String, String> errors;

    //if everything is ok
    public RegisterResponse(boolean result){
        this.result = result;
    }

    //if something not ok
    public RegisterResponse(boolean result, Map<String, String> errors){
        this.result = result;
        this.errors = errors;
    }
}
