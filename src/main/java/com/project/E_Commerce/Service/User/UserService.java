package com.project.E_Commerce.Service.User;

import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.dto.User.*;

import java.util.List;

public interface UserService {

    UserResponse createUser(User user);

    String deactivateUser(Integer id);

    String activateUser(Integer id);

    String updateUserProfile(int userId, UserUpdateRequest dto);

    User updateUserByAdmin(UserAdminUpdateRequest user);

    void deleteUser(int userId);

    String changePassword(int userId, ChangePasswordRequest request);

}