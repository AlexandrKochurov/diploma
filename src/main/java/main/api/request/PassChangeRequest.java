package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PassChangeRequest {
    private String code;
    private String password;
    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;
}
