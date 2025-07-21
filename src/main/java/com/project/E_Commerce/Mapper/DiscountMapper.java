package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Discount;
import com.project.E_Commerce.dto.DiscountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface DiscountMapper {

@Mapping(target="isActive",constant="true")
@Mapping(target = "id", ignore = true)
    Discount toEntity(DiscountRequest dto);

}
