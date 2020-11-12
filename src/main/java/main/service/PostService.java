package main.service;

import main.model.Post;

import java.util.Date;
import java.util.List;

public interface PostService {
    //Метод возвращающий все посты
    List<Post> getPosts(int limit);

    //Метод возвращающий список постов по запросу
    List<Post> searchPosts(String query, int limit);

    //Метод возвращающий список постов за указанную дату
    List<Post> postsByDate(Date date);

    //Метод возвращающий список постов на модерацию
    List<Post> postsForModeration(int limit);

    //Метод возвращающий список моих постов
    List<Post> myPosts(int limit, int id);

    //Метод возвращающий конкретный пост, по ID
    Post postById(int id) throws Exception;

    //Метод добавления поста
    void addPost(Post post);

    //Метод редактирования поста
    void editPost(int id, Post post) throws Exception;

    //Метод удаления поста
    void deletePost(int id) throws Exception;
}
