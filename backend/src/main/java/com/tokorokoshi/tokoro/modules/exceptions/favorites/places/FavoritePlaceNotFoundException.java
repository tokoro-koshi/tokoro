package com.tokorokoshi.tokoro.modules.exceptions.favorites.places;

public class FavoritePlaceNotFoundException extends RuntimeException {
    public FavoritePlaceNotFoundException(String message) {
        super(message);
    }
}
