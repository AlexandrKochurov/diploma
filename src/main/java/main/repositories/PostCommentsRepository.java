package main.repositories;

import main.model.PostComments;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PostCommentsRepository extends CrudRepository<PostComments, Integer> {
    @Transactional
    @Modifying
    @Query(value = "insert into post_comments(post_id, text, time, user_id) values(:post_id, :text, now(), :user_id)",
    nativeQuery = true)
    void newCommentToPost(@Param("post_id") int postId, @Param("text") String text, @Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query(value = "insert into post_comments(parent_id, post_id, text, time) values(:parent_id, :post_id, :text, now(), :user_id)",
    nativeQuery = true)
    void newCommentToComment(@Param("parent_id") int parentId, @Param("post_id") int postId, @Param("text") String text,  @Param("user_id") int userId);

    @Query(value = "select last_insert_id() from post_comments",
    nativeQuery = true)
    int getLastCommentId();

    @Query(value = "select parent_id from post_comments where parent_id = :parent_id",
    nativeQuery = true)
    int checkParent(@Param("parent_id") int parentId);
}