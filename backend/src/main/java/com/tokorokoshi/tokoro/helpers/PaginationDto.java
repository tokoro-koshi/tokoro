package com.tokorokoshi.tokoro.helpers;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;

import java.util.List;

/**
 * A DTO for pagination.
 *
 * @param <T> The type of the payload
 */
@Schema(
        name = "Pagination",
        description = "A DTO for pagination"
)
public record PaginationDto<T>(
        List<T> payload,
        Links links,
        PageMetadata meta
) {
    /**
     * Create an empty pagination DTO.
     *
     * @return The empty pagination DTO
     */
    public PaginationDto<T> empty() {
        return new PaginationDto<>(List.of(), Links.NONE, new PageMetadata(0, 0, 0, 0));
    }

    /**
     * Create a pagination DTO from an entity model.
     *
     * @param entityModel The entity model
     * @return The pagination DTO
     */
    public static <T> PaginationDto<T> fromEntityModel(
            PagedModel<EntityModel<T>> entityModel
    ) {
        return new PaginationDto<>(
                entityModel.getContent().stream().map(EntityModel::getContent).toList(),
                entityModel.getLinks(),
                entityModel.getMetadata()
        );
    }
}
