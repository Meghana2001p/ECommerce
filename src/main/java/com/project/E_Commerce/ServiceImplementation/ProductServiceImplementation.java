package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Mapper.ProductAttributeValueMapper;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

@Autowired
private ProductAttributeValueMapper pavMapper;


@Autowired
    private ProductImageRepo imageRepository;
@Autowired
    private InventoryRepo inventoryRepo;
@Autowired
    private DiscountRepo discountRepo;
@Autowired
    private CouponRepo couponRepo;

@Autowired
private UserRepo userRepo;


    public Product addProduct(ProductRequest product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (productRepo.existsBySku(product.getSku())) {
            throw new DuplicateResourceException("Product with SKU already exists");
        }
        try {
            // 1. Get brand from DB
            Brand brand = brandRepository.findById(product.getBrandId())
                    .orElseThrow(() -> new NotFoundException("Brand not found"));



            // 2. Create new Product
            Product newproduct = new Product();
            newproduct.setName(product.getName());
            newproduct.setDescription(product.getDescription());
            newproduct.setImageAddress(product.getImageAddress());
            newproduct.setPrice(product.getPrice());
            newproduct.setSku(product.getSku());
            newproduct.setIsAvailable(product.getIsAvailable());
            newproduct.setBrand(brand);

            // 3. Add images
            List<ProductImage> images = product.getImageUrls().stream().map(url -> {
                ProductImage img = new ProductImage();
                img.setImageUrl(url);
                img.setProduct(newproduct); // set back-reference
                return img;
            }).collect(Collectors.toList());

            newproduct.setImages(images);

            // 4. Save product → will also save images if you set cascade correctly
            return productRepo.save(newproduct);
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




    //ProductAttributeValue

    //the product that is going to have the attributes

    @Override
    public String addAttributeValue(ProductAttributeAssignmentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Product and Attribute must not be null");
        }

        Integer productId = request.getProductId();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        List<ProductAttributeValue> savedValues = new ArrayList<>();

        for (ProductAttributeAssignmentRequest.AttributeValuePair pair : request.getAttributes()) {
            Integer attrId = pair.getAttributeId();
            String value = pair.getValue();

            ProductAttribute attribute = productAttributeRepo.findById(attrId)
                    .orElseThrow(() -> new ProductNotFoundException("Attribute not found with ID: " + attrId));

            boolean exists = productAttributeValueRepo.existsByProductIdAndAttributeId(productId, attrId);
            if (exists) {
                throw new IllegalArgumentException("Attribute already exists for product: productId="
                        + productId + ", attributeId=" + attrId);
            }


            ProductAttributeValue pav = pavMapper.toEntity(pair, product, attribute);


            ProductAttributeValue saved = productAttributeValueRepo.save(pav);
            savedValues.add(saved);
        }

        return "Product Attribute Value added successfully";
    }


    @Override
    public void updateAttributeValue(ProductAttributeAssignmentRequest request) {
        if (request == null || request.getProductId() == null || request.getAttributes() == null) {
            throw new IllegalArgumentException("Invalid request data");
        }

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        for (ProductAttributeAssignmentRequest.AttributeValuePair pair : request.getAttributes()) {
            ProductAttribute attribute = productAttributeRepo.findById(pair.getAttributeId())
                    .orElseThrow(() -> new ProductNotFoundException("Attribute not found"));

            Optional<ProductAttributeValue> optional = productAttributeValueRepo
                    .findByProductIdAndAttributeId(product.getId(), attribute.getId());

            if (optional.isPresent()) {
                // Update existing value
                ProductAttributeValue existing = optional.get();
                existing.setValue(pair.getValue());
                productAttributeValueRepo.save(existing);
            } else {
                // Insert new record using mapper
                ProductAttributeValue newValue = pavMapper.toEntity(pair, product, attribute);
                productAttributeValueRepo.save(newValue);
            }
        }
    }




    @Override
    public ProductAttributeValueResponse getAttributeValueById(Integer id) {
        if (id==null) {
            throw new IllegalArgumentException("Invalid  data");
        }

        ProductAttributeValue pav =   productAttributeValueRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Attribute value not found with ID: " + id));

            return new ProductAttributeValueResponse
                    (
                    pav.getId(),
                    pav.getValue(),
                    pav.getProduct().getId(),
                    pav.getAttribute().getId()
            );

    }

    @Override
    public String deleteAttributeValue(Integer id) {

            ProductAttributeValue val = productAttributeValueRepo.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Attribute value not found with ID: " + id));
            productAttributeValueRepo.deleteById(id);
            return "Attribute value deleted successfully";


    }



    //Brand

    @Override
    public Brand createBrand(BrandRequest brandRequest) {
        if (brandRequest == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        try {
            // Check if brand name already exists
            if (brandRepository.existsByBrandName(brandRequest.getBrandName())) {
                throw new IllegalArgumentException("Brand with name '" + brandRequest.getBrandName() + "' already exists.");
            }
            Brand brand = new Brand();
            brand.setBrandName(brandRequest.getBrandName());

            if (brandRequest.getCategoryIds() != null && !brandRequest.getCategoryIds().isEmpty()) {
                List<Category> categories = categoryRepository.findAllById(brandRequest.getCategoryIds());
                //the categoried size should be equal to the categories that is sent therough the request
                if (categories.size() != brandRequest.getCategoryIds().size()) {
                    throw new IllegalArgumentException("One or more category IDs are invalid");
                }
                brand.setCategories(categories);
            } else {
                throw new IllegalArgumentException("At least one category must be provided");
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
    public Brand updateBrand(Integer brandId, BrandRequest updatedBrand) {
        if(brandId==null)
        {
            throw  new IllegalArgumentException("Id should not be null");
        }
        try {
            Brand existingBrand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ProductNotFoundException("Brand not found with ID: "));

            existingBrand.setBrandName(updatedBrand.getBrandName());
            if (updatedBrand.getCategoryIds() != null) {
                List<Category> categories = categoryRepository.findAllById(updatedBrand.getCategoryIds());
                if (categories.size() != updatedBrand.getCategoryIds().size()) {
                    throw new IllegalArgumentException("Invalid category IDs");
                }
                existingBrand.setCategories(categories);
            }
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
    public Category createCategory(CategoryRequest category) {
        if(category==null)
        {
            throw new IllegalArgumentException("Category cannot be null");
        }
        try {
            if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
                throw new IllegalArgumentException("Category with this name already exists.");
            }

            Category newCategory= new Category();
            newCategory.setCategoryName(category.getCategoryName());
            Integer parentId = category.getParentId();
            if (parentId != null) {
                Category parent = categoryRepository.findById(parentId)
                        .orElseThrow(() -> new NotFoundException("Parent category not found"));
                newCategory.setParent(parent);
            } else {
                newCategory.setParent(null);
            }
            categoryRepository.save(newCategory);
            return newCategory;
        } catch (DataAccessException e) {
            logger.error("Database error while creating category", e);
            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            logger.error("Unexpected error creating the category ", e);
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
    public List<CategoryResponse> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();

            List<CategoryResponse> responseList = new ArrayList<>();

            for (Category category : categories) {
                CategoryResponse response = new CategoryResponse();
                response.setCategoryId(category.getCategoryId());
                response.setCategoryName(category.getCategoryName());
                response.setParentID(
                        category.getParent() != null ? category.getParent().getCategoryId() : null
                );
                responseList.add(response);
            }

            return responseList;

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
    public List<ProductList> getAllAvailableProducts(Pageable pageable) {

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
          //  boolean inWishlist = wishlistRepo.existsByUserIdAndProductId(userId, p.getId());
            //boolean inCart = cartRepo.existsByUserIdAndProductId(userId, p.getId());
            return new ProductList(
                    p.getId(),
                    p.getName(),
                    p.getBrand().getBrandName(),
                    p.getDescription(),
                    p.getImageAddress(),
                    p.getPrice(),
                    discountPrice,
                    discountPercent != null ? discountPercent.intValue() : 0,
                    avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0,
                    reviewCount,
                    p.getIsAvailable(),
                    null
                  );
            // // inWishlist,
            //                    // inCart
        }).toList();
    }



    @Override
    public ProductDetail getProductDetailById(Integer productId) {

        ProductWithBrandProjection product = productRepo.findWithBrand(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

// Map to ProductDetail DTO manually here


        // 2️⃣ Build ProductDetailDTO manually
//        ProductDetail dto = new ProductDetail();
//        dto.setProductId(product.getProductId()); // Assuming product.getId() is correct
//        dto.setName(product.getName());
//        dto.setDescription(product.getDescription());
//        dto.setSku(product.getSku());
//        dto.setPrice(product.getPrice());
//        dto.setIsAvailable(product.getIsAvailable());
//        dto.setBrandName(product.getBrandName());

        ProductDetail dto1 = pavMapper.toProductDetail(product);


//        // 3️⃣ Set image URLs
//        List<ProductImage> images = imageRepository.findByProductIdOrderByIsPrimary(productId);
//        List<String> imageUrls = images.stream()
//                .map(ProductImage::getImageUrl)
//                .toList();
//        dto1.setImageUrls(imageUrls);

        List<ProductImage> images = imageRepository.findByProductIdOrderByIsPrimary(productId);
        dto1.setImageUrls(pavMapper.mapImagesToUrls(images));

//        // 4️⃣ Set attributes
//        List<ProductAttributeValue> pavs = productAttributeValueRepo.findByProductId(productId);
//        List<ProductAttributeResponse> attributeDTOs = pavs.stream().map(pav -> {
//            ProductAttributeResponse attrDTO = new ProductAttributeResponse();
//            attrDTO.setAttributeName(pav.getAttribute().getName());
//            attrDTO.setValue(pav.getValue());
//            return attrDTO;
//        }).toList();
//        dto.setAttributes(attributeDTOs);



        List<ProductAttributeValue> pavs = productAttributeValueRepo.findByProductId(productId);
        dto1.setAttributes(pavMapper.mapAttributes(pavs));



//        // 5️⃣ Set inventory
//        inventoryRepo.findByProductId(productId).ifPresent(inv -> {
//            InventoryResponse invDTO = new InventoryResponse();
//            invDTO.setInStock(inv.getInStock());
//            invDTO.setStockQuantity(inv.getStockQuantity());
//            invDTO.setLastUpdated(inv.getLastUpdated());
//            dto.setInventory(invDTO);
//        });

        inventoryRepo.findByProductId(productId).ifPresent(inv -> {
            dto1.setInventory(pavMapper.toInventoryResponse(inv));
        });


//        // 6️⃣ Set active discount
//        List<Discount> discounts = discountRepo.findActiveDiscounts();
//        if (!discounts.isEmpty()) {
//            Discount discount = discounts.get(0); // Example: apply first
//            DiscountResponse discountDTO = new DiscountResponse();
//            discountDTO.setName(discount.getName());
//            discountDTO.setDiscountPercent(discount.getDiscountPercent());
//            discountDTO.setStartDate(discount.getStartDate());
//            discountDTO.setEndDate(discount.getEndDate());
//            discountDTO.setIsActive(true);
//            dto.setActiveDiscount(discountDTO);
//        }

        List<Discount> discounts = discountRepo.findActiveDiscounts();
        if (!discounts.isEmpty()) {
            Discount discount = discounts.get(0); // Example: apply first
            dto1.setActiveDiscount(pavMapper.toActiveDiscountResponse(discount));
        }


//        // 7️⃣ Set reviews
//        List<Review> reviews = reviewRepo.findByProductId(productId);
//        List<ReviewResponse> reviewDTOs = reviews.stream().map(r -> {
//            ReviewResponse reviewDTO = new ReviewResponse();
//            reviewDTO.setComment(r.getComment());
//            reviewDTO.setRating(r.getRating());
//            reviewDTO.setCreatedAt(r.getCreatedAt());
//            reviewDTO.setUserId(r.getUser().getId());
//            reviewDTO.setUserName(r.getUser().getName());
//            return reviewDTO;
//        }).toList();
//        dto.setReviews(reviewDTOs);

        List<Review> reviews = reviewRepo.findByProductId(productId);
        dto1.setReviews(pavMapper.toReviewResponseList(reviews));


        // 8️⃣ Set average rating
        Double avgRating = reviewRepo.findAverageRating(productId);
        dto1.setAverageRating(avgRating != null ? avgRating : 0.0);

//        // 9️⃣ Set related products
//        List<RelatedProduct> related = relatedProductRepo.findActiveRelatedByProductId(productId);
//        List<RelatedProductResponse> relatedDTOs = related.stream().map(rel -> {
//            Product relatedProduct = rel.getRelatedProduct();
//            RelatedProductResponse rpDTO = new RelatedProductResponse();
//            rpDTO.setProductId(relatedProduct.getId());
//            rpDTO.setName(relatedProduct.getName());
//            rpDTO.setPrice(relatedProduct.getPrice());
//            rpDTO.setImageUrl(relatedProduct.getImageAddress());
//            rpDTO.setRelationshipType(rel.getRelationshipType().name());
//            return rpDTO;
//        }).toList();
//        dto.setRelatedProducts(relatedDTOs);
        List<RelatedProduct> related = relatedProductRepo.findActiveRelatedByProductId(productId);
        dto1.setRelatedProducts(pavMapper.toRelatedProductResponseList(related));


//        // 🔟 Set available coupons
//        List<Coupon> coupons = couponRepo.findActiveCoupons();
//        List<CouponResponse> couponDTOs = coupons.stream().map(c -> {
//            CouponResponse couponDTO = new CouponResponse();
//            couponDTO.setCode(c.getCode());
//            couponDTO.setDiscountAmount(c.getDiscountAmount());
//            couponDTO.setDiscountPercent(c.getDiscountPercent());
//            couponDTO.setExpiryDate(c.getExpiryDate());
//            couponDTO.setUsageLimit(c.getUsageLimit());
//            return couponDTO;
//        }).toList();
//        dto.setAvailableCoupons(couponDTOs);

        List<Coupon> coupons = couponRepo.findActiveCoupons();
        dto1.setAvailableCoupons(pavMapper.toCouponResponseList(coupons));


        // 🔁 Optional: Set delivery info (if needed in future)
    /*
    deliveryStatusRepository.findLatestByOrderId(orderId).ifPresent(delivery -> {
        DeliveryInfoResponse deliveryInfo = new DeliveryInfoResponse();
        deliveryInfo.setCarrier(delivery.getCarrier());
        deliveryInfo.setDeliveryType(delivery.getDeliveryType());
        deliveryInfo.setEstimatedDeliveryDate(delivery.getEstimatedDeliveryDate());
        deliveryInfo.setStatus(delivery.getStatus());
        deliveryInfo.setTrackingNumber(delivery.getTrackingNumber());
        dto.setDeliveryInfo(deliveryInfo);
    });
    */


        return dto1;
    }


    @Override
    public List<ProductList> getAllProductsAfterLogin(int userId, int page) {

        userRepo.findById(userId)
                .orElseThrow(()->new UserNotFoundException("user doesnot exists"));

        List<Product> products = productRepo.findAll();

        Set<Integer> wishlistProductIds = wishlistRepo.findProductIdsByUserId(userId);
        Set<Integer> cartProductIds = cartRepo.findProductIdsByUserId(userId);


        return products.stream().map(p ->
        {
            //for calculating the discount
            BigDecimal discountPercent = getActiveDiscountPercent(p.getId());
            BigDecimal discountPrice = applyDiscount(p.getPrice(), discountPercent);
//get the ratings

            Double avgRating = reviewRepo.findAverageRatingByProductId(p.getId());
            Integer reviewCount = reviewRepo.countByProductId(p.getId());
//wishlist or cart stat
            boolean inWishlist = wishlistProductIds.contains(p.getId());
            boolean inCart = cartProductIds.contains(p.getId());
            return new ProductList(
                    p.getId(),
                    p.getName(),
                    p.getBrand().getBrandName(),
                    p.getDescription(),
                    p.getImageAddress(),
                    p.getPrice(),
                    discountPrice,
                    discountPercent != null ? discountPercent.intValue() : 0,
                    avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0,
                    reviewCount,
                    p.getIsAvailable(),
                    null,
                    inWishlist,
                    inCart );
        }).toList();

    }

    @Override
    public ProductDetail getProductByIdAndUserId(int userId, int page, int productId) {

        // 1️⃣ Fetch product and brand projection
        ProductWithBrandProjection product = productRepo.findWithBrand(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2️⃣ Map basic product + brand info
        ProductDetail dto = pavMapper.toProductDetail(product);

        // 3️⃣ Set image URLs
        List<ProductImage> images = imageRepository.findByProductIdOrderByIsPrimary(productId);
        dto.setImageUrls(pavMapper.mapImagesToUrls(images));

        // 4️⃣ Set attributes
        List<ProductAttributeValue> pavs = productAttributeValueRepo.findByProductId(productId);
        dto.setAttributes(pavMapper.mapAttributes(pavs));

        // 5️⃣ Set inventory
        inventoryRepo.findByProductId(productId)
                .ifPresent(inv -> dto.setInventory(pavMapper.toInventoryResponse(inv)));

        // 6️⃣ Set active discount
        List<Discount> discounts = discountRepo.findActiveDiscounts();
        if (!discounts.isEmpty()) {
            dto.setActiveDiscount(pavMapper.toActiveDiscountResponse(discounts.get(0)));
        }

        // 7️⃣ Set reviews
        List<Review> reviews = reviewRepo.findByProductId(productId);
        dto.setReviews(pavMapper.toReviewResponseList(reviews));

        // 8️⃣ Set average rating
        Double avgRating = reviewRepo.findAverageRating(productId);
        dto.setAverageRating(avgRating != null ? avgRating : 0.0);

        // 9️⃣ Set related products
        List<RelatedProduct> related = relatedProductRepo.findActiveRelatedByProductId(productId);
        dto.setRelatedProducts(pavMapper.toRelatedProductResponseList(related));

        // 🔟 Set available coupons
        List<Coupon> coupons = couponRepo.findActiveCoupons();
        dto.setAvailableCoupons(pavMapper.toCouponResponseList(coupons));

        // 🔁  Set wishlist/cart flags (user-specific)
        if (userId > 0) {
            Set<Integer> wishlistProductIds = wishlistRepo.findProductIdsByUserId(userId);
            Set<Integer> cartProductIds = cartRepo.findProductIdsByUserId(userId);
            dto.setInWishlist(wishlistProductIds.contains(productId));
            dto.setInCart(cartProductIds.contains(productId));
        }

        return dto;
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