package com.tokorokoshi.tokoro.modules.blogs;

import com.tokorokoshi.tokoro.database.Blog;
import com.tokorokoshi.tokoro.modules.blogs.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blogs.dto.CreateUpdateBlogDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    Blog toBlogSchema(CreateUpdateBlogDto createUpdateBlogDto);

    BlogDto toBlogDto(Blog blogSchema);

    List<BlogDto> toBlogDto(List<Blog> blogList);

    Blog toBlogSchema(BlogDto blogDto);
}
