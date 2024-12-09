package com.tokorokoshi.tokoro.modules.blogs;

import com.tokorokoshi.tokoro.modules.blogs.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blogs.dto.CreateUpdateBlogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/blogs")
public class BlogsController {

    private final BlogsService blogsService;

    @Autowired
    public BlogsController(BlogsService blogsService) {
        this.blogsService = blogsService;
    }

    @PostMapping(value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> createBlog(
            @RequestBody CreateUpdateBlogDto createUpdateBlogDto
    ) {
        return ResponseEntity.ok(this.blogsService.saveBlog(createUpdateBlogDto));
    }

    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> getBlog(@PathVariable String id) {
        return ResponseEntity.ok(this.blogsService.getBlogById(id));
    }

    @GetMapping(value = {"", "/"},
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        return ResponseEntity.ok(this.blogsService.getAllBlogs());
    }

    @PutMapping(value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> updateBlog(@RequestBody CreateUpdateBlogDto createUpdateBlogDto,
                                              @PathVariable String id) {
        BlogDto updatedBlog = this.blogsService.updateBlog(id, createUpdateBlogDto);
        if (updatedBlog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBlog(@PathVariable String id) {
        this.blogsService.deleteBlog(id);
    }


}
