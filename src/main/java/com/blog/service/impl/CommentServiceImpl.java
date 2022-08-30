package com.blog.service.impl;

import com.blog.dto.CommentDto;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final  PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {

        Post post =postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "post id", postId));

        Comment comment = modelMapper.map(commentDto,Comment.class);
        comment.setPost(post);
        Comment saved = commentRepository.save(comment);

        return modelMapper.map(saved,CommentDto.class);


    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","comment id", commentId));
    }
}
