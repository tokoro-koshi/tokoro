package com.tokorokoshi.tokoro.modules.places.dto;

public class HashTagDto {
    private String id;
    private String name;

    public HashTagDto() {
        this.id = "";
        this.name = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
