package com.springboot.blog.payloads;

import com.springboot.blog.entities.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(
        description = "PostDto Model Information"
)
public class PostDto {
    private Long id;

    @NotEmpty
    @Size(min = 2, message = "Post title should have atleast 2 characters")
    @Schema(description = "Blog Post Title")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Post description should have atleast 10 characters")
    @Schema(description = "Blog Post Description")
    private String description;

    @NotEmpty
    @Schema(description = "Blog Post Content")
    private String content;
    private Set<CommentDto> comments;

    @Schema(description = "Blog Post Category")
    private Long categoryId;
}
