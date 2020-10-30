package service.impl;

import model.Post;
import model.Tag;
import model.User;
import service.GeneralService;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GeneralServiceImpl implements GeneralService {
    @Override
    public List<Post> postsByTag(String tag) {
        return null;
    }

    @Override
    public String imageUpload(String path) {
        return null;
    }

    @Override
    public void addComment(Integer parentId, Integer postId, String text) {

    }

    @Override
    public List<Tag> tagsList(String query) {
        return null;
    }

    @Override
    public List<Integer> calendar(Integer year) {
        return null;
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
    public Map<String, Integer> allStats(User user) {
        return null;
    }

    @Override
    public Map<String, String> getSettings() {
        return null;
    }

    @Override
    public Map<String, String> setSettings() {
        return null;
    }
}
