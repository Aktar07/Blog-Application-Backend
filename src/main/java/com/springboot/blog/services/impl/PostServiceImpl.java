package com.springboot.blog.services.impl;

import com.springboot.blog.entities.Category;
import com.springboot.blog.entities.Post;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payloads.PostDto;
import com.springboot.blog.payloads.PostResponse;
import com.springboot.blog.repositories.CategoryRepository;
import com.springboot.blog.repositories.PostRepository;
import com.springboot.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;

    private CategoryRepository categoryRepository;
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper,CategoryRepository categoryRepository) {

        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));

        //convert Dto to entity

        Post post = mapToEntity(postDto);
        post.setCategory(category);

        Post newPost = postRepository.save(post);

        //convert entity to Dto
        PostDto postResponse = mapToDTO(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        //get content for page object
        List<Post> postList = posts.getContent();

        List<PostDto> content = postList.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getAllPostsById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by id and if not found throw exception
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));

        Category category = categoryRepository.findById(postDto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map((post) -> mapToDTO(post)).collect(Collectors.toList());
    }

    //convert entity to Dto
    private PostDto mapToDTO(Post post){
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        PostDto postDto = mapper.map(post,PostDto.class);
        return postDto;
    }

    //convert Entity into DTO
    private Post mapToEntity(PostDto postDto){
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        Post post = mapper.map(postDto,Post.class);
        return post;
    }


}
