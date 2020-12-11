package main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PostDTO {
    private int id;

    private Instant timestamp;

    private String title;

    private String announce;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;

    private UserDTO user;

}
