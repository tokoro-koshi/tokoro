package com.tokorokoshi.tokoro.modules.blog;

import com.tokorokoshi.tokoro.database.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public BlogService(MongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;
    }

    //Crud
    public void saveBlog(Blog blog){
        mongoTemplate.save(blog);
    }

    public Blog getBlogById(String id){
        return mongoTemplate.findById(id, Blog.class);
    }

    public List<Blog> getAllBlogs(){
        return mongoTemplate.findAll(Blog.class);
    }

    public void updateBlog(String id, Blog blog){
        blog.setId(id);
        mongoTemplate.save(blog);
    }

    public void deleteBlog(String id){
        mongoTemplate.remove(getBlogById(id));
    }
}
