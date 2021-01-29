package main.service.impl;

import main.api.response.PostByIdResponse;
import main.api.response.PostsListResponse;
import main.dto.PostCommentsDTO;
import main.dto.PostDTO;
import main.dto.UserDTO;
import main.exceptions.NotFoundPostByIdException;
import main.exceptions.NotFoundPostsException;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComments;
import main.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import main.repositories.PostRepository;
import main.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final int ANNOUNCE_LENGTH = 23;

    @Autowired
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public PostsListResponse getAllPostsByMode(int offset, int limit, String mode) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        int quantity = postRepository.countAllPosts();
        switch (mode) {
            case "recent" : return new PostsListResponse(quantity, getPostsDTO(postRepository.recentPosts(pageable)));
            case "popular": return new PostsListResponse(quantity, getPostsDTO(postRepository.popularPosts(pageable)));
            case "best": return new PostsListResponse(quantity, getPostsDTO(postRepository.bestPosts(pageable)));
            case "early": return new PostsListResponse(quantity, getPostsDTO(postRepository.earlyPosts(pageable)));
            default: throw new NotFoundPostsException();
        }
    }

    @Override
    public PostsListResponse searchPosts(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return new PostsListResponse(postRepository.countSearchPosts(query), getPostsDTO(postRepository.searchPosts(pageable, query)));
    }

    @Override
    public PostsListResponse postsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return new PostsListResponse(postRepository.countPostsByDate(date), getPostsDTO(postRepository.postsByDate(pageable, date)));
    }

    @Override
    public PostsListResponse postsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return new PostsListResponse(postRepository.countPostsByTag(tag), getPostsDTO(postRepository.postsByTag(pageable, tag)));
    }

    @Override
    public PostsListResponse postsForModeration(int offset, int limit, String status) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return new PostsListResponse(postRepository.countPostsForModeration(status), getPostsDTO(postRepository.postsForModeration(pageable, status)));
    }

    @Override
    public PostsListResponse myPosts(int offset, int limit, int userId, int active, String status) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return new PostsListResponse(postRepository.countMyPosts(userId, status, active), getPostsDTO(postRepository.myPosts(pageable, userId, status, active)));
    }

    @Override
    public PostByIdResponse postById(int id) {
        Post post = postRepository.postById(id)
                .orElseThrow(NotFoundPostByIdException::new);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return getPostByIdResponse(post);
    }

    @Override
    public void addPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void editPost(int id, Post post) throws Exception {
        post.setModStatus(ModerationStatus.NEW);
        postRepository.findById(id).orElseThrow(() -> new Exception("Post doesn't exist"));
        postRepository.save(post);
    }

    @Override
    public void deletePost(int id) throws Exception {
        postRepository.findById(id).orElseThrow(() -> new Exception("Post doesn't exist"));
        postRepository.deleteById(id);
    }

    private List<PostDTO> getPostsDTO(List<Post> postList) {
        return postList.stream()
                .map(
                        post ->
                                new PostDTO(
                                        post.getId(),
                                        post.getInstant().getEpochSecond(),
                                        post.getTitle(),
                                        post.getText().substring(0, Math.min(post.getText().length(), ANNOUNCE_LENGTH)),
                                        (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == 1).count()),
                                        (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == -1).count()),
                                        post.getPostCommentsList().size(),
                                        post.getViewCount(),
                                        new UserDTO(post.getUserId().getId(), post.getUserId().getName())
                                        ))
                .collect(toList());
    }

    private List<PostCommentsDTO> getPostCommentsDTO(List<PostComments> postCommentsList){
        return postCommentsList.stream()
                .map(
                        comment ->
                                new PostCommentsDTO(
                                        comment.getId(),
                                        comment.getTime().getEpochSecond(),
                                        comment.getText(),
                                        new UserDTO(comment.getUserId().getId(), comment.getUserId().getName(), comment.getUserId().getPhoto())
                                )
                )
                .collect(toList());
    }

    private PostByIdResponse getPostByIdResponse(Post post){
        return new PostByIdResponse(
                post.getId(),
                post.getInstant().getEpochSecond(),
                post.getIsActive() == 1,
                new UserDTO(post.getUserId().getId(), post.getUserId().getName()),
                post.getTitle(),
                post.getText(),
                (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == 1).count()),
                (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == -1).count()),
                post.getViewCount(),
                getPostCommentsDTO(post.getPostCommentsList()),
                post.getTagList().stream().filter(tag -> tag.getId() == post.getId()).map(Tag::getName).collect(Collectors.toList())
        );
    }

    private int addViewToPost(int currentView){
        return currentView + 1;
    }
}
