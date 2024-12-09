package com.tokorokoshi.tokoro.modules.users.dto;




public class CreateUpdateUserDto {
    private String username;
    private String email;
    private String password;
    private UserPreferencesDto userPreferencesDto;
    private UserFavoritesDto userFavoritesDto;

    public CreateUpdateUserDto(){
        this.username = "";
        this.email = "";
        this.password = "";
        this.userFavoritesDto = new UserFavoritesDto();
        this.userPreferencesDto = new UserPreferencesDto();
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

    public UserPreferencesDto getUserPreferencesDto() {
        return userPreferencesDto;
    }

    public void setUserPreferencesDto(UserPreferencesDto userPreferencesDto) {
        this.userPreferencesDto = userPreferencesDto;
    }

    public UserFavoritesDto getUserFavoritesDto() {
        return userFavoritesDto;
    }

    public void setUserFavoritesDto(UserFavoritesDto userFavoritesDto) {
        this.userFavoritesDto = userFavoritesDto;
    }
}
