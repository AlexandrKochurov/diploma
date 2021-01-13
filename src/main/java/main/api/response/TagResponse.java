package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.dto.TagDTO;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Data
@AllArgsConstructor
public class TagResponse {
    List<TagDTO> tags;
}
