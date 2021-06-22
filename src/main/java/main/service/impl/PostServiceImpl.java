package main.service.impl;

import main.api.request.AddOrEditPostRequest;
import main.api.response.*;
import main.dto.PostCommentsDTO;
import main.dto.PostDTO;
import main.dto.UserDTO;
import main.exceptions.FalseResultWithErrorsException;
import main.exceptions.NotFoundPostByIdException;
import main.exceptions.NotFoundPostsException;
import main.model.*;
import main.repositories.PostVoteRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import main.repositories.PostRepository;
import main.service.PostService;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final UserRepository userRepository;
    private final String NOTAUTHUSER = "anonymousUser";

    private final int ANNOUNCE_LENGTH = 50;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostVoteRepository postVoteRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postVoteRepository = postVoteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PostsListResponse getAllPostsByMode(int offset, int limit, String mode) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        int quantity = postRepository.countAllPosts();
        switch (mode) {
            case "recent":
                return new PostsListResponse(quantity, getPostsDTO(postRepository.recentPosts(pageable)));
            case "popular":
                return new PostsListResponse(quantity, getPostsDTO(postRepository.popularPosts(pageable)));
            case "best":
                return new PostsListResponse(quantity, getPostsDTO(postRepository.bestPosts(pageable)));
            case "early":
                return new PostsListResponse(quantity, getPostsDTO(postRepository.earlyPosts(pageable)));
            default:
                throw new NotFoundPostsException();
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
    public PostsListResponse myPosts(int offset, int limit, String status) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        int userId = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
        int isActive = 0;
        String moderatorStatus = null;
        switch (status) {
            case "inactive":
                break;

            case "pending":
                isActive = 1;
                moderatorStatus = "NEW";
                break;

            case "declined":
                isActive = 1;
                moderatorStatus = "DECLINED";
                break;

            case "published":
                isActive = 1;
                moderatorStatus = "ACCEPTED";
                break;
        }
        return new PostsListResponse(postRepository.countMyPostsByStatus(userId, moderatorStatus, isActive), getPostsDTO(postRepository.myPosts(pageable, userId, moderatorStatus, isActive)));
    }

    @Override
    public PostByIdResponse postById(int id) {
        Post post = postRepository.postByIdForModeration(id).orElseThrow(NotFoundPostByIdException::new);
        if (SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase(NOTAUTHUSER)) {
            post = postRepository.postById(id).orElseThrow(NotFoundPostByIdException::new);
            return getPostByIdResponse(post);
        }
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        //Если текущий юзер - не автор поста и не модератор то пост отображается с учетом статуса модерации, в противном случае этот статус не учитывается
        if (post.getUserId().getId() != user.getId() && user.getIsModerator() == 0) {
            post = postRepository.postById(id).orElseThrow(NotFoundPostByIdException::new);
            post.setViewCount(addViewToPost(post.getViewCount(), post));
            postRepository.save(post);
        }
        return getPostByIdResponse(post);
    }

    @Override
    public ResultResponse addPost(AddOrEditPostRequest addPostRequest) {
        Post post = new Post();
        Map<String, String> errors = addOrChangePost(post, addPostRequest);
        if (errors.isEmpty()) {
            return SimpleResultResponse.TRUE;
        } else {
            throw new FalseResultWithErrorsException(false, errors);
        }
    }

    @Override
    public ResultResponse editPost(int id, AddOrEditPostRequest editPostRequest) throws Exception {
        Post postToChange = postRepository.postByIdForModeration(id).orElseThrow(NotFoundPostByIdException::new);
        Map<String, String> errors = addOrChangePost(postToChange, editPostRequest);
        if (errors.isEmpty()) {
            return SimpleResultResponse.TRUE;
        } else {
            throw new FalseResultWithErrorsException(false, errors);
        }
    }

    @Override
    public void deletePost(int id) throws Exception {
        postRepository.findById(id).orElseThrow(() -> new Exception("Post doesn't exist"));
        postRepository.deleteById(id);
    }

    public ResultResponse setLikeOrDislike(int postId, byte vote) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase(NOTAUTHUSER)) {
            return SimpleResultResponse.FALSE;
        }

        Post post = postRepository.postById(postId).orElseThrow(NotFoundPostByIdException::new);
        int userId = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
        PostVote postVote = post.getPostVoteList() // если есть голос, получаем его, если нет голоса - создаем новый
                .stream()
                .filter(pv -> pv.getUserId() == userId)
                .findFirst()
                .orElseGet(PostVote::new);

        if (postVote.getValue() == vote) { // если голос тот же что и пробуют поставить возвращаем false
            return SimpleResultResponse.FALSE;
        }

        // иначе заполняем данными, новая дата, и данные которые нужны для нового голоса
        postVote.setTime(new Date());
        postVote.setPostId(postId);
        postVote.setUserId(userId);
        postVote.setValue(vote);

        postVoteRepository.save(postVote);

        return SimpleResultResponse.TRUE;
    }

    private List<PostDTO> getPostsDTO(List<Post> postList) {
        return postList.stream()
                .map(this::toPostDTO)
                .collect(toList());
    }

    private List<PostCommentsDTO> getPostCommentsDTO(List<PostComments> postCommentsList) {
        return postCommentsList.stream()
                .map(this::toPostCommentsDTO)
                .collect(toList());
    }

    private PostByIdResponse getPostByIdResponse(Post post) {
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
                post.getTagList().stream().map(Tag::getName).collect(Collectors.toList())
        );
    }

    private Map<String, String> addOrChangePost(Post post, AddOrEditPostRequest addOrEditPostRequest) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Map<String, String> errors = new HashMap<>();
        if (addOrEditPostRequest.getTitle().isBlank() || addOrEditPostRequest.getTitle().length() < 3) {
            errors.put("title", "Заголовок пустой или слишком короткий, минимум 3 символа");
        }
        if (addOrEditPostRequest.getText().isBlank() || addOrEditPostRequest.getText().length() < 50) {
            errors.put("text", "Текст поста пустой или слишком короткий, минимум 50 символов");
        }
        if (errors.isEmpty()) {
            post.setIsActive(addOrEditPostRequest.getActive());

            //Проверка, если текущий юзер - не модератор, то при изменении поста его статус будет меняться на NEW
            if (user.getIsModerator() == 0) {
                post.setModStatus(ModerationStatus.NEW);
            }
            //Проверка времени, если стоит раньше чем текущее, ставим текущее
            if (Instant.ofEpochSecond(addOrEditPostRequest.getTimestamp()).getEpochSecond() < Instant.now().getEpochSecond()) {
                post.setInstant(Instant.ofEpochSecond(Instant.now().getEpochSecond()));
            } else {
                post.setInstant(Instant.ofEpochSecond(addOrEditPostRequest.getTimestamp()));
            }

            post.setText(addOrEditPostRequest.getText());
            post.setTitle(addOrEditPostRequest.getTitle());
            post.setUserId(user);

            //Создание и добавление тегов
            List<Tag> tagList = new ArrayList<>();
            for (String name : addOrEditPostRequest.getTags()) {
                if(!addOrEditPostRequest.getTags().contains(name)) {
                    Tag tag = new Tag();
                    tag.setName(name);
                    tagList.add(tag);
                }
            }
            post.setTagList(tagList);

            postRepository.save(post);
        }
        return errors;
    }

    private int addViewToPost(int currentView, Post post) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        byte isModerator = user.getIsModerator();
        boolean isMine = user.getId() == post.getUserId().getId();
        if (isModerator == 1 || isMine) {
            return currentView;
        }
        return ++currentView;
    }

    private PostDTO toPostDTO(Post post){
        return new PostDTO(
                        post.getId(),
                        post.getInstant().getEpochSecond(),
                        post.getTitle(),
                        post.getText().substring(0, Math.min(post.getText().length(), ANNOUNCE_LENGTH)).replaceAll("\\<.*?>", ""),
                        (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == 1).count()),
                        (int) (post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == -1).count()),
                        post.getPostCommentsList().size(),
                        post.getViewCount(),
                        new UserDTO(post.getUserId().getId(), post.getUserId().getName())
                );
    }

    private PostCommentsDTO toPostCommentsDTO(PostComments comment){
        return new PostCommentsDTO(
                comment.getId(),
                comment.getTime().getEpochSecond(),
                comment.getText(),
                new UserDTO(comment.getUserId().getId(), comment.getUserId().getName(), comment.getUserId().getPhoto())
        );
    }
}
