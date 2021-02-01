package main.repositories;

import main.model.PostVote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {
    @Query(value = "select count(*) from post_votes where value = 1",
    nativeQuery = true)
    int countAllLikes();

    @Query(value = "select count(*) from post_votes where value = -1",
            nativeQuery = true)
    int countAllDislikes();

    @Transactional
    @Modifying
    @Query(value = "insert into post_votes(post_id, time, user_id, value) values (:post_id, now(), :user_id, :value)",
    nativeQuery = true)
    void newLikeOrDislike(@Param("post_id") int postId, @Param("user_id") int userId, @Param("value") byte value);

    @Transactional
    @Modifying
    @Query(value = "update post_votes set value = :value where post_id = :post_id",
    nativeQuery = true)
    void changeLikeOrDislike(@Param("post_id") int postId, @Param("value") byte value);

}