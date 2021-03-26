package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import main.api.request.AddOrEditPostRequest;
import main.api.request.LikeAndDislikeRequest;
import main.api.response.LikeDislikeResponse;
import main.api.response.PostByIdResponse;
import main.api.response.PostsListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import main.service.impl.PostServiceImpl;


@RestController
@RequestMapping(value = "/api/post")
@Api(value = "Api Post Controller")
public class ApiPostController {

    private final PostServiceImpl postServiceImpl;

    private final byte LIKE = 1;
    private final byte DISLIKE = -1;

    @Autowired
    public ApiPostController(PostServiceImpl postServiceImpl) {
        this.postServiceImpl = postServiceImpl;
    }

    @GetMapping(value = "")
    @ApiOperation(value = "Вывод списка постов", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Posts either found or not :)")
    public ResponseEntity<PostsListResponse> getPosts(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {
        return ResponseEntity.ok(postServiceImpl.getAllPostsByMode(offset, limit, mode));
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "Вывод списка постов по запросу", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Posts either found or not :)")
    public ResponseEntity<PostsListResponse> searchPosts(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false) String query
    ) {
        return ResponseEntity.ok(postServiceImpl.searchPosts(offset, limit, query));
    }

    @GetMapping(value = "/byDate")
    @ApiOperation(value = "Вывод списка постов за указанную дату", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Posts either found or not :)")
    public ResponseEntity<PostsListResponse> postsByDate(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "date", required = false) String date
    ) {
        return ResponseEntity.ok(postServiceImpl.postsByDate(offset, limit, date));
    }

    @GetMapping(value = "/byTag")
    @ApiOperation(value = "Вывод списка постов по тегу", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Posts either found or not :)")
//    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostsListResponse> postsByTag(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "tag", required = false) String tag
    ) {
        return ResponseEntity.ok(postServiceImpl.postsByTag(offset, limit, tag));
    }

    @GetMapping(value = "/moderation")
    @ApiOperation(value = "Список постов на модерацию", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Posts either found or not :)")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<?> postsForModeration(
            @RequestParam(name = "offset", required = false) int offset,
            @RequestParam(name = "limit", required = false) int limit,
            @RequestParam(name = "status", required = false) String status
    ) {
        return ResponseEntity.ok(postServiceImpl.postsForModeration(offset, limit, status));
    }

    @GetMapping(value = "/my")
    @ApiOperation(value = "Список моих постов", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Posts either found or not :)")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostsListResponse> myPosts(
            @RequestParam(name = "offset", required = false) int offset,
            @RequestParam(name = "limit", required = false) int limit,
            @RequestParam(name = "status", required = false) String status
    ) {
        return ResponseEntity.ok(postServiceImpl.myPosts(offset, limit, status));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Вывод поста по id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пост успешно найден"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
//    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostByIdResponse> postById(@PathVariable int id) {
        return ResponseEntity.ok(postServiceImpl.postById(id));
    }

    @PostMapping(value = "")
    @ApiOperation(value = "Добавление поста", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пост успешно добавлен"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> addPost(@RequestBody AddOrEditPostRequest addPostRequest) {
        return ResponseEntity.ok(postServiceImpl.addPost(addPostRequest));
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Изменение поста", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пост успешно изменен"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> editPost(
            @RequestBody AddOrEditPostRequest editPostRequest,
            @PathVariable(name = "id", required = false) int id
    ) throws Exception {
        postServiceImpl.editPost(id, editPostRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/like")
    @ApiOperation(value = "Лайк поста", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Лайк поставлен")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LikeDislikeResponse> like(@RequestBody LikeAndDislikeRequest likeAndDislikeRequest){
        return ResponseEntity.ok(postServiceImpl.setLikeOrDislike(likeAndDislikeRequest.getPostId(), LIKE));
    }

    @PostMapping(value = "/dislike")
    @ApiOperation(value = "Дизлайк поста", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Дизлайк поставлен")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LikeDislikeResponse> dislike(@RequestBody LikeAndDislikeRequest likeAndDislikeRequest){
        return ResponseEntity.ok(postServiceImpl.setLikeOrDislike(likeAndDislikeRequest.getPostId(), DISLIKE));
    }
}
