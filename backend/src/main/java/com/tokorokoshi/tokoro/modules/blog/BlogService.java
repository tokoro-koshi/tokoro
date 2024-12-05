package com.tokorokoshi.tokoro.modules.blog;

import com.tokorokoshi.tokoro.database.Blog;
import com.tokorokoshi.tokoro.modules.blog.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blog.dto.CreateUpdateBlogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    private final MongoTemplate mongoTemplate;
    private final BlogMapper blogMapper;

    @Autowired
    public BlogService(
            MongoTemplate mongoTemplate,
            BlogMapper blogMapper)
    {
        this.blogMapper = blogMapper;
        this.mongoTemplate = mongoTemplate;
    }

    //Crud
    public BlogDto saveBlog(CreateUpdateBlogDto blog){
        Blog blogSchema = blogMapper.toBlogSchema(blog);
        return blogMapper.toBlogDto(mongoTemplate.save(blogSchema));
    }

    public BlogDto getBlogById(String id){
        return blogMapper.toBlogDto(mongoTemplate.findById(id, Blog.class));
    }

    public List<BlogDto> getAllBlogs(){
        return blogMapper.toBlogDto(mongoTemplate.findAll(Blog.class));
    }

    public BlogDto updateBlog(String id, CreateUpdateBlogDto blog){
        Blog blogSchema = blogMapper.toBlogSchema(blog);
        blogSchema.setId(id);
        return blogMapper.toBlogDto(mongoTemplate.save(blogSchema));
    }

    public void deleteBlog(String id){
        mongoTemplate.remove(blogMapper.toBlogSchema(getBlogById(id)));
    }
}