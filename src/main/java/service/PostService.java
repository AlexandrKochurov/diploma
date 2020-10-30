package service;

import model.Post;

import java.util.Date;
import java.util.List;

public interface PostService {
    //Метод возвращающий все посты
    public List<Post> getPosts(int limit);

    //Метод возвращающий список постов по запросу
    public List<Post> searchPosts(String query, int limit);

    //Метод возвращающий список постов за указанную дату
    public List<Post> postsByDate(Date date);

    //Метод возвращающий список постов на модерацию
    public List<Post> postsForModeration(int limit);

    //Метод возвращающий список моих постов
    public List<Post> myPosts(int limit, int id);

    //Метод возвращающий конкретный пост, по ID
    public Post postById(int id) throws Exception;

    //Метод добавления поста
    public void addPost(Post post);

    //Метод редактирования поста
    public void editPost(int id, Post post) throws Exception;

    //Метод удаления поста
    public void deletePost(int id) throws Exception;
}
