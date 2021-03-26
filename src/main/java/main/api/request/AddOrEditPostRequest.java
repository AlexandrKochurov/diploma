package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AddOrEditPostRequest {
    private long timestamp;
    private byte active;
    private String title;
    private List<String> tags;
    private String text;
}
