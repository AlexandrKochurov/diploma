package main.service;

import main.api.response.CalendarResponse;
import main.api.response.TagResponse;
import main.model.User;

import java.io.File;
import java.util.Map;

public interface GeneralService {
    String imageUpload(String path);

    void addComment(Integer parentId, Integer postId, String text);

    TagResponse tagsList(String query);

    CalendarResponse calendar(Integer year);

    User changeProfile(File photo, Integer removePhoto, String name, String email, String password);

    Map<String, Integer> myStats(User user);

    Map<String, Integer> allStats(User user);

    Map<String, String> getSettings();

    Map<String, String> setSettings();
}
