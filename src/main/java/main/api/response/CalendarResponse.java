package main.api.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Component
public class CalendarResponse {
    private List<Integer> years;

    private Map<String, Integer> posts;
}
