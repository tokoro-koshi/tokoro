package com.tokorokoshi.tokoro.modules.blogs;

import com.tokorokoshi.tokoro.database.Blog;
import com.tokorokoshi.tokoro.modules.blogs.dto.BlogDto;
import com.tokorokoshi.tokoro.modules.blogs.dto.CreateUpdateBlogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(target = "comments", source = "commentDtos")
    Blog toBlogSchema(CreateUpdateBlogDto createUpdateBlogDto);

    @Mapping(target = "commentDtos", source = "comments")
    BlogDto toBlogDto(Blog blogSchema);

    @Mapping(target = "commentDtos", source = "comments")
    List<BlogDto> toBlogDto(List<Blog> blogList);

    @Mapping(target = "comments", source = "commentDtos")
    Blog toBlogSchema(BlogDto blogDto);
}
