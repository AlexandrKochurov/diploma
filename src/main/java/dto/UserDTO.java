package dto;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDTO {
    private int id;

    private byte isModerator;

    private Date regTime;

    private String name;

    private String eMail;

    private String password;

    private String code;

    private String photo;
}
