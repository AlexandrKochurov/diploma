package main.service;

import main.api.request.*;
import main.api.response.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface GeneralService {
    String imageUpload(MultipartFile multipartFile) throws IOException;

    TagResponse tagsList(String query);

    CalendarResponse calendar(Integer year);

    ChangeProfileResponse changeProfileWithPhoto(MultipartFile photo, String email, String password, String name, Integer removePhoto) throws IOException;

    ChangeProfileResponse changeProfile(ChangeProfileRequest changeProfileRequest);

    StatisticResponse myStats();

    StatisticResponse allStats();

    void setGlobalSettings(SettingsRequest settingsRequest);

    CommentResponse comment(int parentId, int postId, String text);

    SettingsResponse getGlobalSettings();

    ModeratorDecisionResponse changeModeratorDecision(PostModerationRequest request);
}
