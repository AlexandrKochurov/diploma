package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ChangeProfileRequest {
    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;
}
