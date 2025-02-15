package com.tokorokoshi.tokoro.modules.search;

import com.tokorokoshi.tokoro.dto.Response;
import com.tokorokoshi.tokoro.modules.places.PlacesService;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.tags.TagsService;
import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;
import com.tokorokoshi.tokoro.modules.tags.dto.TagsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final TagsService tagsService;
    private final PlacesService placesService;

    @Autowired
    public SearchService(TagsService tagsService,
                         PlacesService placesService) {
        this.tagsService = tagsService;
        this.placesService = placesService;
    }

    public Response<List<PlaceDto>> search(String query) {
        Response<TagsDto> tagsResponse = tagsService.generateTags(query);
        if (tagsResponse.isRefusal()) {
            return Response.<List<PlaceDto>>builder()
                    .refusal(tagsResponse.getRefusal())
                    .refusalStatus(tagsResponse.getRefusalStatus())
                    .build();
        }
        List<TagDto> tags = List.of(tagsResponse.getContent().tags());
        return Response.<List<PlaceDto>>builder()
                .content(placesService.getPlacesByTags(tags))
                .build();
    }
}
