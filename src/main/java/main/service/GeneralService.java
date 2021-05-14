package main.service;

import main.api.request.*;
import main.api.response.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface GeneralService {
    String imageUpload(MultipartFile multipartFile) throws IOException;

    TagResponse tagsList(String query);

    CalendarResponse calendar(Integer year);

    ResultResponse changeProfileWithPhoto(MultipartFile photo, String email, String password, String name, Integer removePhoto) throws IOException;

    ResultResponse changeProfile(ChangeProfileRequest changeProfileRequest);

    StatisticResponse myStats();

    StatisticResponse allStats();

    void setGlobalSettings(SettingsRequest settingsRequest);

    Object comment(CommentRequest commentRequest);

    SettingsResponse getGlobalSettings();

    ResultResponse changeModeratorDecision(PostModerationRequest request);
}
