package com.alkemy.apiBlog.repositories;

import com.alkemy.apiBlog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    @Query("SELECT c FROM Post c WHERE c.title LIKE :q or c.content LIKE :q"
            + " or c.creationDate LIKE :q or c.category LIKE :q")
    public List<Post> findAllByQ(@Param("q") String q);

    @Query("SELECT c FROM Post c WHERE c.id like :id")
    public Post searchById (@Param("id") String id);
}