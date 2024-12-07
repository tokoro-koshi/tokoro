package com.tokorokoshi.tokoro.modules.user;


import com.tokorokoshi.tokoro.modules.user.dto.CreateUpdateUserDto;
import com.tokorokoshi.tokoro.modules.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> createUser(
            @RequestBody CreateUpdateUserDto userCreateDto
    ){
        return ResponseEntity.ok(this.userService.saveUser(userCreateDto));
    }

    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> getUser(@PathVariable String id){
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @GetMapping(value = {"", "/"},
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @PutMapping(
            value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody CreateUpdateUserDto updateUserDto
    ){
        UserDto updatedUser = this.userService.updateUser(id, updateUserDto);
        if(updatedUser == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(
            value = "/{id}"
    )
    public void deleteUser(@PathVariable String id){
        this.userService.deleteUser(id);
    }

}
