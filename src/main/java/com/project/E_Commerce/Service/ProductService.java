package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.*;
import java.util.List;


//Product, ProductImage, ProductAttribue, ProductAttributeValue, Brand, Category, RelatedProduct

public interface ProductService {

    //Product
    Product addProduct(Product product);
    Product getProductById(Integer id);
    Product updateProduct(Integer id, Product updatedProduct);
    String deleteProduct(Integer id);

    //Product Image
    String addProductImage(ProductImage image);
    List<ProductImage> getImagesByProductId(int productId);
    String deleteProductImage(int imageId);

//    //Product Attribute

    ProductAttribute addAttribute(ProductAttribute attribute);
    ProductAttribute updateAttribute(int attributeId, ProductAttribute updated);
    ProductAttribute getAttributeById(int attributeId);
    String deleteAttribute(int attributeId);
    //Product Attribute Value
    ProductAttributeValue addAttributeValue(ProductAttributeValue value);
    ProductAttributeValue updateAttributeValue(Integer id, ProductAttributeValue updatedValue);
    ProductAttributeValue getAttributeValueById(Integer id);
    String deleteAttributeValue(Integer id);



//
//    ProductAttribute addProductAttribute(ProductAttribute attribute);
//    List<ProductAttribute> getAllAttributes();
//    ProductAttribute getAttributeById(int id);
//
//    //Product Attribute Value
//    ProductAttributeValue addProductAttributeValue(ProductAttributeValue value);
//    List<ProductAttributeValue> getAttributeValuesByProductId(int productId);
//    String deleteAttributeValue(int id);
//    ProductAttributeValue updateProductAttributeValue(ProductAttributeValue value);
//
//    //Brand
//    Brand addBrand(Brand brand);
//    List<Brand> getAllBrands();
//    Brand getBrandById(int id);
//    Brand updateBrand(Brand brand);
//    String deleteBrand(int id);
//
//    //Category
//    Category addCategory(Category category);
//    List<Category> getAllCategories();
//    Category getCategoryById(int id);
//    Category updateCategory(Category category);
//    boolean deleteCategory(int id);
//
//    //Related Product
//    RelatedProduct addRelatedProduct(RelatedProduct relatedProduct);
//    List<RelatedProduct> getRelatedProductsByProductId(int productId);
//    String deleteRelatedProduct(int relationId);
//    List<RelatedProduct> getRelatedProductsByType(int productId, RelatedProduct.RelationshipType type);
//    String hardDeleteRelation(int productId, int relatedProductId, RelatedProduct.RelationshipType type);

}
