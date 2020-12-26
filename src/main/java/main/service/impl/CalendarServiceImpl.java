package main.service.impl;

import main.api.response.CalendarResponse;
import main.dto.CalendarDTO;
import main.repositories.PostRepository;
import main.service.CalendarService;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class CalendarServiceImpl implements CalendarService {
    private final PostRepository postRepository;

    public CalendarServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public CalendarResponse getCalendar(int year) {
        if(year == 0){
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        Map<String, Integer> posts = new HashMap<>();
        List<Integer> years = new ArrayList<>();
        List<CalendarDTO> calendarDTOList = postRepository.allByDate(year);

        for(CalendarDTO calendarDTO: calendarDTOList){
            years.add(calendarDTO.getYear());
            posts.put(calendarDTO.getDate(), calendarDTO.getAmount());
        }

        return new CalendarResponse(years, posts);
    }
}
