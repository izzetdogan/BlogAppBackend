package com.blog.service.impl;

import com.blog.dto.ApiResponse;
import com.blog.dto.PostDto;
import com.blog.dto.PostResponse;
import com.blog.dto.UserDto;
import com.blog.exceptions.AuthException;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.model.Category;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;
import com.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private  final PostRepository postRepository;
    private final UserRepository userRepository;
    private  final CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "userıd",userId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "categoory Id",categoryId));

        Post post = this.modelMapper.map(postDto,Post.class);
        post.setImageName("default.png");
        post.setCreatedAt(new Date());
        post.setUser(user);
        post.setCategory(category);
        Post newPost = postRepository.save(post);
        return modelMapper.map(newPost,PostDto.class);

    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "Post id", postId ));
        User user = modelMapper.map(currentUser(),User.class);
        if(post.getUser().getId()!= user.getId() ){
            throw new RuntimeException("UnAuthor to Update");
        }
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        post.setImageName(postDto.getImageName());

        Post updatedPost = postRepository.save(post);

        return  modelMapper.map(updatedPost,PostDto.class);

    }

    @Override
    public boolean deletePost(Integer postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","Post id",postId));
        User user = modelMapper.map(currentUser(),User.class);
        if(post.getUser().getId()!= user.getId() ){
            return false;
        }
        postRepository.delete(post);
        return true;
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc"))? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();


        Pageable p = PageRequest.of(pageNumber,pageSize, sort);
        Page<Post> pagePosts = postRepository.findAll(p);

        List<Post> posts = pagePosts.getContent();

        List<PostDto> postDtos = posts.stream()
                .map((post -> modelMapper.map(post,PostDto.class))).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());

        postResponse.setLastPage(pagePosts.isLast());


        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));
        return modelMapper.map(post,PostDto.class);

    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("catgory", "category Id", categoryId));
        List<Post>  posts = postRepository.findAllPostByCategory(category);

        List<PostDto> postDtos = posts.stream().map((post -> modelMapper.map(post, PostDto.class))).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getPostByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        List<Post> posts = postRepository.findAllPostByUser(user);

        List<PostDto> postDtos = posts.stream().map((post -> modelMapper.map(post, PostDto.class))).collect(Collectors.toList());
        return  postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {

        List<Post> posts = postRepository.findByTitleContaining(keyword);

        List<PostDto> postDtos = posts.stream()
                .map((post) -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        return postDtos;
    }



    private UserDto currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user =userRepository.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User", "email", 0));
        UserDto userDtos  = modelMapper.map(user,UserDto.class);
        return userDtos;
    }




}

