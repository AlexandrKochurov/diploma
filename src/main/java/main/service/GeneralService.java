package main.service;

import main.api.request.ChangeProfileRequest;
import main.api.request.ImageUploadRequest;
import main.api.request.PostModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface GeneralService {
    String imageUpload(MultipartFile multipartFile) throws IOException;

    TagResponse tagsList(String query);

    CalendarResponse calendar(Integer year);

    ChangeProfileResponse changeProfile(ChangeProfileRequest changeProfileRequest) throws IOException;

    StatisticResponse myStats();

    StatisticResponse allStats();

    void setGlobalSettings(SettingsRequest settingsRequest);

    CommentResponse comment(int parentId, int postId, String text);

    SettingsResponse getGlobalSettings();

    ModeratorDecisionResponse changeModeratorDecision(PostModerationRequest request);
}
