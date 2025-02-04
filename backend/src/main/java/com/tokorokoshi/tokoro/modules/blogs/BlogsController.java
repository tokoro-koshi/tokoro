package com.tokorokoshi.tokoro.modules.blogs;

import com.tokorokoshi.tokoro.modules.blogs.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blogs.dto.CreateUpdateBlogDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Blogs", description = "API for managing blogs")
@RestController
@RequestMapping("/api/blogs")
public class BlogsController {
    private final BlogsService blogsService;

    @Autowired
    public BlogsController(BlogsService blogsService) {
        this.blogsService = blogsService;
    }

    @Operation(
            summary = "Create a new blog",
            description = "Accepts a request with a JSON body to create a new blog, and returns the created blog"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BlogDto> createBlog(
            @Parameter(description = "The blog to create", required = true)
            @RequestBody
            CreateUpdateBlogDto createUpdateBlogDto
    ) {
        return ResponseEntity.ok(this.blogsService.saveBlog(createUpdateBlogDto));
    }

    @Operation(
            summary = "Get a blog by ID",
            description = "Returns the blog with the given ID"
    )
    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BlogDto> getBlog(
            @Parameter(
                    description = "The ID of the blog to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        return ResponseEntity.ok(this.blogsService.getBlogById(id));
    }

    @Operation(
            summary = "Get all blogs",
            description = "Returns a paginated list of all blogs"
    )
    @GetMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<BlogDto>> getAllBlogs(
            @Parameter(
                    description = "The page number to get",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(this.blogsService.getAllBlogs(pageable));
    }

    @Operation(
            summary = "Update a blog",
            description = "Accepts a request with a JSON body to update a blog, and returns the updated blog"
    )
    @PutMapping(
            value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BlogDto> updateBlog(
            @Parameter(description = "The blog to update", required = true)
            @RequestBody
            CreateUpdateBlogDto createUpdateBlogDto,
            @Parameter(
                    description = "The ID of the blog to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        BlogDto updatedBlog = this.blogsService.updateBlog(
                id,
                createUpdateBlogDto
        );
        if (updatedBlog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBlog);
    }

    @Operation(
            summary = "Delete a blog",
            description = "Deletes the blog with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public void deleteBlog(
            @Parameter(
                    description = "The ID of the blog to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        this.blogsService.deleteBlog(id);
    }
}
