package com.tokorokoshi.tokoro.modules.place;

import com.tokorokoshi.tokoro.database.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {
    //What is this?
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PlaceService(MongoTemplate mongoTemplate){this.mongoTemplate = mongoTemplate;}

    //CRUD

    public void savePlace(Place place){
        mongoTemplate.save(place);
    }

    public Place getPlaceById(String id){return mongoTemplate.findById(id, Place.class);}

    public List<Place> getAllPlaces(){return mongoTemplate.findAll(Place.class);}

    public void updatePlace(String id, Place place)
    {
        place.setId(id);
        mongoTemplate.save(place);
    }

    public void deleteUser(String id){
        mongoTemplate.remove(getPlaceById(id));
    }
}
