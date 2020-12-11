package main.service.impl;

import main.dto.PostDTO;
import main.dto.UserDTO;
import main.model.ModerationStatus;
import main.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import main.repositories.PostRepository;
import main.service.PostService;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final int ANNOUNCE_LENGTH = 33;

    @Autowired
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDTO> getAllPostsByMode(int limit, int offset, String mode) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        switch (mode) {
            case "recent" : return getPostsDTO(postRepository.recentPosts(pageable));
            case "popular": return getPostsDTO(postRepository.popularPosts(pageable));
            case "best": return getPostsDTO(postRepository.bestPosts(pageable));
            case "early": return getPostsDTO(postRepository.earlyPosts(pageable));
            default: return null;
        }
    }

    @Override
    public List<PostDTO> searchPosts(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return getPostsDTO(postRepository.searchPosts(pageable, query));
    }

    @Override
    public List<PostDTO> postsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return getPostsDTO(postRepository.postsByDate(pageable, date));
    }

    @Override
    public List<PostDTO> postsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return getPostsDTO(postRepository.postsByTag(pageable, tag));
    }

    @Override
    public List<PostDTO> postsForModeration(int offset, int limit, String status) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return getPostsDTO(postRepository.postsForModeration(pageable, status));
    }

    @Override
    public List<PostDTO> myPosts(int offset, int limit, int userId, int active, String status) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return getPostsDTO(postRepository.myPosts(pageable, userId, status, active));
    }

    @Override
    public PostDTO postById(int id){
        Post post = postRepository.findById(id).get();
        return new PostDTO(post.getId(),
                post.getInstant(),
                post.getTitle(),
                post.getText().substring(0, ANNOUNCE_LENGTH),
                (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == 1).count()),
                (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == -1).count()),
                post.getPostVoteList().size(),
                post.getViewCount(),
                new UserDTO(post.getUserId().getId(), post.getUserId().getName()));
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
                                        post.getInstant(),
                                        post.getTitle(),
                                        post.getText().substring(0, ANNOUNCE_LENGTH),
                                        (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == 1).count()),
                                        (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == -1).count()),
                                        post.getPostVoteList().size(),
                                        post.getViewCount(),
                                        new UserDTO(post.getUserId().getId(), post.getUserId().getName())))
                .collect(toList());
    }
}
