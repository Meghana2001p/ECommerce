package com.project.E_Commerce.Controller.User;

import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Service.User.UserService;
import com.project.E_Commerce.dto.User.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


//User,UserFavourite,Wishlist,UserEmailPreference,SearchHistory
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
  private UserService userService;

    @Autowired
    private UserRepo userRepo;

//user
    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user){

        UserResponse addedUser = userService.createUser(user);
        return  ResponseEntity.ok(addedUser);
    }

    //user
  @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody  @Valid
                                         LoginRequest req)
  {
      User user = userService.loginUser(req.getEmail(), req.getPassword());
      UserResponse resp = new UserResponse(
              user.getName(),
              user.getRole().name(),
              user.getEmail(),
              user.getStatus().name(),
              user.getPhoneNumber()
      );

      return ResponseEntity.ok(resp);
  }

  //admin
@GetMapping("/")
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

//admin
@GetMapping("/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable("id") Integer id)
{
    Optional<User> user = userRepo.findById(id);
     User existingUser = user.get();
     UserResponse response=new UserResponse(existingUser.getId(),existingUser.getName(),existingUser.getEmail(),existingUser.getRole().name(),existingUser.getStatus().name(),existingUser.getPhoneNumber());
     return  ResponseEntity.ok(response);

}

//admin
@PutMapping("/deactivate/{id}")
public ResponseEntity<?> deactivateUser(@PathVariable("id") Integer id)
{

   String message =  userService.deactivateUser(id);
   return  ResponseEntity.ok(message);

}

//user
@PutMapping("/update-profile/{id}")
public ResponseEntity<?> updateByUser( @PathVariable("id") Integer id, @RequestBody @Valid UserUpdateRequest user) {
  String message =  userService.updateUserProfile(id,user);
    return ResponseEntity.ok(message);
}

//admin
    @PutMapping("/admin/update-user")
    public ResponseEntity<?> updateUserByAdmin(@RequestBody @Valid User user) {
        User updatedUser = userService.updateUserByAdmin(user);
        return ResponseEntity.ok(updatedUser);
    }


//user
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable("id") int userId,
                                            @RequestBody @Valid ChangePasswordRequest request) {
        String message = userService.changePassword(userId, request);
        return ResponseEntity.ok(message);
    }







}
