package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.dto.CategoryRequest;
import com.project.E_Commerce.dto.ProductDetailDTO;
import com.project.E_Commerce.dto.ProductList;
import org.springframework.data.domain.Pageable;

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

   //Product Attribute

    ProductAttribute addAttribute(ProductAttribute attribute);
    ProductAttribute updateAttribute(int attributeId, ProductAttribute updated);
    ProductAttribute getAttributeById(int attributeId);
    String deleteAttribute(int attributeId);


    //Product Attribute Value
    ProductAttributeValue addAttributeValue(ProductAttributeValue value);
    ProductAttributeValue updateAttributeValue(Integer id, ProductAttributeValue updatedValue);
    ProductAttributeValue getAttributeValueById(Integer id);
    String deleteAttributeValue(Integer id);

   //Brand
   Brand createBrand(Brand brand);
    Brand updateBrand(Integer brandId, Brand brand);
    Brand getBrandById(Integer brandId);
    List<Brand> getAllBrands();
    void deleteBrand(Integer brandId);


    //Category
    Category createCategory(CategoryRequest category);
    Category updateCategory(Integer categoryId, Category category);
    Category getCategoryById(Integer categoryId);
    List<Category> getAllCategories();
    void deleteCategory(Integer categoryId);


    //RelatedProduct

    RelatedProduct createRelatedProduct(RelatedProduct relatedProduct);
    RelatedProduct updateRelatedProduct(Integer id, RelatedProduct updated);
    RelatedProduct getRelatedProductById(Integer id);
    List<RelatedProduct> getAllRelatedProducts();
    void deleteRelatedProduct(Integer id);

 //list of all the products and the specific product details in that too

    List<ProductList> getAllAvailableProducts(Integer userId, Pageable pageable);

    ProductDetailDTO getProductDetailById(Integer productId, Integer userId);





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
