package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Address;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdressRepository extends JpaRepository<Address,Integer> {
    //create is done by save
    //find by id to findAll

    @Query("select a from Address a where a.user.id=:userID")
    List<Address> findByUserId(@Param("userId")Integer userID);


    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.type = :type, a.street = :street, a.city = :city, " +
            "a.zipCode = :zipCode, a.user.id = :userId WHERE a.id = :id")
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
    @Query("DELETE FROM Address a WHERE a.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);

}
