package main.controller;

import main.api.request.AddOrEditPostRequest;
import main.api.request.LikeAndDislikeRequest;
import main.api.response.PostByIdResponse;
import main.api.response.PostsListResponse;
import main.api.response.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import main.service.impl.PostServiceImpl;


@RestController
@RequestMapping(value = "/api/post")
public class ApiPostController {

    private final PostServiceImpl postServiceImpl;

    private final byte LIKE = 1;
    private final byte DISLIKE = -1;

    @Autowired
    public ApiPostController(PostServiceImpl postServiceImpl) {
        this.postServiceImpl = postServiceImpl;
    }

    @GetMapping(value = "")
    public ResponseEntity<PostsListResponse> getPosts(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {
        return ResponseEntity.ok(postServiceImpl.getAllPostsByMode(offset, limit, mode));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PostsListResponse> searchPosts(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false) String query
    ) {
        return ResponseEntity.ok(postServiceImpl.searchPosts(offset, limit, query));
    }

    @GetMapping(value = "/byDate")
    public ResponseEntity<PostsListResponse> postsByDate(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "date", required = false) String date
    ) {
        return ResponseEntity.ok(postServiceImpl.postsByDate(offset, limit, date));
    }

    @GetMapping(value = "/byTag")
    public ResponseEntity<PostsListResponse> postsByTag(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "tag", required = false) String tag
    ) {
        return ResponseEntity.ok(postServiceImpl.postsByTag(offset, limit, tag));
    }

    @GetMapping(value = "/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<?> postsForModeration(
            @RequestParam(name = "offset", required = false) int offset,
            @RequestParam(name = "limit", required = false) int limit,
            @RequestParam(name = "status", required = false) String status
    ) {
        return ResponseEntity.ok(postServiceImpl.postsForModeration(offset, limit, status));
    }

    @GetMapping(value = "/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostsListResponse> myPosts(
            @RequestParam(name = "offset", required = false) int offset,
            @RequestParam(name = "limit", required = false) int limit,
            @RequestParam(name = "status", required = false) String status
    ) {
        return ResponseEntity.ok(postServiceImpl.myPosts(offset, limit, status));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostByIdResponse> postById(@PathVariable int id) {
        return ResponseEntity.ok(postServiceImpl.postById(id));
    }

    @PostMapping(value = "")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> addPost(@RequestBody AddOrEditPostRequest addPostRequest) {
        return ResponseEntity.ok(postServiceImpl.addPost(addPostRequest));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> editPost(
            @RequestBody AddOrEditPostRequest editPostRequest,
            @PathVariable(name = "id", required = false) int id
    ) throws Exception {
        return ResponseEntity.ok(postServiceImpl.editPost(id, editPostRequest));
    }

    @PostMapping(value = "/like")
    public ResponseEntity<ResultResponse> like(@RequestBody LikeAndDislikeRequest likeAndDislikeRequest){
        return ResponseEntity.ok(postServiceImpl.setLikeOrDislike(likeAndDislikeRequest.getPostId(), LIKE));
    }

    @PostMapping(value = "/dislike")
    public ResponseEntity<ResultResponse> dislike(@RequestBody LikeAndDislikeRequest likeAndDislikeRequest){
        return ResponseEntity.ok(postServiceImpl.setLikeOrDislike(likeAndDislikeRequest.getPostId(), DISLIKE));
    }
}
