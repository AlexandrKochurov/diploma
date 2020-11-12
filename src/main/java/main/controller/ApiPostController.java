package main.controller;

import main.dto.PostDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import main.model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import main.service.ConverterPostDTO;
import main.service.impl.PostServiceImpl;

import java.util.Date;


@RestController
@RequestMapping(value = "/api/post")
@Api(value = "Api Post Controller")
public class ApiPostController {

    private final PostServiceImpl postServiceImpl;

    @Autowired
    public ApiPostController(PostServiceImpl postServiceImpl){
        this.postServiceImpl = postServiceImpl;
    }

    @GetMapping(value = "/")
    @ApiOperation(value = "Вывод списка постов", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно выведены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity getPosts(
            @RequestParam(name = "limit", required = false) Integer limit
    ){
        return ResponseEntity.ok(postServiceImpl.getPosts(limit));
    }

    @GetMapping(value = "/search/")
    @ApiOperation(value = "Вывод списка постов по запросу", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity searchPosts(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "limit", required = false) Integer limit
    ){
        return ResponseEntity.ok(postServiceImpl.searchPosts(query, limit));
    }

    @GetMapping(value = "/byDate/")
    @ApiOperation(value = "Вывод списка постов за указанную дату", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity postsByDate(@RequestBody Date date){
        return ResponseEntity.ok(postServiceImpl.postsByDate(date));
    }

    @GetMapping(value = "/moderation/")
    @ApiOperation(value = "Список постов на модерацию", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity postsForModeration(
            @RequestParam(name = "limit", required = false) Integer limit
    ){
        return ResponseEntity.ok(postServiceImpl.postsForModeration(limit));
    }

    @GetMapping(value = "/my/")
    @ApiOperation(value = "Список моих постов", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 404, message = "Посты не найдены")
    })
    public ResponseEntity myPosts(
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "id", required = false) Integer id
    ){
        return ResponseEntity.ok(postServiceImpl.myPosts(limit, id));
    }

    @GetMapping(value = "/{ID}/")
    @ApiOperation(value = "Вывод поста по id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пост успешно найден"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    public ResponseEntity postById(
            @RequestParam(name = "id", required = false) Integer id
    ) throws Exception {
        postServiceImpl.postById(id);
        return ResponseEntity.ok(postServiceImpl.postById(id));
    }

    @PostMapping(value = "/")
    @ApiOperation(value = "Добавление поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Пост успешно добавлен"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    public ResponseEntity addPost(@RequestBody PostDTO postDTO){
        Post post = ConverterPostDTO.convertDtoToModel(postDTO);
        postServiceImpl.addPost(post);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{ID}/")
    @ApiOperation(value = "Изменение поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Пост успешно изменен"),
            @ApiResponse(code = 404, message = "Пост не найден")
    })
    public ResponseEntity editPost(
            @RequestBody PostDTO postDTO,
            @RequestParam(name = "id", required = false) Integer id
    ) throws Exception {
        Post post = ConverterPostDTO.convertDtoToModel(postDTO);
        postServiceImpl.editPost(id, post);
        return ResponseEntity.ok().build();
    }
}
