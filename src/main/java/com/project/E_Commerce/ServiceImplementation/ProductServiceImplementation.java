package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Mapper.*;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

//Product, ProductImage, ProductAttribue, ProductAttributeValue, Brand, Category, RelatedProduct
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImplementation.class);

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductImageRepo productImageRepo;

    @Autowired
    private ProductAttributeRepo productAttributeRepo;

    @Autowired
   private ProductAttributeValueRepo productAttributeValueRepo;

    @Autowired
    private BrandRepo brandRepository;

@Autowired
private CategoryRepo categoryRepository;

@Autowired
private  RelatedProductRepo relatedProductRepo;

@Autowired
private ProductDiscountRepo productDiscountRepo;

@Autowired
private ReviewRepo reviewRepo;

@Autowired
private CartRepo cartRepo;

@Autowired
private WishlistRepo wishlistRepo;




    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        try {
            int res = productRepo.save(product).getId();
            if (res <= 0) {
                throw new DataCreationException("Product could not be added");
            }
            return product;
        } catch (DataAccessException e) {
            logger.error("Database access error while creating product : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while inserting the product ", e);
            throw e; // Rethrow so global handler can manage it
        }
    }

    @Override
    public Product getProductById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        try {
            return productRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        } catch (DataAccessException e) {
            logger.error("Database access error while fetching product by ID {}: {}", id, e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error while fetching product by ID {}", id, e);
            throw e;
        }    }

    public Product updateProduct(Integer id, Product updatedProduct) {
        if (id == null || id <= 0 || updatedProduct == null) {
            throw new IllegalArgumentException("Invalid product data or ID");
        }

        try {
            Product existing = productRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

            // Update fields
            existing.setName(updatedProduct.getName());
            existing.setDescription(updatedProduct.getDescription());
            existing.setImageAddress(updatedProduct.getImageAddress());
            existing.setPrice(updatedProduct.getPrice());
            existing.setSku(updatedProduct.getSku());
            existing.setIsAvailable(updatedProduct.getIsAvailable());
            existing.setBrand(updatedProduct.getBrand());

            Product saved = productRepo.save(existing);
            if (saved.getId() == null || saved.getId() <= 0) {
                throw new DataCreationException("Product could not be updated");
            }

            return saved;
        } catch (DataAccessException e) {
            logger.error("Database access error while updating product ID {}: {}", id, e.getMessage(), e);
            throw new DataCreationException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error while updating product ID {}", id, e);
            throw e;
        }
    }


    public String deleteProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        try {
            Product existing = productRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
            productRepo.delete(existing);
            return "Product deleted successfully";
        } catch (DataAccessException e) {
            logger.error("Database access error while deleting product ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error while deleting product ID {}", id, e);
            throw e;
        }
    }


    //Product Image
    @Override
    public String addProductImage(ProductImage image) {
        if (image == null || image.getProduct() == null || image.getProduct().getId() == null) {
            throw new IllegalArgumentException("Image or Product ID is required");
        }

        try {
            boolean exists = productImageRepo.existsById(image.getProduct().getId());
            if (!exists) {
                throw new IllegalArgumentException("Product not found with id: " + image.getProduct().getId());
            }

            ProductImage saved = productImageRepo.save(image);
            if (saved.getId() == null || saved.getId() <= 0) {
                throw new DataCreationException("Image could not be saved");
            }

            return "Product image added successfully ";
        } catch (DataAccessException e) {
            logger.error("Database error while saving product image: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error while adding product image", e);
            throw e;
        }
    }

    @Override
    public List<ProductImage> getImagesByProductId(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        try {
            return productImageRepo.findByProductId(productId);
        } catch (DataAccessException e) {
            logger.error("Database error fetching images for product ID {}: {}", productId, e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error fetching product images", e);
            throw e;
        }
    }

    @Override
    public String deleteProductImage(int imageId) {
        if (imageId <= 0) {
            throw new IllegalArgumentException("Invalid image ID");
        }

        try {
            ProductImage image = productImageRepo.findById(imageId)
                    .orElseThrow(() -> new ProductNotFoundException("Image not found "));
            productImageRepo.delete(image);
            return "Product image deleted successfully";
        } catch (DataAccessException e) {
            logger.error("Database error deleting image ID {}: {}", imageId, e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting product image", e);
            throw e;
        }
    }
    //Attributes

    @Override
    public ProductAttribute addAttribute(ProductAttribute attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute cannot be null");
        }

        try {
            ProductAttribute saved = productAttributeRepo.save(attribute);
            if (saved.getId() <= 0) {
                throw new RuntimeException("Attribute could not be saved");
            }
            return saved;
        } catch (DataAccessException e) {
            logger.error("Database error while saving attribute: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            throw e;
        }
    }

    @Override
    public ProductAttribute updateAttribute(int attributeId, ProductAttribute updated) {
        if (attributeId <= 0 || updated == null) {
            throw new IllegalArgumentException("Invalid data for update");
        }

        try {
            ProductAttribute existing = productAttributeRepo.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));

            existing.setName(updated.getName());
            return productAttributeRepo.save(existing);
        } catch (DataAccessException e) {
            logger.error("DB error updating attribute ID {}: {}", attributeId, e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error updating attribute", e);
            throw e;
        }
    }

    @Override
    public ProductAttribute getAttributeById(int attributeId) {
        if (attributeId <= 0) {
            throw new IllegalArgumentException("Invalid attribute ID");
        }

        try {
            return productAttributeRepo.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));
        } catch (DataAccessException e) {
            logger.error("DB error fetching attribute ID {}: {}", attributeId, e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error fetching attribute", e);
            throw e;
        }
    }

    @Override
    public String deleteAttribute(int attributeId) {
        if (attributeId <= 0) {
            throw new IllegalArgumentException("Invalid attribute ID");
        }

        try {
            ProductAttribute existing = productAttributeRepo.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));
            productAttributeRepo.delete(existing);
            return "Attribute deleted successfully";
        } catch (DataAccessException e) {
            logger.error("DB error deleting attribute ID {}: {}", attributeId, e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting attribute", e);
            throw e;
        }
    }

    //ProductAttribute
    @Override
    public ProductAttributeValue addAttributeValue(ProductAttributeValue value) {
        if (value == null || value.getProduct() == null || value.getAttribute() == null) {
            throw new IllegalArgumentException("Product and Attribute must not be null");
        }

        try {
            boolean productExists = productRepo.existsById(value.getProduct().getId());
            boolean attrExists = productAttributeValueRepo.existsById(value.getAttribute().getId());

            if (!productExists) throw new ProductNotFoundException("Product not found");
            if (!attrExists) throw new ProductNotFoundException("Attribute not found");

            return productAttributeValueRepo.save(value);
        } catch (DataAccessException e) {
            logger.error("DB error deleting attribute ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting attribute", e);
            throw e;
        }
    }

    @Override
    public ProductAttributeValue updateAttributeValue(Integer id, ProductAttributeValue updated) {
        if (id == null || updated == null) {
            throw new IllegalArgumentException("Invalid input for update");
        }

        try {
            ProductAttributeValue existing = productAttributeValueRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Attribute value not found"));

            existing.setValue(updated.getValue());
            existing.setProduct(updated.getProduct());
            existing.setAttribute(updated.getAttribute());

            return productAttributeValueRepo.save(existing);
        } catch (DataAccessException e) {
            logger.error("DB error deleting attribute ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting attribute", e);
            throw e;
        }
    }

    @Override
    public ProductAttributeValue getAttributeValueById(Integer id) {
        try {
            return productAttributeValueRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Attribute value not found with ID: " + id));
        }catch (DataAccessException e) {
            logger.error("DB error deleting attribute ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting attribute", e);
            throw e;
        }
    }

    @Override
    public String deleteAttributeValue(Integer id) {
        try {
            ProductAttributeValue val = productAttributeValueRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Attribute value not found with ID: " + id));
            productAttributeValueRepo.delete(val);
            return "Attribute value deleted successfully";
        }
        catch (DataAccessException e) {
            logger.error("DB error deleting attribute ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting attribute", e);
            throw e;
        }
    }



    //Brand

    @Override
    public Brand createBrand(Brand brand) {

        if (brand == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        try {
            if (brandRepository.existsByBrandName(brand.getBrandName())) {
                throw new IllegalArgumentException("Brand with name '" + brand.getBrandName() + "' already exists.");
            }

            return brandRepository.save(brand);
        }
        catch (DataAccessException e) {
            logger.error("DB error creating brand  ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting attribute", e);
            throw e;
        }
    }

    @Override
    public Brand updateBrand(Integer brandId, Brand updatedBrand) {
        if(brandId==null)
        {
            throw  new IllegalArgumentException("Id should not be null");
        }
        try {
            Brand existingBrand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ProductNotFoundException("Brand not found with ID: "));

            existingBrand.setBrandName(updatedBrand.getBrandName());
            existingBrand.setCategories(updatedBrand.getCategories());

            return brandRepository.save(existingBrand);
        }
        catch (DataAccessException e) {
            logger.error("DB error update Brand  ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error updating the brand ", e);
            throw e;
        }
    }

    @Override
    public Brand getBrandById(Integer brandId) {
        if(brandId==null)
        {
            throw  new IllegalArgumentException("Id should not be null");
        }
        try {
            return brandRepository.findById(brandId)
                    .orElseThrow(() -> new ProductNotFoundException("Brand not found  " ));
        }
        catch (DataAccessException e) {
            logger.error("DB error retreiving the brand  ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error retriving brand ", e);
            throw e;
        }
    }

    @Override
    public List<Brand> getAllBrands() {
        try {
            return brandRepository.findAll();
        }
        catch (DataAccessException e) {
            logger.error("DB error retreiving the brand  ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error retriving brand ", e);
            throw e;
        }
        }

    @Override
    public void deleteBrand(Integer brandId) {
        if(brandId==null)
        {
            throw  new IllegalArgumentException("Id should not be null");
        }
        try {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ProductNotFoundException("Brand not found with ID: " + brandId));
            brandRepository.delete(brand);

        }
        catch (DataAccessException e) {
            logger.error("DB error deleting  the brand  ID {}: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error deleting  brand ", e);
            throw e;
        }
    }



    //Category

    @Override
    public Category createCategory(Category category) {
        if(category==null)
        {
            throw new IllegalArgumentException("Category cannot be null");
        }
        try {
            if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
                throw new IllegalArgumentException("Category with this name already exists.");
            }
            return categoryRepository.save(category);
        } catch (DataAccessException e) {
            logger.error("Database error while creating category", e);
            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            logger.error("Unexpected error deleting  brand ", e);
            throw e;
        }
    }

    @Override
    public Category updateCategory(Integer categoryId, Category updatedCategory) {
        if(categoryId==null)
        {
            throw new IllegalArgumentException("Category  id cannot be null");
        }
        if(updatedCategory==null)
        {
            throw new IllegalArgumentException("Category cannot be null");
        }
        try {
            Category existing = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductNotFoundException("Category not found"));

            existing.setCategoryName(updatedCategory.getCategoryName());
            existing.setParent(updatedCategory.getParent());

            return categoryRepository.save(existing);
        } catch (DataAccessException e) {
            logger.error("Database error while updating category", e);
            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            logger.error("Unexpected error deleting  brand ", e);
            throw e;
        }
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        if(categoryId==null)
        {
            throw new IllegalArgumentException("Category  id cannot be null");
        }
        try {
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductNotFoundException("Category not found"));
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving category", e);
            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            logger.error("Unexpected error deleting  brand ", e);
            throw e;
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving categories", e);
            throw new DataBaseException("Internal server error");
        }
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        if(categoryId==null)
        {
            throw new IllegalArgumentException("Category  id cannot be null");
        }
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ProductNotFoundException("Category not found"));
            categoryRepository.delete(category);
        } catch (DataAccessException e) {
            logger.error("Database error while deleting category", e);
            throw new DataBaseException("Internal server error");
        }
    }


    //Realted Products


    @Override
    public RelatedProduct createRelatedProduct(RelatedProduct relatedProduct) {
        try {
            return relatedProductRepo.save(relatedProduct);
        } catch (DataAccessException e) {
            throw new DataBaseException("DB error while creating related product");
        } catch (Exception e) {
            logger.error("Unexpected error while creating related product", e);
            throw e;
        }
    }

    @Override
    public RelatedProduct updateRelatedProduct(Integer id, RelatedProduct updated) {
        try {
            RelatedProduct existing = relatedProductRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Related product not found"));

            existing.setProduct(updated.getProduct());
            existing.setRelatedProduct(updated.getRelatedProduct());
            existing.setRelationshipType(updated.getRelationshipType());
            existing.setIsActive(updated.getIsActive());

            return relatedProductRepo.save(existing);
        } catch (DataAccessException e) {
            throw new DataBaseException("DB error while updating related product");
        } catch (Exception e) {
            logger.error("Unexpected error while updating related product", e);
            throw e;
        }
    }

    @Override
    public RelatedProduct getRelatedProductById(Integer id) {
        try {
            return relatedProductRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Related product not found"));
        } catch (DataAccessException e) {
            throw new DataBaseException("DB error while retrieving related product");
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving related product", e);
            throw e;
        }
    }

    @Override
    public List<RelatedProduct> getAllRelatedProducts() {
        try {
            return relatedProductRepo.findAll();
        } catch (DataAccessException e) {
            throw new DataBaseException("DB error while retrieving all related products");
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving all related products", e);
            throw e;
        }
    }

    @Override
    public void deleteRelatedProduct(Integer id) {
        try {
            RelatedProduct rp = relatedProductRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Related product not found"));
            relatedProductRepo.delete(rp);
        } catch (DataAccessException e) {
            throw new DataBaseException("DB error while deleting related product");
        } catch (Exception e) {
            logger.error("Unexpected error while deleting related product", e);
            throw e;
        }
    }

    @Override
    public List<ProductList> getAllAvailableProducts(Integer userId,Pageable pageable) {

        List<Product> products = productRepo.findAll();

        return products.stream().map(p ->
        {
            //for calculating the discount
            BigDecimal discountPercent = getActiveDiscountPercent(p.getId());
            BigDecimal discountPrice = applyDiscount(p.getPrice(), discountPercent);
//get the ratings

            Double avgRating = reviewRepo.findAverageRatingByProductId(p.getId());
            Integer reviewCount = reviewRepo.countByProductId(p.getId());
//wishlist or cart stat
            boolean inWishlist = wishlistRepo.existsByUserIdAndProductId(userId, p.getId());
            boolean inCart = cartRepo.existsByUserIdAndProductId(userId, p.getId());
            return new ProductList(
                    p.getId(),
                    p.getName(),
                    p.getBrand().getBrandName(),
                    p.getPrice(),
                    discountPrice,
                    discountPrice != null ? discountPercent.intValue() : 0,
                    avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0,
                    reviewCount,
                    p.getIsAvailable(),
                    null,
                    inWishlist,
                    inCart);
        }).toList();
    }









    @Override
    public ProductDetailDTO getProductDetailById(Integer productId, Integer userId) {
Product p = productRepo.findByIdWithBrand(productId)
        .orElseThrow(()->new ProductNotFoundException("Product Not Found"));
//image urls
List<String> imageUrls= productImageRepo.findByProductId(productId)
        .stream().map(ProductImage::getImageUrl).toList();

//Attributes(size/color)

        List<ProductAttributeValue> attrList = productAttributeValueRepo.findByProductId(productId);
        List<ProductAttributeDTO> attributes = attrList.stream().map(a ->
                new ProductAttributeDTO(a.getAttribute().getName(), a.getValue())
        ).toList();


        List<String> sizes = attrList.stream()
                .filter(a -> a.getAttribute().getName().equalsIgnoreCase("Size"))
                .map(ProductAttributeValue::getValue).distinct().toList();

        List<String> colors = attrList.stream()
                .filter(a -> a.getAttribute().getName().equalsIgnoreCase("Color"))
                .map(ProductAttributeValue::getValue).distinct().toList();
//offers

        List<String> offers = productDiscountRepo.findActiveDiscounts(productId)
                .stream().map(d -> d.getDiscount().getName()).toList();


   //relatedProducts

        List<RelatedProductDTO> related = relatedProductRepo.findByProductIdAndIsActiveTrue(productId)
                .stream().map(r -> {
                    Product rel = r.getRelatedProduct();
                    return new RelatedProductDTO(
                            rel.getId(),
                            rel.getName(),
                            rel.getImageAddress(),
                            rel.getPrice(),
                            r.getRelationshipType().name()
                    );
                }).toList();

//review Products
        List<ReviewDTO> reviews = reviewRepo.findByProductId(productId).stream()
                .map(r -> new ReviewDTO(
                        r.getUser().getName(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt()
                )).toList();

        Double avgRating = reviewRepo.findAverageRatingByProductId(productId);
        Integer reviewCount = reviewRepo.countByProductId(productId);


//wishlist and cart status

        boolean inWishlist = wishlistRepo.existsByUserIdAndProductId(userId, productId);
        boolean inCart = cartRepo.existsByUserIdAndProductId(userId, productId);

        //Discount
        BigDecimal discountPercent = getActiveDiscountPercent(productId);
        BigDecimal discountedPrice = applyDiscount(p.getPrice(), discountPercent);
        return new ProductDetailDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getSku(),
                p.getBrand().getBrandName(),
                p.getPrice(),
                discountedPrice,
                discountPercent != null ? discountPercent.intValue() : 0,
                p.getIsAvailable(),
                imageUrls,
                attributes,
                sizes,
                colors,
                offers,
                "7 days return available",
                "Delivered in 3–5 days",
                related,
                avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0,
                reviewCount,
                reviews,
                inWishlist,
                inCart
        );

















    }
    private BigDecimal getActiveDiscountPercent(Integer productId) {
        List<ProductDiscount> discounts = productDiscountRepo.findActiveDiscounts(productId);
        if (discounts.isEmpty()) return null;
        return discounts.get(0).getDiscount().getDiscountPercent();
    }

    private BigDecimal applyDiscount(BigDecimal price, BigDecimal percent) {
        if (percent == null) return price;
        return price.subtract(price.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }



}


//
//    @Autowired
//    private ProductMapper productMapper;
//
//    @Autowired
//    private ProductImageMapper productImageMapper;
//
//    @Autowired
//    private ProductAttributeMapper productAttributeMapper;
//
//    @Autowired
//    private ProductAttributeValueMapper productAttributeValueMapper;
//
//    @Autowired
//    private BrandMapper brandMapper;
//
//    @Autowired
//    private CategoryMapper categoryMapper;
//
//    @Autowired
//    private RelatedProductMapper relatedProductMapper;
//
//    @Override
//    public Product addProduct(Product product) {
//        if (product == null) {
//            throw new IllegalArgumentException("Product cannot be null");
//        }
//        try {
//            int res = productMapper.createProduct(product);
//            if (res <= 0) {
//                throw new DataCreationException("Product could not be added");
//            }
//            return product;
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to create product: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public Product getProductById(int id) {
//        try {
//            Product product = productMapper.getProductById(id);
//            if (product == null) {
//                throw new NotFoundException("Product with ID " + id + " not found");
//            }
//            return product;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve product with ID " + id);
//        }
//    }
//
//    @Override
//    public List<Product> getAllProducts() {
//        try {
//            return productMapper.getAllProducts();
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve all products");
//        }
//    }
//
//    @Override
//    public Product updateProduct(Product product) {
//        if (product == null) {
//            throw new IllegalArgumentException("Product cannot be null");
//        }
//        if (product.getId() == null) {
//            throw new IllegalArgumentException("Product ID must not be null for update");
//        }
//
//        try {
//            Product existing = productMapper.getProductById(product.getId());
//            if (existing == null) {
//                throw new NotFoundException("Product with ID " + product.getId() + " does not exist.");
//            }
//
//            int res = productMapper.updateProduct(product);
//            if (res <= 0) {
//                throw new DataUpdateException("Product update failed");
//            }
//
//            return productMapper.getProductById(product.getId());
//        } catch (DataAccessException e) {
//            throw new DataUpdateException("Failed to update product: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String deleteProduct(int id) {
//        try {
//            int res = productMapper.deleteProductById(id);
//            if (res <= 0) {
//                throw new NotFoundException("Product with ID " + id + " was not deleted or does not exist");
//            }
//            return "Product with ID " + id + " was deleted successfully";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to delete product with ID " + id);
//        }
//    }
//
//    //ProductImage
//
//    @Override
//    public String addProductImage(ProductImage image) {
//        if (image == null) {
//            throw new IllegalArgumentException("Product image cannot be null");
//        }
//        try {
//            int res = productImageMapper.insertProductImage(image);
//            if (res <= 0) {
//                throw new DataCreationException("The product image cannot be added");
//            }
//            return "Image added successfully";
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to add product image: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<ProductImage> getImagesByProductId(int productId) {
//        try {
//            return productImageMapper.getImagesByProductId(productId);
//        } catch (DataAccessException e) {
//            //logger.log(ex)
//            throw new DataRetrievalException("Failed to retrieve images for product ID " + productId);
//        }
//    }
//
//    @Override
//    public String deleteProductImage(int imageId) {
//        try {
//            int res = productImageMapper.deleteProductImage(imageId);
//            if (res <= 0) {
//                throw new NotFoundException("The product image cannot be deleted or does not exist");
//            }
//            return "Image deleted successfully";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to delete product image with ID " + imageId);
//        }
//    }
//
//    //ProductAttribute
//
//    @Override
//    public ProductAttribute addProductAttribute(ProductAttribute attribute) {
//        if (attribute == null) {
//            throw new IllegalArgumentException("Product attribute cannot be null");
//        }
//        try {
//            int res = productAttributeMapper.createAttribute(attribute);
//            if (res <= 0) {
//                throw new DataCreationException("The product attribute could not be created");
//            }
//            return attribute;
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to create product attribute: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<ProductAttribute> getAllAttributes() {
//        try {
//            return productAttributeMapper.getAllAttributes();
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve all product attributes");
//        }
//    }
//
//    @Override
//    public ProductAttribute getAttributeById(int id) {
//        try {
//            ProductAttribute attribute = productAttributeMapper.getAttributeById(id);
//            if (attribute == null) {
//                throw new NotFoundException("Attribute with ID " + id + " not found");
//            }
//            return attribute;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve attribute with ID " + id);
//        }
//    }
//
//    @Override
//    public ProductAttributeValue addProductAttributeValue(ProductAttributeValue value) {
//        if (value == null) {
//            throw new IllegalArgumentException("Attribute value cannot be null");
//        }
//        try {
//            int res = productAttributeValueMapper.createAttributeValue(value);
//            if (res <= 0) {
//                throw new DataCreationException("The product attribute value could not be created");
//            }
//            return value;
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to create attribute value: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<ProductAttributeValue> getAttributeValuesByProductId(int productId) {
//        try {
//            return productAttributeValueMapper.getAttributeValuesByProduct(productId);
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve attribute values for product ID " + productId);
//        }
//    }
//
//    @Override
//    public String deleteAttributeValue(int id) {
//        try {
//            int res = productAttributeValueMapper.deleteAttributeValue(id);
//            if (res <= 0) {
//                throw new NotFoundException("The product attribute value could not be deleted or does not exist");
//            }
//            return "Attribute value was deleted successfully";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to delete attribute value with ID " + id);
//        }
//    }
//
//    @Override
//    public ProductAttributeValue updateProductAttributeValue(ProductAttributeValue value) {
//        if (value == null) {
//            throw new IllegalArgumentException("Attribute value cannot be null");
//        }
//        if (value.getId() == null) {
//            throw new IllegalArgumentException("Attribute value ID must not be null for update");
//        }
//
//        try {
//            ProductAttributeValue existing = productAttributeValueMapper.getAttributeValueById(value.getId());
//            if (existing == null) {
//                throw new NotFoundException("Attribute value with ID " + value.getId() + " does not exist");
//            }
//
//            int res = productAttributeValueMapper.updateAttributeValue(value);
//            if (res <= 0) {
//                throw new DataUpdateException("Attribute value could not be updated");
//            }
//
//            return value;
//        } catch (DataAccessException e) {
//            throw new DataUpdateException("Failed to update attribute value: " + e.getMessage());
//        }
//    }
//
//    //Brand
//
//    @Override
//    public Brand addBrand(Brand brand) {
//        if (brand == null) {
//            throw new IllegalArgumentException("Brand cannot be null");
//        }
//        try {
//            if (brandMapper.getBrandByBrandName(brand.getBrandName())) {
//                throw new DuplicateResourceException("Brand with name '" + brand.getBrandName() + "' already exists");
//            }
//
//            int res = brandMapper.createBrand(brand);
//            if (res <= 0) {
//                throw new DataCreationException("Brand could not be created");
//            }
//            return brand;
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to create brand: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<Brand> getAllBrands() {
//        try {
//            return brandMapper.getAllBrands();
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve all brands");
//        }
//    }
//
//    @Override
//    public Brand getBrandById(int id) {
//        try {
//            Brand brand = brandMapper.getBrandById(id);
//            if (brand == null) {
//                throw new NotFoundException("Brand with ID " + id + " not found");
//            }
//            return brand;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve brand with ID " + id);
//        }
//    }
//
//    @Override
//    public Brand updateBrand(Brand brand) {
//        if (brand == null) {
//            throw new IllegalArgumentException("Brand cannot be null");
//        }
//        try {
//            Brand existingBrand = brandMapper.getBrandById(brand.getBrandId());
//            if (existingBrand == null) {
//                throw new NotFoundException("Brand does not exist with ID: " + brand.getBrandId());
//            }
//
//            int res = brandMapper.updateBrand(brand);
//            if (res <= 0) {
//                throw new DataUpdateException("Brand update failed");
//            }
//
//            return brand;
//        } catch (DataAccessException e) {
//            throw new DataUpdateException("Failed to update brand: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String deleteBrand(int id) {
//        try {
//            Brand existingBrand = brandMapper.getBrandById(id);
//            if (existingBrand == null) {
//                throw new NotFoundException("Brand does not exist with ID: " + id);
//            }
//
//            int res = brandMapper.deleteBrand(id);
//            if (res <= 0) {
//                throw new DataDeletionException("Brand delete failed");
//            }
//            return "Brand was deleted successfully";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to delete brand with ID " + id);
//        }
//    }
//
//    @Override
//    public Category addCategory(Category category) {
//        if (category == null) {
//            throw new IllegalArgumentException("Category cannot be null");
//        }
//        try {
//            Category existingCategory = categoryMapper.getCategoryByName(category.getCategoryName());
//            if (existingCategory != null) {
//                throw new DuplicateResourceException("Category with name '" + category.getCategoryName() + "' already exists");
//            }
//
//            int res = categoryMapper.createCategory(category);
//            if (res <= 0) {
//                throw new DataCreationException("Category could not be added");
//            }
//            return category;
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to create category: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<Category> getAllCategories() {
//        try {
//            return categoryMapper.getAllCategories();
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve all categories");
//        }
//    }
//
//    @Override
//    public Category getCategoryById(int id) {
//        try {
//            Category category = categoryMapper.getCategoryById(id);
//            if (category == null) {
//                throw new NotFoundException("Category with ID " + id + " not found");
//            }
//            return category;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve category with ID " + id);
//        }
//    }
//
//    @Override
//    public Category updateCategory(Category category) {
//        if (category == null) {
//            throw new IllegalArgumentException("Category cannot be null");
//        }
//        if (category.getCategoryId() == null) {
//            throw new IllegalArgumentException("Category ID must not be null for update");
//        }
//        try {
//            Category existingCategory = categoryMapper.getCategoryById(category.getCategoryId());
//            if (existingCategory == null) {
//                throw new NotFoundException("Category does not exist with ID: " + category.getCategoryId());
//            }
//
//            int res = categoryMapper.updateCategory(category);
//            if (res <= 0) {
//                throw new DataUpdateException("Category could not be updated");
//            }
//            return category;
//        } catch (DataAccessException e) {
//            throw new DataUpdateException("Failed to update category: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public boolean deleteCategory(int id)
//    {
//        if (id <= 0) {
//            throw new IllegalArgumentException("Category ID must be a positive number");
//        }
//
//        try {
//            Category existingCategory = categoryMapper.getCategoryById(id);
//            if (existingCategory == null)
//            {
//                throw new NotFoundException("Category does not exist with ID");
//            }
//            int affectedRows = categoryMapper.deleteCategory(id);
//
//            if (affectedRows == 0) {
//                throw new DataUpdateException("No category was deleted - possible concurrency issue");
//            }
//
//            return true;
//        } catch (DataAccessException e) {
//            throw new DataUpdateException("Failed to update category: " + e.getMessage());
//        }
//   }
//
//    //related product
//    @Override
//    public RelatedProduct addRelatedProduct(RelatedProduct relatedProduct) {
//        if (relatedProduct == null) {
//            throw new IllegalArgumentException("Related product cannot be null");
//        }
//
//        if (relatedProduct.getProductId().equals(relatedProduct.getRelatedProductId())) {
//            throw new IllegalArgumentException("Product cannot be related to itself");
//        }
//
//        try {
//            Product mainProduct = productMapper.getProductById(relatedProduct.getProductId());
//            if (mainProduct == null) {
//                throw new NotFoundException("Main product with ID " + relatedProduct.getProductId() + " does not exist");
//            }
//
//            Product relatedProd = productMapper.getProductById(relatedProduct.getRelatedProductId());
//            if (relatedProd == null) {
//                throw new NotFoundException("Related product with ID " + relatedProduct.getRelatedProductId() + " does not exist");
//            }
//
//            relatedProduct.setCreatedAt(LocalDateTime.now());
//            relatedProduct.setIsActive(true);
//            relatedProductMapper.insertRelatedProduct(relatedProduct);
//
//            return relatedProduct;
//        } catch (DuplicateKeyException e) {
//            throw new DuplicateRelationException("This relationship already exists");
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to create relationship: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<RelatedProduct> getRelatedProductsByProductId(int productId) {
//        try {
//            return relatedProductMapper.getActiveRelatedProducts(productId);
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve related products");
//        }
//    }
//
//    @Override
//    @Transactional
//    public String deleteRelatedProduct(int relationId) {
//        try {
//            int rowsAffected = relatedProductMapper.deactivateRelation(relationId);
//            if (rowsAffected == 0) {
//                throw new NotFoundException("Relation not found with ID: " + relationId);
//            }
//            return "Successfully deactivated relation with ID: " + relationId;
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to delete relation: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<RelatedProduct> getRelatedProductsByType(int productId, RelatedProduct.RelationshipType type) {
//        try {
//            return relatedProductMapper.getRelatedProductsByType(productId, type.toString());
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve related products by type");
//        }
//    }
//
//    @Override
//    @Transactional
//    public String hardDeleteRelation(int productId, int relatedProductId, RelatedProduct.RelationshipType type) {
//        try {
//            int rowsDeleted = relatedProductMapper.deleteRelation(
//                    productId,
//                    relatedProductId,
//                    type.toString()
//            );
//
//            if (rowsDeleted == 0) {
//                throw new NotFoundException("No such relation found");
//            }
//            return "Successfully deleted relation";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to hard delete relation: " + e.getMessage());
//        }
//    }
//}