package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Mapper.ProductAttributeValueMapper;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.Service.RelatedProductService;
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

@Autowired
private  UserFavouriteRepo userFavouriteRepo;

@Autowired
private RelatedProductService relatedProductService;


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

            ProductList productList = new ProductList();

            productList.setId(p.getId());
            productList.setName(p.getName());
            productList.setBrandName(p.getBrand().getBrandName());
            productList.setDescription(p.getDescription());
            productList.setThumbnailUrl(p.getImageAddress()); // Assuming this is imageAddress
            productList.setPrice(p.getPrice());
            productList.setDiscountedPrice(discountPrice);
            productList.setDiscountPercentage(discountPercent != null ? discountPercent.intValue() : 0);
            productList.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
            productList.setReviewCount(reviewCount);
            productList.setInStock(p.getIsAvailable());
            productList.setLabel(null); // Set as needed

            return productList;
        }).toList();
    }



    @Override
    public ProductDetail  getProductDetailById(Integer productId) {

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

//============================discount=============================//

//        List<Discount> discounts = discountRepo.findActiveDiscounts();
//        if (!discounts.isEmpty()) {
//            Discount discount = discounts.get(0);
//            discount.getDiscountPercent();// Example: apply first
//            dto1.setActiveDiscount(pavMapper.toActiveDiscountResponse(discount));
//        }

      Optional<Discount> discount = productDiscountRepo.findDiscountDetailsByProductId(productId);
     Product product1= productRepo.findById(productId).orElseThrow(()->new ProductNotFoundException("product not found"));
   BigDecimal price= product1.getPrice();
   if(discount.isPresent())
   {
      Discount existingDiscount=  discount.get();
       BigDecimal discountPer=  existingDiscount.getDiscountPercent();

       BigDecimal appliedPrice= applyDiscount(price,discountPer);
                 DiscountResponse discountDTO = new DiscountResponse();
                 discountDTO.setName(existingDiscount.getName());
                 discountDTO.setDiscountPercent(discountPer);
                 discountDTO.setDiscountedPrice(appliedPrice);
       dto1.setActiveDiscount(discountDTO);

//return  discountDTO;
   }else {
       dto1.setActiveDiscount(null); // No discount
   }




//============================discount=============================//










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
        List<RelatedProductResponse> related = relatedProductService.getRelatedProductsByProductId(productId);
        dto1.setRelatedProducts(related);


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

        List<CouponResponse1> couponResponses = couponRepo.findActiveCoupons()
                .stream()
                .map(coupon -> {
                    CouponResponse1 response = new CouponResponse1();
                    response.setCode(coupon.getCode());
                    response.setDiscountAmount(coupon.getDiscountAmount());
                    response.setExpiryDate(coupon.getExpiryDate());
                    return response;
                })
                .collect(Collectors.toList());

        dto1.setAvailableCoupons(couponResponses);



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
        Set<Integer> favouriteProductIds = userFavouriteRepo.findProductIdsByUserId(userId);


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
            boolean inLiked = favouriteProductIds.contains(p.getId());

            ProductList productList = new ProductList();

            productList.setId(p.getId());
            productList.setName(p.getName());
            productList.setBrandName(p.getBrand().getBrandName());
            productList.setDescription(p.getDescription());
            productList.setThumbnailUrl(p.getImageAddress()); // Assuming this is imageAddress
            productList.setPrice(p.getPrice());
            productList.setDiscountedPrice(discountPrice);
            productList.setDiscountPercentage(discountPercent != null ? discountPercent.intValue() : 0);
            productList.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
            productList.setReviewCount(reviewCount);
            productList.setInStock(p.getIsAvailable());
            productList.setLabel(null); // Set as needed
          productList.setInWishlist(inWishlist);
          productList.setIsLiked(inLiked);
            return productList;


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


        Optional<Discount> discount = productDiscountRepo.findDiscountDetailsByProductId(productId);
        Product product1= productRepo.findById(productId).orElseThrow(()->new ProductNotFoundException("product not found"));
        BigDecimal price= product1.getPrice();
        if(discount.isPresent())
        {
            Discount existingDiscount=  discount.get();
            BigDecimal discountPer=  existingDiscount.getDiscountPercent();

            BigDecimal appliedPrice= applyDiscount(price,discountPer);
            DiscountResponse discountDTO = new DiscountResponse();
            discountDTO.setName(existingDiscount.getName());
            discountDTO.setDiscountPercent(discountPer);
            discountDTO.setDiscountedPrice(appliedPrice);
            dto.setActiveDiscount(discountDTO);

//return  discountDTO;
        }else {
            dto.setActiveDiscount(null); // No discount
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
            Set<Integer> cartProductIds = userFavouriteRepo.findProductIdsByUserId(userId);
            dto.setInWishlist(wishlistProductIds.contains(productId));
            dto.setLiked(cartProductIds.contains(productId));
        }

        return dto;
    }



    private BigDecimal getActiveDiscountPercent(Integer productId) {
        Optional<BigDecimal> discounts = productDiscountRepo.findDiscountPercentByProductId(productId);
        if (discounts.isEmpty()) return null;
        return  discounts.get();
    }

    private BigDecimal applyDiscount(BigDecimal price, BigDecimal percent) {
        if (percent == null) return price;
        return price.subtract(price.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }



}

