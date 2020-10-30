package service;

import model.Post;
import model.Tag;
import model.User;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface GeneralService {
    public List<Post> postsByTag(String tag);

    public String imageUpload(String path);

    public void addComment(Integer parentId, Integer postId, String text);

    public List<Tag> tagsList(String query);

    public List<Integer> calendar(Integer year);

    public User changeProfile(File photo, Integer removePhoto, String name, String email, String password);

    public Map<String, Integer> myStats(User user);

    public Map<String, Integer> allStats(User user);

    public Map<String, String> getSettings();

    public Map<String, String> setSettings();
}
