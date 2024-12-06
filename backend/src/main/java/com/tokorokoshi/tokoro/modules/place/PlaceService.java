package com.tokorokoshi.tokoro.modules.place;

import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.place.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.place.dto.PlaceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {
    //What is this?
    private final MongoTemplate mongoTemplate;
    private final PlaceMapper placeMapper;

    @Autowired
    public PlaceService(MongoTemplate mongoTemplate,
                        PlaceMapper placeMapper){
        this.mongoTemplate = mongoTemplate;
        this.placeMapper = placeMapper;
    }

    //CRUD

    public PlaceDto savePlace(CreateUpdatePlaceDto place){
        return placeMapper.toPlaceDto(mongoTemplate.save(placeMapper.toPlaceSchema(place)));
    }

    public PlaceDto getPlaceById(String id){return placeMapper.toPlaceDto(mongoTemplate.findById(id, Place.class));}

    public List<PlaceDto> getAllPlaces(){return placeMapper.toPlaceDto(mongoTemplate.findAll(Place.class));}

    public PlaceDto updatePlace(String id, CreateUpdatePlaceDto place)
    {
        Place placeSchema = placeMapper.toPlaceSchema(place);
        placeSchema.setId(id);
        return placeMapper.toPlaceDto(mongoTemplate.save(placeSchema));
    }

    public void deletePlace(String id){
        mongoTemplate.remove(placeMapper.toPlaceSchema(getPlaceById(id)));
    }
}