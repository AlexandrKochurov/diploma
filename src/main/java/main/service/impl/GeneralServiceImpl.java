package main.service.impl;

import main.api.request.ChangeProfileRequest;
import main.api.request.CommentRequest;
import main.api.request.PostModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.dto.CalendarInterfaceProjection;
import main.dto.TagDTO;
import main.dto.TagInterfaceProjection;
import main.exceptions.BadImageException;
import main.exceptions.FalseResultWithErrorsException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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

    private final int PICTURE_WIDTH = 36;
    private final int PICTURE_HEIGHT = 36;

    @Value("${blog.max_image_size}")
    private int maxFileSize;

    @Value("${uploadPath}")
    private String uploadPath;

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
    public String imageUpload(MultipartFile multipartFile) throws IOException {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        Path dirPath = Path.of(uploadPath + createRandomPath());
        Path pathToFile = dirPath.resolve(multipartFile.getOriginalFilename());
        Map<String, String> errors = new HashMap<>();
        //Проверка фото
        if (extension != null && !extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("png")) {
            errors.put("imageExtension", "Неверное расширение файла");
        }
        if (multipartFile.getSize() > maxFileSize) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }
        if (errors.isEmpty()) {
            //Создаем файл
            File file = new File(pathToFile.toAbsolutePath().toString());
            //Создаем директорию
            new File(dirPath.toString()).mkdirs();
            //Пишем фото в свежесозданный файл
            multipartFile.transferTo(file);
            return "/" + pathToFile.toString().replace("\\", "/");
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

    private Map<String, String> checkProfileChanges(ChangeProfileRequest changeProfileRequest) {
        Map<String, String> errors = new HashMap<>();
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        //Проверка осуществляется только если email менялся.
        if (!currentUserEmail.equals(changeProfileRequest.getEmail())) {
            if (!changeProfileRequest.getEmail().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
                    || userRepository.findByEmail(changeProfileRequest.getEmail()).isPresent()) {
                errors.put("email", "Неверно введен email или email уже существует");
            }
        }
        if (changeProfileRequest.getPassword() != null) {
            if (changeProfileRequest.getPassword().length() < 6) {
                errors.put("password", "Пароль должен быть не менее 6 символов");
            }
        }
        if (changeProfileRequest.getName() != null) {
            if (!changeProfileRequest.getName().matches("\\D+")) {
                errors.put("name", "Имя не должно содержать других символов кроме букв");
            }
        }
        return errors;
    }

    @Override
    public ResultResponse changeProfileWithPhoto(MultipartFile photo, String email, String password, String name, Integer removePhoto) throws IOException {
        String photoPath = uploadPhotoWithResize(photo);
        ChangeProfileRequest changeProfileRequest = new ChangeProfileRequest(photoPath, name, email, password, removePhoto);
        Map<String, String> errors = new HashMap<>(checkProfileChanges(changeProfileRequest));
        if (!errors.isEmpty()) {
            throw new FalseResultWithErrorsException(false, errors);
        }
        return makeChangesToProfile(changeProfileRequest);
    }

    @Override
    public ResultResponse changeProfile(ChangeProfileRequest changeProfileRequest) {
        Map<String, String> errors = new HashMap<>(checkProfileChanges(changeProfileRequest));
        if (!errors.isEmpty()) {
            throw new FalseResultWithErrorsException(false, errors);
        }
        return makeChangesToProfile(changeProfileRequest);
    }

    private ResultResponse makeChangesToProfile(ChangeProfileRequest changeProfileRequest){
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!changeProfileRequest.getEmail().isBlank()) {
            user.get().setEmail(changeProfileRequest.getEmail());
        }
        if (changeProfileRequest.getPassword() != null) {
            BCryptPasswordEncoder passenc = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
            user.get().setPassword(passenc.encode(changeProfileRequest.getPassword()));
        }
        if (!changeProfileRequest.getName().isBlank()) {
            user.get().setName(changeProfileRequest.getName());
        }
        if(changeProfileRequest.getPhoto() != null){
            user.get().setPhoto(changeProfileRequest.getPhoto());
        }
        if (changeProfileRequest.getRemovePhoto() != null) {
            if (changeProfileRequest.getRemovePhoto() == 1) {
                user.get().setPhoto(null);
            }
        }
        userRepository.save(user.get());
        return SimpleResultResponse.TRUE;
    }

    @Override
    public StatisticResponse myStats() {
        int userId = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
        return new StatisticResponse(
                postRepository.countMyPosts(userId),
                postVoteRepository.countLikesById(userId),
                postVoteRepository.countDislikesById(userId),
                postRepository.viewCountSumById(userId) == null ? 0 : postRepository.viewCountSumById(userId),
                postRepository.getFirstPublicationById(userId) == null ? 0 : postRepository.getFirstPublicationById(userId).getEpochSecond()
        );
    }

    @Override
    public StatisticResponse allStats() {
        boolean statIsPub = globalSettingsRepository.getSettingsValueByCode(GlobalSettingsName.STATISTICS_IS_PUBLIC.toString());
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
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
    public CommentResponse comment(CommentRequest commentRequest) {
        Map<String, String> errors = new HashMap<>();
        int userId = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
        if (postRepository.findById(commentRequest.getPostId()).isEmpty()) {
            errors.put("post_id", "Такого поста не существует");
        }
        if (commentRequest.getParentId() != 0 && postCommentsRepository.checkParent(commentRequest.getParentId()) == 0) {
            errors.put("parent_id", "Не существует родительского комментария");
        }
        if (commentRequest.getText().length() < 15) {
            errors.put("text", "Текст комментария пустой или слишком короткий, минимум 15 символов");
        }
        if (commentRequest.getParentId() > 0 && errors.isEmpty()) {
            postCommentsRepository.newCommentToComment(commentRequest.getParentId(), commentRequest.getPostId(), commentRequest.getText(), userId);
            return new CommentResponse(postCommentsRepository.getLastCommentId());
        }
        if (errors.isEmpty()) {
            postCommentsRepository.newCommentToPost(commentRequest.getPostId(), commentRequest.getText(), userId);
            return new CommentResponse(postCommentsRepository.getLastCommentId());
        }
        throw new FalseResultWithErrorsException(false, errors);
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
    public ResultResponse changeModeratorDecision(PostModerationRequest request) {
        Post post = postRepository.postByIdForModeration(request.getPostId()).get();
        User moderator = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if (request.getDecision().equalsIgnoreCase("accept")) {
            post.setModStatus(ModerationStatus.ACCEPTED);
        } else {
            post.setModStatus(ModerationStatus.DECLINED);
        }
        post.setModeratorId(moderator);
        postRepository.save(post);
        return SimpleResultResponse.TRUE;
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

    private String uploadPhotoWithResize(MultipartFile photo) throws IOException {
        Path path = Path.of(uploadPath + "photo/" + photo.getOriginalFilename());
        ByteArrayInputStream bais = new ByteArrayInputStream(photo.getBytes());
        BufferedImage bufferedImage = ImageIO.read(bais);
        File file = new File(path.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        ImageIO.write(Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, PICTURE_WIDTH, PICTURE_HEIGHT), "jpg", file);
        return "/" + file.toString().replace("\\", "/");
    }
}
