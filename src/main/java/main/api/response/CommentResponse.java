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
public class CommentResponse {
    private int id;
    private boolean result;
    private Map<String, String> errors;

    public CommentResponse(int id){
        this.id = id;
    }

    public CommentResponse(boolean result, Map<String, String> errors){
        this.result = result;
        this.errors = errors;
    }
}
