package main.service.impl;

import main.api.request.ChangeProfileRequest;
import main.api.request.PostModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.dto.CalendarInterfaceProjection;
import main.dto.TagDTO;
import main.dto.TagInterfaceProjection;
import main.exceptions.BadImageException;
import main.model.GlobalSettingsName;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.User;
import main.repositories.*;
import main.service.GeneralService;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class GeneralServiceImpl implements GeneralService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final PostVoteRepository postVoteRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final int PICTURE_WIDTH = 36;
    private final int PICTURE_HEIGHT = 36;

    @Value("${blog.max_image_size}")
    private int maxFileSize;

    @Value("${uploadPath}")
    private String uploadPath;

    @Autowired
    public GeneralServiceImpl(TagRepository tagRepository, PostRepository postRepository, GlobalSettingsRepository globalSettingsRepository, PostVoteRepository postVoteRepository, PostCommentsRepository postCommentsRepository, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.globalSettingsRepository = globalSettingsRepository;
        this.postVoteRepository = postVoteRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String imageUpload(MultipartFile multipartFile) throws IOException {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        Path path = Path.of("C:/Diploma/src/main/resources/upload/" + createRandomPath() + multipartFile.getOriginalFilename());
        Map<String, String> errors = new HashMap<>();
        assert extension != null;
        if (!extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("png")) {
            errors.put("imageExtension", "Неверное расширение файла");
        }
        if (multipartFile.getSize() > maxFileSize) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }
        if (errors.isEmpty()) {
            File file = new File(path.toString());
            file.mkdirs();
            multipartFile.transferTo(file);
            return file.getPath().substring(29).replace("\\", "/");
        } else {
            throw new BadImageException(false, errors);
        }
    }

    @Override
    public TagResponse tagsList(String query) {
        return new TagResponse(getTagDTOs(tagRepository.tagsByWeight(query)));
    }

    @Override
    public CalendarResponse calendar(Integer year) {
        if (year == 0) {
            year = LocalDate.now().getYear();
        }
        List<CalendarInterfaceProjection> calendarInterfaceProjectionList = postRepository.allByDate(year);
        List<Integer> allYears = postRepository.allByYears();
        Map<String, Integer> posts = new HashMap<>();

        for (CalendarInterfaceProjection cip : calendarInterfaceProjectionList) {
            posts.put(cip.getDate(), cip.getAmount());
        }
        return new CalendarResponse(allYears, posts);
    }

    @Override
    public ChangeProfileResponse changeProfile(ChangeProfileRequest changeProfileRequest) throws IOException {
        Map<String, String> errors = new HashMap<>();
        if (!changeProfileRequest.getEmail().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") || userRepository.findByEmail(changeProfileRequest.getEmail()).isPresent()) {
            errors.put("email", "Неверно введен email, либо email уже существует");
        }
        if (changeProfileRequest.getPhoto() != null) {
            if (changeProfileRequest.getPhoto().getSize() > maxFileSize) {
                errors.put("photo", "Фото слишком большого размера");
            }
        }
        if (changeProfileRequest.getPassword() != null) {
            if (changeProfileRequest.getPassword().length() < 6) {
                errors.put("password", "Пароль должен быть не менее 6 символов");
            }
        }
        if (changeProfileRequest.getName() != null) {
            if (!changeProfileRequest.getName().matches("\\D+")) {
                errors.put("name", "Указано неверное имя");
            }
        }
        if (!errors.isEmpty()) {
            return new ChangeProfileResponse(false, errors);
        }

        Path path = Path.of("C:/Diploma/src/main/resources/upload/photo/" + changeProfileRequest.getPhoto().getOriginalFilename());
        if (!changeProfileRequest.getPhoto().isEmpty()) {
            ByteArrayInputStream bais = new ByteArrayInputStream(changeProfileRequest.getPhoto().getBytes());
            BufferedImage bufferedImage = ImageIO.read(bais);
            File file = new File(path.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            ImageIO.write(Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, PICTURE_WIDTH, PICTURE_HEIGHT), "jpg", file);
        }
        org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(currentUser.getUsername()).orElseThrow());
        if (!changeProfileRequest.getEmail().isBlank()) {
            user.get().setEmail(changeProfileRequest.getEmail());
        }
        if (!changeProfileRequest.getPassword().isBlank()) {
            BCryptPasswordEncoder passenc = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
            user.get().setPassword(passenc.encode(changeProfileRequest.getPassword()));
        }
        if (!changeProfileRequest.getName().isBlank()) {
            user.get().setName(changeProfileRequest.getName());
        }
        if (!changeProfileRequest.getPhoto().isEmpty()) {
            user.get().setPhoto(path.toString().substring(29).replace("\\", "/"));
        }
        if (changeProfileRequest.getRemovePhoto() == 1) {
            File file = new File(path.toString());
            file.delete();
        }
        userRepository.save(user.get());
        return new ChangeProfileResponse(true);
    }

    @Override
    public Map<String, Integer> myStats(User user) {
        return null;
    }

    @Override
    public StatisticResponse allStats(User user) {
        boolean statIsPub = globalSettingsRepository.getSettingsValueByCode(GlobalSettingsName.STATISTICS_IS_PUBLIC.toString());
        if (user.getIsModerator() != 1 && !statIsPub) {
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
        int userId = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
        if (postRepository.findById(postId).isEmpty()) {
            errors.put("post_id", "There is no such post");
        }
        if (parentId != 0 && postCommentsRepository.checkParent(parentId) == 0) {
            errors.put("parent_id", "There is no such parental comment");
        }
        if (text.length() < 15) {
            errors.put("text", "The text is too short or empty");
        }
        if (parentId > 0 && errors.isEmpty()) {
            postCommentsRepository.newCommentToComment(parentId, postId, text, userId);
            return new CommentResponse(postCommentsRepository.getLastCommentId());
        }
        if (errors.isEmpty()) {
            postCommentsRepository.newCommentToPost(postId, text, userId);
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

    @Override
    public ModeratorDecisionResponse changeModeratorDecision(PostModerationRequest request) {
        Post post = postRepository.postByIdForModeration(request.getPostId()).get();
        User moderator = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if(request.getDecision().equalsIgnoreCase("accept")){
            post.setModStatus(ModerationStatus.ACCEPTED);
        } else {
            post.setModStatus(ModerationStatus.DECLINED);
        }
        post.setModeratorId(moderator);
        postRepository.save(post);
        return new ModeratorDecisionResponse(true);
    }

    private List<TagDTO> getTagDTOs(List<TagInterfaceProjection> tagList) {
        double weightReference = 0;
        //Нахожу максимум
        for (TagInterfaceProjection tagInterfaceProjection : tagList) {
            if (weightReference < tagInterfaceProjection.getWeight()) {
                weightReference = tagInterfaceProjection.getWeight();
            }
        }
        List<TagDTO> tagDTOList = new ArrayList<>();
        //Если вес совпадает с максимумом присваем нормальный вес 1, иначе получаем свой нормальный вес
        for (TagInterfaceProjection tagInterfaceProjection : tagList) {
            if (tagInterfaceProjection.getWeight() == weightReference) {
                tagDTOList.add(new TagDTO(tagInterfaceProjection.getName(), 1));
            }
            if (tagInterfaceProjection.getWeight() < weightReference) {
                tagDTOList.add(new TagDTO(tagInterfaceProjection.getName(), tagInterfaceProjection.getWeight() / weightReference));
            }
        }
        tagDTOList.sort(Comparator.comparing(TagDTO::getWeight).reversed());
        return tagDTOList;
    }

    private String createRandomPath() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
            if (i == 1 || i == 3 || i == 5) stringBuilder.append("/");
        }
        return stringBuilder.toString();
    }

    public byte[] getImage(String path) {
        File file = new File("C:/Diploma/src/main/resources" + uploadPath + path);
        byte[] image = new byte[0];
        if (file.exists()) {
            try (FileInputStream is = new FileInputStream(file)) {
                image = is.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                image = new byte[0];
            }
        }
        return image;
    }
}
