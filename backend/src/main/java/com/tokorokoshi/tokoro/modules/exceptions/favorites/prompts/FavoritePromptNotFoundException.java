package com.tokorokoshi.tokoro.modules.exceptions.favorites.prompts;

public class FavoritePromptNotFoundException extends RuntimeException {
    public FavoritePromptNotFoundException(String message) {
        super(message);
    }
}
