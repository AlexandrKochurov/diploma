package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProfileResponse {
    private boolean result;
    private Map<String, String> errors;

    public ChangeProfileResponse(boolean result) {
        this.result = result;
    }
}
