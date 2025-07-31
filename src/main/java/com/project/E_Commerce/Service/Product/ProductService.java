package com.project.E_Commerce.Service.Product;

import com.project.E_Commerce.Entity.Product.*;
import com.project.E_Commerce.dto.Product.*;
import org.springframework.data.domain.Pageable;

import java.util.List;


//Product, ProductImage, ProductAttribue, ProductAttributeValue, Brand, Category, RelatedProduct

public interface ProductService {

    //Product
    Product addProduct(ProductRequest product);

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
    String addAttributeValue(ProductAttributeAssignmentRequest value);
    // void updateAttributeValue(Integer id, ProductAttributeValue updatedValue);

    void updateAttributeValue(ProductAttributeAssignmentRequest request);

    ProductAttributeValueResponse getAttributeValueById(Integer id);

    String deleteAttributeValue(Integer id);

    //Brand
    Brand createBrand(BrandRequest brand);

    Brand updateBrand(Integer brandId, BrandRequest brand);

    Brand getBrandById(Integer brandId);

    List<Brand> getAllBrands();

    void deleteBrand(Integer brandId);


    //Category
    Category createCategory(CategoryRequest category);

    Category updateCategory(Integer categoryId, Category category);

    Category getCategoryById(Integer categoryId);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Integer categoryId);


    //RelatedProduct

    RelatedProduct createRelatedProduct(RelatedProduct relatedProduct);

    RelatedProduct updateRelatedProduct(Integer id, RelatedProduct updated);

    RelatedProduct getRelatedProductById(Integer id);

    List<RelatedProduct> getAllRelatedProducts();

    void deleteRelatedProduct(Integer id);


    //list of all the products and the specific product details in that too

    List<ProductList> getAllAvailableProducts(Pageable pageable);

    ProductDetail getProductDetailById(Integer productId);

    List<ProductList> getAllProductsAfterLogin(int userId, int page);

    ProductDetail getProductByIdAndUserId(int userId, int page, int productId);

//    List<ProductList> getAllAvailableProducts(Pageable pageable);
//
//    ProductDetail getProductDetailById(Integer productId);
//
//    List<ProductList> getAllProductsAfterLogin(int userId, int page);
//
//   ProductList getSpecificProductDetailsByIdandUserId(int userId, int page,int productId);
}