package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean result;

    @JsonProperty("user")
    private UserLoginResponse userLoginResponse;

    public LoginResponse(boolean result){
        this.result = result;
    }
}
