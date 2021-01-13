package main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDTO {
    private String name;

    private double weight;
}
