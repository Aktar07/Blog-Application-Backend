package com.springboot.blog.services;

import com.springboot.blog.payloads.PostDto;
import com.springboot.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);
    PostDto getAllPostsById(long id);
    PostDto updatePost(PostDto postDto,long id);
    void deletePostById(long id);
    List<PostDto> getPostsByCategory(Long categoryId);

}
