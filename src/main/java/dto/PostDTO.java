package dto;

import lombok.Builder;
import lombok.Data;
import model.ModerationStatus;

import java.util.Date;

@Data
@Builder
public class PostDTO {
    private int id;

    private byte isActive;

    private ModerationStatus modStatus;

    private int moderatorId;

    private int userId;

    private Date time;

    private String title;

    private String text;

    private int viewCount;
}
