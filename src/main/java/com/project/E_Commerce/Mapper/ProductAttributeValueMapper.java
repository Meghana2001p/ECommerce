package com.project.E_Commerce.Mapper;


import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Entity.ProductAttribute;
import com.project.E_Commerce.Entity.ProductAttributeValue;
import com.project.E_Commerce.dto.ProductAttributeAssignmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductAttributeValueMapper {

    // Converts DTO input + entity objects into the final entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "attribute", source = "attribute")
    ProductAttributeValue toEntity(ProductAttributeAssignmentRequest.AttributeValuePair dto, Product product, ProductAttribute attribute);
}


//
// * Converts from [InputDTO] and additional entities ([Entity1], [Entity2], etc.)
// * into [TargetEntity].
// */
//@Mapper(componentModel = "spring")
//public interface YourEntityMapper {
//
//    @Mapping(target = "id", ignore = true) // Ignore ID if it's auto-generated
//    @Mapping(target = "fieldX", source = "entity1") // Map custom object
//    @Mapping(target = "fieldY", source = "entity2") // Map another object
//    TargetEntity toEntity(
//            InputDTO dto,
//            Entity1 entity1,
//            Entity2 entity2
//    );
//}