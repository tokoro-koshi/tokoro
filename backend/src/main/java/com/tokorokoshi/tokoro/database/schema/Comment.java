package com.tokorokoshi.tokoro.database.schema;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comment {
    @Id
    private ObjectId user_Id;

    @NotNull
    private String value;
}
