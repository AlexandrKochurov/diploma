package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PassChangeResponse {
    private boolean result;
    private Map<String, String> errors;

    public PassChangeResponse(boolean result) {
        this.result = result;
    }
}
