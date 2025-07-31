package com.project.E_Commerce.Repository.User;

import com.project.E_Commerce.Entity.User.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//**
//        * Repository interface for performing CRUD operations and custom queries on User entities.
// *
//         * Includes:
//        * - Standard CRUD methods (inherited from JpaRepository)
// * - Login by email and password
// * - Lookup by email
// * - Check if a user is active
// * - Deactivate a user by ID
// */
@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    //  Login user based on email and password
    @Query("SELECT u FROM User u WHERE u.email = :email ")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    //  Check if user exists by email
    @Query("SELECT COUNT(*) > 0 FROM User WHERE email = :email")
    Boolean existsByEmail(@Param("email")String email);

    //  Get user by email
    @Query("select u from User u where u.email=:email")
    Optional<User> findByEmail(@Param("email")String email);

    //  Check if user is active
    @Query("SELECT CASE WHEN u.isActive = true THEN true ELSE false END FROM User u WHERE u.id = :userId")
    boolean isUserActive(Integer userId);



    //  Deactivate user by setting isActive to false
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :userId")
    int deactivateUser(Integer userId);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isActive = true WHERE u.id = :id")
    int activateUser(@Param("id") Integer id);
    //UPDATE

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name, u.email = :email, u.password = :password, u.phoneNumber = :phoneNumber, " +
            "u.role = :role, u.status = :status WHERE u.id = :userId")
    int updateUserFields(@Param("userId") Integer userId,
                         @Param("name") String name,
                         @Param("email") String email,
                         @Param("password") String password,
                         @Param("phoneNumber") String phoneNumber,
                         @Param("role") User.Role role,
                         @Param("status") User.Status status);



    //DELETE
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :userId")
    int deleteByUserId(@Param("userId") Integer userId);




}
