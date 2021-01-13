package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.PostCommentsDTO;
import main.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostByIdResponse {
    private int id;

    private long timestamp;

    private boolean active;

    private UserDTO user;

    private String title;

    private String text;

    private int likeCount;

    private int dislikeCount;

    private int viewCount;

    private List<PostCommentsDTO> comments;

    private List<String> tags;
}
