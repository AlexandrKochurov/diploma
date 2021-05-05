package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProfileRequest {
    private String photo;
    private String password;
    private String name;
    private String email;
    private Integer removePhoto;
}
