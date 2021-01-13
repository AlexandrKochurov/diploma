package main.service.impl;

import main.api.response.CalendarResponse;
import main.api.response.TagResponse;
import main.dto.CalendarInterfaceProjection;
import main.dto.TagDTO;
import main.dto.TagInterfaceProjection;
import main.model.User;
import main.repositories.PostRepository;
import main.repositories.TagRepository;
import main.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@Service
public class GeneralServiceImpl implements GeneralService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Autowired
    public GeneralServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public String imageUpload(String path) {
        return null;
    }

    @Override
    public void addComment(Integer parentId, Integer postId, String text) {

    }

    @Override
    public TagResponse tagsList(String query) {
        return new TagResponse(getTagDTOs(tagRepository.tagsByWeight(query)));
    }

    @Override
    public CalendarResponse calendar(Integer year) {
        if(year == 0){
            year = LocalDate.now().getYear();
        }
        List<CalendarInterfaceProjection> calendarInterfaceProjectionList = postRepository.allByDate(year);
        List<Integer> years = new ArrayList<>();
        Map<String, Integer> posts = new HashMap<>();

        for(CalendarInterfaceProjection cip: calendarInterfaceProjectionList){
            years.add(cip.getYear());
            posts.put(cip.getDate(), cip.getAmount());
        }
        return new CalendarResponse(years, posts);
    }

    @Override
    public User changeProfile(File photo, Integer removePhoto, String name, String email, String password) {
        return null;
    }

    @Override
    public Map<String, Integer> myStats(User user) {
        return null;
    }

    @Override
    public Map<String, Integer> allStats(User user) {
        return null;
    }

    @Override
    public Map<String, String> getSettings() {
        return null;
    }

    @Override
    public Map<String, String> setSettings() {
        return null;
    }

    private List<TagDTO> getTagDTOs(List<TagInterfaceProjection> tagList){
        double weightReference = 0;
        //Нахожу максимум
        for(TagInterfaceProjection tagInterfaceProjection: tagList){
            if(weightReference < tagInterfaceProjection.getWeight()){
                weightReference = tagInterfaceProjection.getWeight();
            }
        }
        List<TagDTO> tagDTOList = new ArrayList<>();
        //Если вес совпадает с максимумом присваем нормальный вес 1, иначе получаем свой нормальный вес
        for(TagInterfaceProjection tagInterfaceProjection: tagList){
            if(tagInterfaceProjection.getWeight() == weightReference){
                tagDTOList.add(new TagDTO(tagInterfaceProjection.getName(), 1));
            }
            if(tagInterfaceProjection.getWeight() < weightReference){
                tagDTOList.add(new TagDTO(tagInterfaceProjection.getName(), tagInterfaceProjection.getWeight()/weightReference));
            }
        }
        tagDTOList.sort(Comparator.comparing(TagDTO::getWeight).reversed());
        return tagDTOList;
    }
}
