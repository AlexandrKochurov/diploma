package main.repositories;

import main.dto.TagInterfaceProjection;
import main.model.Tag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
    @Query(value = "select * from tags",
            nativeQuery = true)
    List<Tag> allTags();

    @Query(value = "select tags.name as name, " +
            "(select count(*) from tag2post where tag2post.tag_id = tags.id) / (select count(*) from posts where posts.is_active = 1 and posts.moderator_status = 'ACCEPTED' and posts.instant < now()) as weight " +
            "from tags " +
            "inner join tag2post, posts " +
            "where (:query is null or tags.name like :query) AND tags.id=tag2post.tag_id AND posts.id=tag2post.post_id AND posts.is_active= 1 AND posts.moderator_status= 'ACCEPTED' " +
            "group by tags.id",
            nativeQuery = true)
    List<TagInterfaceProjection> tagsByWeight(@Param("query") String query);

    @Transactional
    @Modifying
    @Query(value = "insert into tags(name) values (:tag)",
            nativeQuery = true)
    void addNewTag(@Param("tag") String tag);

    @Query(value = "select id from tags where tags.name like :tag",
            nativeQuery = true)
    Optional<Integer> getTagId(@Param("tag") String tag);
}