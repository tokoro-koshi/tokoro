package com.tokorokoshi.tokoro.modules.blog;

import com.tokorokoshi.tokoro.modules.blog.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blog.dto.CreateUpdateBlogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    @PostMapping(value = {"", "/"},
                consumes = APPLICATION_JSON_VALUE,
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> createBlog(
            @RequestBody CreateUpdateBlogDto createUpdateBlogDto
            ){
        return ResponseEntity.ok(this.blogService.saveBlog(createUpdateBlogDto));
    }

    @GetMapping(value = "/{id}",
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> getBlog(@PathVariable String id){
        return ResponseEntity.ok(this.blogService.getBlogById(id));
    }

    @GetMapping(value = {"", "/"},
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BlogDto>> getAllBlogs(){
        return ResponseEntity.ok(this.blogService.getAllBlogs());
    }

    @PutMapping(value = "/{id}",
                consumes = APPLICATION_JSON_VALUE,
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogDto> updateBlog(@RequestBody CreateUpdateBlogDto createUpdateBlogDto,
                                              @PathVariable String id){
        BlogDto updatedBlog = this.blogService.updateBlog(id, createUpdateBlogDto);
        if(updatedBlog == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBlog(@PathVariable String id){
        this.blogService.deleteBlog(id);
    }


}
