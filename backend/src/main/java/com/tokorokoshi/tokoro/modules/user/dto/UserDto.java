package com.tokorokoshi.tokoro.modules.user.dto;

public class UserDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private UserPreferencesDto userPreferencesDto;
    private UserFavoritesDto userFavoritesDto;

    public UserDto(){
        this.id = "";
        this.username = "";
        this.email = "";
        this.password = "";
        this.userPreferencesDto = new UserPreferencesDto();
        this.userFavoritesDto = new UserFavoritesDto();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserFavoritesDto getUserFavoritesDto() {
        return userFavoritesDto;
    }

    public void setUserFavoritesDto(UserFavoritesDto userFavoritesDto) {
        this.userFavoritesDto = userFavoritesDto;
    }

    public UserPreferencesDto getUserPreferencesDto() {
        return userPreferencesDto;
    }

    public void setUserPreferencesDto(UserPreferencesDto userPreferencesDto) {
        this.userPreferencesDto = userPreferencesDto;
    }
}
