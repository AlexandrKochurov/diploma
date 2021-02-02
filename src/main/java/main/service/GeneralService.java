package main.service;

import main.api.request.SettingsRequest;
import main.api.response.*;
import main.model.User;

import java.io.File;
import java.util.Map;

public interface GeneralService {
    String imageUpload(String path);

    TagResponse tagsList(String query);

    CalendarResponse calendar(Integer year);

    User changeProfile(File photo, Integer removePhoto, String name, String email, String password);

    Map<String, Integer> myStats(User user);

    StatisticResponse allStats(User user);

    void setGlobalSettings(SettingsRequest settingsRequest);

    CommentResponse comment(int parentId, int postId, String text);

    SettingsResponse getGlobalSettings();
}
