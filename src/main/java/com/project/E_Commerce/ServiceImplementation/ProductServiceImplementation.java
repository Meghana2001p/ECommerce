package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Mapper.*;
import com.project.E_Commerce.Service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//Product, ProductImage, ProductAttribue, ProductAttributeValue, Brand, Category, RelatedProduct
@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Autowired
    private ProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RelatedProductMapper relatedProductMapper;

    @Override
    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        try {
            int res = productMapper.createProduct(product);
            if (res <= 0) {
                throw new DataCreationException("Product could not be added");
            }
            return product;
        } catch (DataAccessException e) {
            throw new DataCreationException("Failed to create product: " + e.getMessage());
        }
    }

    @Override
    public Product getProductById(int id) {
        try {
            Product product = productMapper.getProductById(id);
            if (product == null) {
                throw new NotFoundException("Product with ID " + id + " not found");
            }
            return product;
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve product with ID " + id);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productMapper.getAllProducts();
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve all products");
        }
    }

    @Override
    public Product updateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID must not be null for update");
        }

        try {
            Product existing = productMapper.getProductById(product.getId());
            if (existing == null) {
                throw new NotFoundException("Product with ID " + product.getId() + " does not exist.");
            }

            int res = productMapper.updateProduct(product);
            if (res <= 0) {
                throw new DataUpdateException("Product update failed");
            }

            return productMapper.getProductById(product.getId());
        } catch (DataAccessException e) {
            throw new DataUpdateException("Failed to update product: " + e.getMessage());
        }
    }

    @Override
    public String deleteProduct(int id) {
        try {
            int res = productMapper.deleteProductById(id);
            if (res <= 0) {
                throw new NotFoundException("Product with ID " + id + " was not deleted or does not exist");
            }
            return "Product with ID " + id + " was deleted successfully";
        } catch (DataAccessException e) {
            throw new DataDeletionException("Failed to delete product with ID " + id);
        }
    }

    //ProductImage

    @Override
    public String addProductImage(ProductImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Product image cannot be null");
        }
        try {
            int res = productImageMapper.insertProductImage(image);
            if (res <= 0) {
                throw new DataCreationException("The product image cannot be added");
            }
            return "Image added successfully";
        } catch (DataAccessException e) {
            throw new DataCreationException("Failed to add product image: " + e.getMessage());
        }
    }

    @Override
    public List<ProductImage> getImagesByProductId(int productId) {
        try {
            return productImageMapper.getImagesByProductId(productId);
        } catch (DataAccessException e) {
            //logger.log(ex)
            throw new DataRetrievalException("Failed to retrieve images for product ID " + productId);
        }
    }

    @Override
    public String deleteProductImage(int imageId) {
        try {
            int res = productImageMapper.deleteProductImage(imageId);
            if (res <= 0) {
                throw new NotFoundException("The product image cannot be deleted or does not exist");
            }
            return "Image deleted successfully";
        } catch (DataAccessException e) {
            throw new DataDeletionException("Failed to delete product image with ID " + imageId);
        }
    }

    //ProductAttribute

    @Override
    public ProductAttribute addProductAttribute(ProductAttribute attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Product attribute cannot be null");
        }
        try {
            int res = productAttributeMapper.createAttribute(attribute);
            if (res <= 0) {
                throw new DataCreationException("The product attribute could not be created");
            }
            return attribute;
        } catch (DataAccessException e) {
            throw new DataCreationException("Failed to create product attribute: " + e.getMessage());
        }
    }

    @Override
    public List<ProductAttribute> getAllAttributes() {
        try {
            return productAttributeMapper.getAllAttributes();
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve all product attributes");
        }
    }

    @Override
    public ProductAttribute getAttributeById(int id) {
        try {
            ProductAttribute attribute = productAttributeMapper.getAttributeById(id);
            if (attribute == null) {
                throw new NotFoundException("Attribute with ID " + id + " not found");
            }
            return attribute;
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve attribute with ID " + id);
        }
    }

    @Override
    public ProductAttributeValue addProductAttributeValue(ProductAttributeValue value) {
        if (value == null) {
            throw new IllegalArgumentException("Attribute value cannot be null");
        }
        try {
            int res = productAttributeValueMapper.createAttributeValue(value);
            if (res <= 0) {
                throw new DataCreationException("The product attribute value could not be created");
            }
            return value;
        } catch (DataAccessException e) {
            throw new DataCreationException("Failed to create attribute value: " + e.getMessage());
        }
    }

    @Override
    public List<ProductAttributeValue> getAttributeValuesByProductId(int productId) {
        try {
            return productAttributeValueMapper.getAttributeValuesByProduct(productId);
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve attribute values for product ID " + productId);
        }
    }

    @Override
    public String deleteAttributeValue(int id) {
        try {
            int res = productAttributeValueMapper.deleteAttributeValue(id);
            if (res <= 0) {
                throw new NotFoundException("The product attribute value could not be deleted or does not exist");
            }
            return "Attribute value was deleted successfully";
        } catch (DataAccessException e) {
            throw new DataDeletionException("Failed to delete attribute value with ID " + id);
        }
    }

    @Override
    public ProductAttributeValue updateProductAttributeValue(ProductAttributeValue value) {
        if (value == null) {
            throw new IllegalArgumentException("Attribute value cannot be null");
        }
        if (value.getId() == null) {
            throw new IllegalArgumentException("Attribute value ID must not be null for update");
        }

        try {
            ProductAttributeValue existing = productAttributeValueMapper.getAttributeValueById(value.getId());
            if (existing == null) {
                throw new NotFoundException("Attribute value with ID " + value.getId() + " does not exist");
            }

            int res = productAttributeValueMapper.updateAttributeValue(value);
            if (res <= 0) {
                throw new DataUpdateException("Attribute value could not be updated");
            }

            return value;
        } catch (DataAccessException e) {
            throw new DataUpdateException("Failed to update attribute value: " + e.getMessage());
        }
    }

    //Brand

    @Override
    public Brand addBrand(Brand brand) {
        if (brand == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        try {
            if (brandMapper.getBrandByBrandName(brand.getBrandName())) {
                throw new DuplicateResourceException("Brand with name '" + brand.getBrandName() + "' already exists");
            }

            int res = brandMapper.createBrand(brand);
            if (res <= 0) {
                throw new DataCreationException("Brand could not be created");
            }
            return brand;
        } catch (DataAccessException e) {
            throw new DataCreationException("Failed to create brand: " + e.getMessage());
        }
    }

    @Override
    public List<Brand> getAllBrands() {
        try {
            return brandMapper.getAllBrands();
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve all brands");
        }
    }

    @Override
    public Brand getBrandById(int id) {
        try {
            Brand brand = brandMapper.getBrandById(id);
            if (brand == null) {
                throw new NotFoundException("Brand with ID " + id + " not found");
            }
            return brand;
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve brand with ID " + id);
        }
    }

    @Override
    public Brand updateBrand(Brand brand) {
        if (brand == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        try {
            Brand existingBrand = brandMapper.getBrandById(brand.getBrandId());
            if (existingBrand == null) {
                throw new NotFoundException("Brand does not exist with ID: " + brand.getBrandId());
            }

            int res = brandMapper.updateBrand(brand);
            if (res <= 0) {
                throw new DataUpdateException("Brand update failed");
            }

            return brand;
        } catch (DataAccessException e) {
            throw new DataUpdateException("Failed to update brand: " + e.getMessage());
        }
    }

    @Override
    public String deleteBrand(int id) {
        try {
            Brand existingBrand = brandMapper.getBrandById(id);
            if (existingBrand == null) {
                throw new NotFoundException("Brand does not exist with ID: " + id);
            }

            int res = brandMapper.deleteBrand(id);
            if (res <= 0) {
                throw new DataDeletionException("Brand delete failed");
            }
            return "Brand was deleted successfully";
        } catch (DataAccessException e) {
            throw new DataDeletionException("Failed to delete brand with ID " + id);
        }
    }

    @Override
    public Category addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        try {
            Category existingCategory = categoryMapper.getCategoryByName(category.getCategoryName());
            if (existingCategory != null) {
                throw new DuplicateResourceException("Category with name '" + category.getCategoryName() + "' already exists");
            }

            int res = categoryMapper.createCategory(category);
            if (res <= 0) {
                throw new DataCreationException("Category could not be added");
            }
            return category;
        } catch (DataAccessException e) {
            throw new DataCreationException("Failed to create category: " + e.getMessage());
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return categoryMapper.getAllCategories();
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve all categories");
        }
    }

    @Override
    public Category getCategoryById(int id) {
        try {
            Category category = categoryMapper.getCategoryById(id);
            if (category == null) {
                throw new NotFoundException("Category with ID " + id + " not found");
            }
            return category;
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve category with ID " + id);
        }
    }

    @Override
    public Category updateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (category.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID must not be null for update");
        }
        try {
            Category existingCategory = categoryMapper.getCategoryById(category.getCategoryId());
            if (existingCategory == null) {
                throw new NotFoundException("Category does not exist with ID: " + category.getCategoryId());
            }

            int res = categoryMapper.updateCategory(category);
            if (res <= 0) {
                throw new DataUpdateException("Category could not be updated");
            }
            return category;
        } catch (DataAccessException e) {
            throw new DataUpdateException("Failed to update category: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteCategory(int id)
    {
        if (id <= 0) {
            throw new IllegalArgumentException("Category ID must be a positive number");
        }

        try {
            Category existingCategory = categoryMapper.getCategoryById(id);
            if (existingCategory == null)
            {
                throw new NotFoundException("Category does not exist with ID");
            }
            int affectedRows = categoryMapper.deleteCategory(id);

            if (affectedRows == 0) {
                throw new DataUpdateException("No category was deleted - possible concurrency issue");
            }

            return true;
        } catch (DataAccessException e) {
            throw new DataUpdateException("Failed to update category: " + e.getMessage());
        }
   }

    //related product
    @Override
    public RelatedProduct addRelatedProduct(RelatedProduct relatedProduct) {
        if (relatedProduct == null) {
            throw new IllegalArgumentException("Related product cannot be null");
        }

        if (relatedProduct.getProductId().equals(relatedProduct.getRelatedProductId())) {
            throw new IllegalArgumentException("Product cannot be related to itself");
        }

        try {
            Product mainProduct = productMapper.getProductById(relatedProduct.getProductId());
            if (mainProduct == null) {
                throw new NotFoundException("Main product with ID " + relatedProduct.getProductId() + " does not exist");
            }

            Product relatedProd = productMapper.getProductById(relatedProduct.getRelatedProductId());
            if (relatedProd == null) {
                throw new NotFoundException("Related product with ID " + relatedProduct.getRelatedProductId() + " does not exist");
            }

            relatedProduct.setCreatedAt(LocalDateTime.now());
            relatedProduct.setIsActive(true);
            relatedProductMapper.insertRelatedProduct(relatedProduct);

            return relatedProduct;
        } catch (DuplicateKeyException e) {
            throw new DuplicateRelationException("This relationship already exists");
        } catch (DataAccessException e) {
            throw new DataCreationException("Failed to create relationship: " + e.getMessage());
        }
    }

    @Override
    public List<RelatedProduct> getRelatedProductsByProductId(int productId) {
        try {
            return relatedProductMapper.getActiveRelatedProducts(productId);
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve related products");
        }
    }

    @Override
    @Transactional
    public String deleteRelatedProduct(int relationId) {
        try {
            int rowsAffected = relatedProductMapper.deactivateRelation(relationId);
            if (rowsAffected == 0) {
                throw new NotFoundException("Relation not found with ID: " + relationId);
            }
            return "Successfully deactivated relation with ID: " + relationId;
        } catch (DataAccessException e) {
            throw new DataDeletionException("Failed to delete relation: " + e.getMessage());
        }
    }

    @Override
    public List<RelatedProduct> getRelatedProductsByType(int productId, RelatedProduct.RelationshipType type) {
        try {
            return relatedProductMapper.getRelatedProductsByType(productId, type.toString());
        } catch (DataAccessException e) {
            throw new DataRetrievalException("Failed to retrieve related products by type");
        }
    }

    @Override
    @Transactional
    public String hardDeleteRelation(int productId, int relatedProductId, RelatedProduct.RelationshipType type) {
        try {
            int rowsDeleted = relatedProductMapper.deleteRelation(
                    productId,
                    relatedProductId,
                    type.toString()
            );

            if (rowsDeleted == 0) {
                throw new NotFoundException("No such relation found");
            }
            return "Successfully deleted relation";
        } catch (DataAccessException e) {
            throw new DataDeletionException("Failed to hard delete relation: " + e.getMessage());
        }
    }
}