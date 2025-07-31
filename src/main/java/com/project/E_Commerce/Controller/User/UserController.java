package com.project.E_Commerce.Controller.User;

import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Service.User.UserService;
import com.project.E_Commerce.dto.User.*;
import jakarta.validation.Valid;
import jdk.jfr.Percentage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
  private UserService userService;

    @Autowired
    private UserRepo userRepo;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user){
        UserResponse addedUser = userService.createUser(user);
        return  ResponseEntity.ok(addedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
public ResponseEntity<?> getAllUsers()
{

    List<User> users = userRepo.findAll();
    List<UserResponse> resp = users.stream()
            .map(u -> new UserResponse(
                    u.getId(),
                    u.getName(),
                    u.getEmail(),
                    u.getRole().name(),
                    u.getStatus().name(),
                    u.getPhoneNumber()
            ))
            .toList();
    return ResponseEntity.ok(resp);

}

@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable("id") Integer id)
{
    Optional<User> user = userRepo.findById(id);
     User existingUser = user.get();
     UserResponse response=new UserResponse(existingUser.getId(),existingUser.getName(),existingUser.getEmail(),existingUser.getRole().name(),existingUser.getStatus().name(),existingUser.getPhoneNumber());
     return  ResponseEntity.ok(response);

}


@PreAuthorize("hasRole('ADMIN')")
@PutMapping("/deactivate/{id}")
public ResponseEntity<?> deactivateUser(@PathVariable("id") Integer id)
{
   String message =  userService.deactivateUser(id);
   return  ResponseEntity.ok(message);
}


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable Integer id) {
        String response = userService.activateUser(id);
        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasAnyRole('USER','SELLER','DELIVERY')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateByUser( @PathVariable("id") Integer id, @RequestBody @Valid UserUpdateRequest user) {
     String message =  userService.updateUserProfile(id,user);
    return ResponseEntity.ok(message);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUserByAdmin(@RequestBody @Valid UserAdminUpdateRequest user) {
        User updatedUser = userService.updateUserByAdmin(user);
        return ResponseEntity.ok(updatedUser);
    }


    @PreAuthorize("hasAnyRole('USER','SELLER','DELIVERY')")
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable("id") int userId,
                                            @RequestBody @Valid ChangePasswordRequest request) {
        String message = userService.changePassword(userId, request);
        return ResponseEntity.ok(message);
    }

}
