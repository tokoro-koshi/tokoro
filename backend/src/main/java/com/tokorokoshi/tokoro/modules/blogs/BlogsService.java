package com.tokorokoshi.tokoro.modules.blogs;

import com.tokorokoshi.tokoro.database.Blog;
import com.tokorokoshi.tokoro.modules.blogs.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blogs.dto.CreateUpdateBlogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogsService {
    private final MongoTemplate repository;
    private final BlogMapper blogMapper;

    @Autowired
    public BlogsService(
        MongoTemplate repository,
        BlogMapper blogMapper
    ) {
        this.blogMapper = blogMapper;
        this.repository = repository;
    }

    public BlogDto saveBlog(CreateUpdateBlogDto blog) {
        Blog blogSchema = blogMapper.toBlogSchema(blog);
        return blogMapper.toBlogDto(repository.save(blogSchema));
    }

    public BlogDto getBlogById(String id) {
        Blog blog = repository.findById(id, Blog.class);
        if (blog == null) {
            return null;
        }
        return blogMapper.toBlogDto(blog);
    }

    public Page<BlogDto> getAllBlogs(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<BlogDto> blogDtos = blogMapper.toBlogDto(
                repository.find(query, Blog.class)
        );
        long count = repository.count(query, Blog.class);
        return new PageImpl<>(blogDtos, pageable, count);
    }

    public BlogDto updateBlog(String id, CreateUpdateBlogDto blog) {
        if (getBlogById(id) == null) {
            return null;
        }
        Blog blogSchema = blogMapper.toBlogSchema(blog);
        return blogMapper.toBlogDto(repository.save(blogSchema.withId(id)));
    }

    public void deleteBlog(String id) {
        repository.remove(blogMapper.toBlogSchema(getBlogById(id)));
    }
}
