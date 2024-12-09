package com.tokorokoshi.tokoro.modules.users;


import com.tokorokoshi.tokoro.modules.users.dto.CreateUpdateUserDto;
import com.tokorokoshi.tokoro.modules.users.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

    @PostMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> createUser(
            @RequestBody CreateUpdateUserDto userCreateDto
    ){
        return ResponseEntity.ok(this.usersService.saveUser(userCreateDto));
    }

    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> getUser(@PathVariable String id){
        return ResponseEntity.ok(this.usersService.getUserById(id));
    }

    @GetMapping(value = {"", "/"},
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(this.usersService.getAllUsers());
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
        UserDto updatedUser = this.usersService.updateUser(id, updateUserDto);
        if(updatedUser == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(
            value = "/{id}"
    )
    public void deleteUser(@PathVariable String id){
        this.usersService.deleteUser(id);
    }

}
