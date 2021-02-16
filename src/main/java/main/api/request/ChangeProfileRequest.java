package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ChangeProfileRequest {
    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;

    public ChangeProfileRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public ChangeProfileRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public ChangeProfileRequest(MultipartFile photo, String name, String email, int removePhoto) {
        this.photo = photo;
        this.name = name;
        this.email = email;
        this.removePhoto = removePhoto;
    }
}
