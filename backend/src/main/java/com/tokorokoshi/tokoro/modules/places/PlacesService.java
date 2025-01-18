package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacesService {
    private final MongoTemplate mongoTemplate;
    private final PlaceMapper placeMapper;

    @Autowired
    public PlacesService(
        MongoTemplate mongoTemplate,
        PlaceMapper placeMapper
    ) {
        this.mongoTemplate = mongoTemplate;
        this.placeMapper = placeMapper;
    }

    public PlaceDto savePlace(CreateUpdatePlaceDto place) {
        return placeMapper.toPlaceDto(
            mongoTemplate.save(placeMapper.toPlaceSchema(place))
        );
    }

    public PlaceDto getPlaceById(String id) {
        return placeMapper.toPlaceDto(mongoTemplate.findById(id, Place.class));
    }

    public List<PlaceDto> getAllPlaces() {
        return placeMapper.toPlaceDto(mongoTemplate.findAll(Place.class));
    }

    public PlaceDto updatePlace(String id, CreateUpdatePlaceDto place) {
        if (getPlaceById(id) == null) {
            return null;
        }
        Place placeSchema = placeMapper.toPlaceSchema(place);
        return placeMapper.toPlaceDto(mongoTemplate.save(placeSchema.withId(id)));
    }

    public void deletePlace(String id) {
        mongoTemplate.remove(placeMapper.toPlaceSchema(getPlaceById(id)));
    }
}
