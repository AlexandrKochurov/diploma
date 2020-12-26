package main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CalendarDTO {
    private int year;

    private String date;

    private int amount;
}
