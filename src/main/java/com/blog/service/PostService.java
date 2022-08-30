package com.blog.service;

import com.blog.dto.PostDto;
import com.blog.dto.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostService  {

    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    PostDto updatePost(PostDto postDto, Integer postId );

    boolean deletePost(Integer post);

    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostDto getPostById(Integer postId);

    List<PostDto> getPostsByCategory(Integer categoryId);

    List<PostDto> getPostByUser(Integer userId);

    List<PostDto> searchPosts(String keyword);
}
