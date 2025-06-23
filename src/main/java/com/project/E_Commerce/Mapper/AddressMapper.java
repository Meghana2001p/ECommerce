package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

//Admin,Seller for some factors
@Mapper
public interface AddressMapper {

    // Add Address
    @Insert("INSERT INTO address (type, street, city, zip_code, user_id) " +
            "VALUES (#{type}, #{street}, #{city}, #{zipCode}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createAddress(Address address);

    // Update Address
    @Update("UPDATE address SET type = #{type}, street = #{street}, city = #{city}, " +
            "zip_code = #{zipCode}, user_id = #{userId} WHERE id = #{id}")
    int updateAddress(Address address);

    // Delete Address
    @Delete("DELETE FROM address WHERE id = #{id}")
    int deleteAddress(@Param("id") int id);

    // Get Address By ID
    @Select("SELECT * FROM address WHERE id = #{id}")
    Address getAddressById(@Param("id") int id);

    // Get All Addresses for a User
    @Select("SELECT * FROM address WHERE user_id = #{userId}")
    List<Address> getAddressesByUserId(@Param("userId") int userId);
}
