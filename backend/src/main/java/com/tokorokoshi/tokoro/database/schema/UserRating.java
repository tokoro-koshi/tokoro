package com.tokorokoshi.tokoro.database.schema;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userRating")
public class UserRating {
    @Id
    private ObjectId id;

    private ObjectId userId;

    private ObjectId establishmentId;

    private int value;

    public UserRating(){
        this.value = 0;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(ObjectId establishmentId) {
        this.establishmentId = establishmentId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
