package main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentsDTO {
    private int id;

    private long timestamp;

    private String text;

    private UserDTO user;
}