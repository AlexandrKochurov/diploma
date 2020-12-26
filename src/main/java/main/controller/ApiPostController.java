package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import main.api.response.PostsListResponse;
import main.dto.PostsDTO;
import main.model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import main.service.impl.PostServiceImpl;



@RestController
@RequestMapping(value = "/api/post")
@Api(value = "Api Post Controller")
public class ApiPostController {

    private final PostServiceImpl postServiceImpl;

    @Autowired
    public ApiPostController(PostServiceImpl postServiceImpl){
        this.postServiceImpl = postServiceImpl;
    }

    @GetMapping(value = "")
    @ApiOperation(value = "Вывод списка постов", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно выведены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity<PostsListResponse> getPosts(
            @RequestParam(name = "offset", required = false, defaultValue= "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ){
        return ResponseEntity.ok(postServiceImpl.getAllPostsByMode(offset, limit, mode));
    }

    @GetMapping(value = "/search/")
    @ApiOperation(value = "Вывод списка постов по запросу", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity<PostsListResponse> searchPosts(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false) String query
    ){
        return ResponseEntity.ok(postServiceImpl.searchPosts(offset, limit, query));
    }

    @GetMapping(value = "/byDate/")
    @ApiOperation(value = "Вывод списка постов за указанную дату", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity<?> postsByDate(
            @RequestParam(name = "limit", required = false, defaultValue= "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "mode", required = false) String date
    ){
        return ResponseEntity.ok(postServiceImpl.postsByDate(offset, limit, date));
    }

    @GetMapping(value = "/byTag/")
    @ApiOperation(value = "Вывод списка постов за указанную дату", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity<?> postsByTag(
            @RequestParam(name = "limit", required = false, defaultValue= "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "mode", required = false) String tag
    ){
        return ResponseEntity.ok(postServiceImpl.postsByTag(offset, limit, tag));
    }

    @GetMapping(value = "/moderation/")
    @ApiOperation(value = "Список постов на модерацию", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity<?> postsForModeration(
            @RequestParam(name = "offset", required = false) int offset,
            @RequestParam(name = "limit", required = false) int limit,
            @RequestParam(name = "status", required = false) String status
    ){
        return ResponseEntity.ok(postServiceImpl.postsForModeration(offset, limit, status));
    }

    @GetMapping(value = "/my/")
    @ApiOperation(value = "Список моих постов", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity<?> myPosts(
            @RequestParam(name = "offset", required = false) int offset,
            @RequestParam(name = "limit", required = false) int limit,
            @RequestParam(name = "id", required = false) int id,
            @RequestParam(name = "active", required = false) int active,
            @RequestParam(name = "status", required = false) String status
    ){
        return ResponseEntity.ok(postServiceImpl.myPosts(offset, limit, id, active, status));
    }

    @GetMapping(value = "/{id}/")
    @ApiOperation(value = "Вывод поста по id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пост успешно найден"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    public ResponseEntity<PostsDTO> postById(@PathVariable int id) {
        postServiceImpl.postById(id);
        return ResponseEntity.ok(postServiceImpl.postById(id));
    }

    @PostMapping(value = "")
    @ApiOperation(value = "Добавление поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Пост успешно добавлен"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    public ResponseEntity<?> addPost(@RequestBody Post post){
        postServiceImpl.addPost(post);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/")
    @ApiOperation(value = "Изменение поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Пост успешно изменен"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    public ResponseEntity<?> editPost(
            @RequestBody Post post,
            @RequestParam(name = "id", required = false) Integer id
    ) throws Exception {
        postServiceImpl.editPost(id, post);
        return ResponseEntity.ok().build();
    }
}
