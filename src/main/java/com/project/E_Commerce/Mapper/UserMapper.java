package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Mapper
public interface UserMapper {
    //add user
    //delete user
    //update user
    //read users
    @Insert("INSERT INTO users (name, email, password, role, status) " +
            "VALUES (#{name}, #{email}, #{password}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int createUser(User user);

    @Update("UPDATE users SET name = #{name}, email = #{email}, password = #{password}, " +
            "role = #{role}, status = #{status} WHERE user_id = #{userId}")
    int updateUser(User user);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
   int deleteUser(@Param("userId")int  userId);

    @Select("SELECT * FROM users WHERE user_id = #{id}")
   User getUserByID(@Param("id") Integer id);

    @Select("SELECT * FROM users")
    List<User> getAllUsers();


    //login user based on the email and the password
    @Select("select * from users where email = #{email} and password= #{password} ")
    User userLogin(String email,String password);


    @Select("select * from users where email = #{email}")
    boolean getUserByEmail(@Param("email") String email);


    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE user_id = #{userId} AND is_active = true)")
    boolean isUserActive(@Param("userId") int userId);

    // Deactivate user by ID
    @Update("UPDATE users SET is_active = FALSE WHERE user_id = #{userId}")
    int deactivateUser(@Param("userId") int userId);

}
