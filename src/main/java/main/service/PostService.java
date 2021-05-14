package main.service;

import main.api.request.AddOrEditPostRequest;
import main.api.response.*;

public interface PostService {
    //Метод возвращающий все посты
    PostsListResponse getAllPostsByMode(int offset, int limit, String mode);

    //Метод возвращающий список постов по запросу
    PostsListResponse searchPosts(int offset, int limit, String query);

    //Метод возвращающий список постов за указанную дату
    PostsListResponse postsByDate(int offset, int limit, String date);

    //Список постов по тэгу
    PostsListResponse postsByTag(int offset, int limit, String tag);

    //Список постов на модерацию
    PostsListResponse postsForModeration(int offset, int limit, String status);

    //Список моих постов
    PostsListResponse myPosts(int offset, int limit, String status);

    //Метод возвращающий конкретный пост, по ID
    PostByIdResponse postById(int id) throws Exception;

    //Метод добавления поста
    ResultResponse addPost(AddOrEditPostRequest addPostRequest);

    //Метод редактирования поста
    ResultResponse editPost(int id, AddOrEditPostRequest editPostRequest) throws Exception;

    //Метод удаления поста
    void deletePost(int id) throws Exception;

    ResultResponse setLikeOrDislike(int postId, byte vote);
}
