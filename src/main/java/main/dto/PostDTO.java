package main.dto;

import lombok.Builder;
import lombok.Data;
import main.model.ModerationStatus;
import main.model.User;

import java.util.Date;

@Data
@Builder
public class PostDTO {
    private int id;

    private byte isActive;

    private ModerationStatus modStatus;

    private User moderatorId;

    private User userId;

    private Date time;

    private String title;

    private String text;

    private int viewCount;
}
