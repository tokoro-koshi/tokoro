package com.tokorokoshi.tokoro.modules.blogs;

import com.tokorokoshi.tokoro.modules.blogs.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blogs.dto.CreateUpdateBlogDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Blogs", description = "API for managing blogs")
@RestController
@RequestMapping("/api/blogs")
public class BlogsController {
    private final BlogsService blogsService;
    private final Logger logger;
    private final PagedResourcesAssembler<BlogDto> pagedResourcesAssembler;

    @Autowired
    public BlogsController(
            BlogsService blogsService,
            PagedResourcesAssembler<BlogDto> pagedResourcesAssembler
    ) {
        this.blogsService = blogsService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.logger = Logger.getLogger(BlogsController.class.getName());
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
        var blog = this.blogsService.getBlogById(id);
        if (blog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(blog);
    }

    @Operation(
            summary = "Get all blogs",
            description = "Returns a paginated list of all blogs"
    )
    @GetMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PagedModel<EntityModel<BlogDto>>> getAllBlogs(
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
        var blogs = this.blogsService.getAllBlogs(pageable);
        return ResponseEntity.ok(this.pagedResourcesAssembler.toModel(blogs));
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
    public ResponseEntity<?> deleteBlog(
            @Parameter(
                    description = "The ID of the blog to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            this.blogsService.deleteBlog(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
