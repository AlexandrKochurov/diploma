package main.service.impl;

import main.api.request.SettingsRequest;
import main.api.response.*;
import main.dto.CalendarInterfaceProjection;
import main.dto.TagDTO;
import main.dto.TagInterfaceProjection;
import main.model.GlobalSettingsName;
import main.model.User;
import main.repositories.*;
import main.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@Service
public class GeneralServiceImpl implements GeneralService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final PostVoteRepository postVoteRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;

    @Autowired
    public GeneralServiceImpl(TagRepository tagRepository, PostRepository postRepository, GlobalSettingsRepository globalSettingsRepository, PostVoteRepository postVoteRepository, PostCommentsRepository postCommentsRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.globalSettingsRepository = globalSettingsRepository;
        this.postVoteRepository = postVoteRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String imageUpload(String path) {
        return null;
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
        List<Integer> allYears = postRepository.allByYears();
        Map<String, Integer> posts = new HashMap<>();

        for(CalendarInterfaceProjection cip: calendarInterfaceProjectionList){
            posts.put(cip.getDate(), cip.getAmount());
        }
        return new CalendarResponse(allYears, posts);
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
    public StatisticResponse allStats(User user) {
        boolean statIsPub = globalSettingsRepository.getSettingsValueByCode(GlobalSettingsName.STATISTICS_IS_PUBLIC.toString());
        if(user.getIsModerator() != 1 && !statIsPub){
            return new StatisticResponse();
        }
        return new StatisticResponse(
                postRepository.countAllPosts(),
                postVoteRepository.countAllLikes(),
                postVoteRepository.countAllDislikes(),
                postRepository.viewCountSum(),
                postRepository.getFirstPublication().getEpochSecond()
        );
    }

    @Override
    public CommentResponse comment(int parentId, int postId, String text) {
        Map<String, String> errors = new HashMap<>();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (postRepository.findById(postId).isEmpty()) {
            errors.put("post_id", "There is no such post");
        }
        if (postCommentsRepository.checkParent(parentId) <= 0) {
            errors.put("parent_id", "There is no such parental comment");
        }
        if (text.length() < 15) {
            errors.put("text", "The text is too short or empty");
        }
        if (parentId > 0 && errors.isEmpty()) {
            postCommentsRepository.newCommentToComment(parentId, postId, text, userRepository.findByEmail(user.getUsername()).get().getId());
            return new CommentResponse(postCommentsRepository.getLastCommentId());
        }
        if (errors.isEmpty()) {
            postCommentsRepository.newCommentToPost(postId, text, userRepository.findByEmail(user.getUsername()).get().getId());
            return new CommentResponse(postCommentsRepository.getLastCommentId());
        }
        return new CommentResponse(false, errors);
    }

    @Override
    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setStatisticsIsPublic(globalSettingsRepository.getSettingsValueByCode(GlobalSettingsName.STATISTICS_IS_PUBLIC.toString()));
        settingsResponse.setPostModeration(globalSettingsRepository.getSettingsValueByCode(GlobalSettingsName.POST_PREMODERATION.toString()));
        settingsResponse.setMultiuserMode(globalSettingsRepository.getSettingsValueByCode(GlobalSettingsName.MULTIUSER_MODE.toString()));
        return settingsResponse;
    }


    @Override
    public void setGlobalSettings(SettingsRequest settingsRequest) {
        globalSettingsRepository.setGlobalSettings(settingsRequest.isMultiuserMode(), GlobalSettingsName.MULTIUSER_MODE.toString());
        globalSettingsRepository.setGlobalSettings(settingsRequest.isPostModeration(), GlobalSettingsName.POST_PREMODERATION.toString());
        globalSettingsRepository.setGlobalSettings(settingsRequest.isStatisticsIsPublic(), GlobalSettingsName.STATISTICS_IS_PUBLIC.toString());
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
