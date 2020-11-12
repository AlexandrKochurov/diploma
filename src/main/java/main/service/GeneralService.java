package main.service;

import main.model.Post;
import main.model.Tag;
import main.model.User;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface GeneralService {
    List<Post> postsByTag(String tag);

    String imageUpload(String path);

    void addComment(Integer parentId, Integer postId, String text);

    List<Tag> tagsList(String query);

    List<Integer> calendar(Integer year);

    User changeProfile(File photo, Integer removePhoto, String name, String email, String password);

    Map<String, Integer> myStats(User user);

    Map<String, Integer> allStats(User user);

    Map<String, String> getSettings();

    Map<String, String> setSettings();
}
