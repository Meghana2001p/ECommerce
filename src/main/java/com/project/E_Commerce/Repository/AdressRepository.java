package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Address;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdressRepository extends JpaRepository<Address,Integer> {
    //create is done by save
    //find by id to findAll

    @Select("select a from Address a where a.users.userId=:userId")
    List<Address> findByUserId(@Param("userId")Integer userID);


    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.type = :type, a.street = :street, a.city = :city, " +
            "a.zipCode = :zipCode, a.user.userId = :userId WHERE a.id = :id")
    int updateAddress(
            @Param("type") Address.AddressType type,
            @Param("street") String street,
            @Param("city") String city,
            @Param("zipCode") String zipCode,
            @Param("userId") Integer userId,
            @Param("id") Integer id
    );


    //delete
    @Modifying
    @Transactional
    @Query("DELETE FROM Address a WHERE a.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);

}
