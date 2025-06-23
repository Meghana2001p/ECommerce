package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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
   int deleteUser(User user);

    @Select("SELECT * FROM users WHERE user_id = #{id}")
   User getUserByID(@Param("id") Integer id);

    @Select("SELECT * FROM users")
    List<User> getAllUsers();

}
