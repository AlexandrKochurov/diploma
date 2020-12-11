package main.service;

import main.dto.PostDTO;
import main.model.Post;
import java.util.List;

public interface PostService {
    //Метод возвращающий все посты
    List<PostDTO> getAllPostsByMode(int limit, int offset, String mode);

    //Метод возвращающий список постов по запросу
    List<PostDTO> searchPosts(int offset, int limit, String query);

    //Метод возвращающий список постов за указанную дату
    List<PostDTO> postsByDate(int offset, int limit, String date);

    //Список постов по тэгу
    List<PostDTO> postsByTag(int offset, int limit, String tag);

    //Список постов на модерацию
    List<PostDTO> postsForModeration(int offset, int limit, String status);

    //Список моих постов
    List<PostDTO> myPosts(int offset, int limit, int usetId, int active, String status);

    //Метод возвращающий конкретный пост, по ID
    PostDTO postById(int id) throws Exception;

    //Метод добавления поста
    void addPost(Post post);

    //Метод редактирования поста
    void editPost(int id, Post post) throws Exception;

    //Метод удаления поста
    void deletePost(int id) throws Exception;
}
