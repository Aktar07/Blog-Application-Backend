package com.springboot.blog.controllers;

import com.springboot.blog.payloads.PostDto;
import com.springboot.blog.payloads.PostResponse;
import com.springboot.blog.services.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "CRUD REST APIs for Post Resource")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Post Rest API",
            description = "Create Post REST API is used to save post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Get all Posts Rest API",
            description = "GetALL posts from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    public PostResponse getAllPosts(@RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                    @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false)int pageSize,
                                    @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)String sortBy,
                                    @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)String sortDir){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Get Post Rest API",
            description = "Get Post BY Id REST API is used to get a single post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getAllPostsById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Post Rest API",
            description = "Update Post BY Id REST API is used to update a single post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable(name = "id") long id){
        PostDto postResponse = postService.updatePost(postDto,id);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Delete Post Rest API",
            description = "Delete Post BY Id REST API is used to delete post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post is deleted successfully",HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategoryId(@PathVariable(name= "id") Long categoryId){
        List<PostDto> postDtos = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}
