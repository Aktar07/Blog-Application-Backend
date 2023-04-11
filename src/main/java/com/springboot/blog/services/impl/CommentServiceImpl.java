package com.springboot.blog.services.impl;

import com.springboot.blog.entities.Comment;
import com.springboot.blog.entities.Post;
import com.springboot.blog.exceptions.BlogAPIException;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payloads.CommentDto;
import com.springboot.blog.repositories.CommentRepository;
import com.springboot.blog.repositories.PostRepository;
import com.springboot.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId));

        //set post to comment entity
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        //retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert list comment entities to list comment DTOs
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        //retrieve post by post id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);


    }

    @Override
    public void deleteComment(long postId, Long commentId) {
        //retrieve post by post id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDTO(Comment comment){
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        CommentDto commentDto = mapper.map(comment,CommentDto.class);
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        Comment comment = mapper.map(commentDto, Comment.class);
        return comment;
    }
}
