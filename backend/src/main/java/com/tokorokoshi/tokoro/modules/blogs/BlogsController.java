package com.tokorokoshi.tokoro.modules.blogs;

import com.tokorokoshi.tokoro.modules.blogs.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blogs.dto.CreateUpdateBlogDto;
import io.swagger.v3.oas.annotations.Operation;
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
    @PostMapping(value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> createBlog(
            @RequestBody CreateUpdateBlogDto createUpdateBlogDto
    ) {
        return ResponseEntity.ok(this.blogsService.saveBlog(createUpdateBlogDto));
    }

    @Operation(
            summary = "Get a blog by ID",
            description = "Returns the blog with the given ID"
    )
    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> getBlog(@PathVariable String id) {
        return ResponseEntity.ok(this.blogsService.getBlogById(id));
    }

    @Operation(
            summary = "Get all blogs",
            description = "Returns a paginated list of all blogs"
    )
    @GetMapping(value = {"", "/"},
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BlogDto>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(this.blogsService.getAllBlogs(pageable));
    }

    @Operation(
            summary = "Update a blog",
            description = "Accepts a request with a JSON body to update a blog, and returns the updated blog"
    )
    @PutMapping(value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> updateBlog(
            @RequestBody CreateUpdateBlogDto createUpdateBlogDto,
            @PathVariable String id
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
    public void deleteBlog(@PathVariable String id) {
        this.blogsService.deleteBlog(id);
    }
}
