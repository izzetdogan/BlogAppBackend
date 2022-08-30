package com.blog.repository;

import com.blog.model.Category;
import com.blog.model.Post;
import com.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllPostByUser(User user);
    List<Post> findAllPostByCategory(Category category);

    List<Post> findByTitleContaining(String title);

}
