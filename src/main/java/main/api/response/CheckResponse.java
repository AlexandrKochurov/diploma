package main.api.response;

import lombok.Data;
import main.model.User;
import org.springframework.stereotype.Component;

@Component
@Data
public class CheckResponse {
    private boolean result;
    private User user;
}
