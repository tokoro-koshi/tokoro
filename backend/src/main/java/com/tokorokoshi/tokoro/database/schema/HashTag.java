package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hashTag")
public class HashTag {
    @Id
    @NonNull
    private ObjectId id;

    @NonNull
    private String name;

    public HashTag() {
        this.name = "";
    }

    @NonNull
    public ObjectId getId() {
        return id;
    }

    public void setId(@NonNull ObjectId id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
