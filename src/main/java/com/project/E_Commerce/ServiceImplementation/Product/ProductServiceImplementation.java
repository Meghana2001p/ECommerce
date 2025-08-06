package com.project.E_Commerce.ServiceImplementation.Product;

import com.project.E_Commerce.Entity.Cart.Coupon;
import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Entity.Product.*;
import com.project.E_Commerce.Mapper.ProductAttributeValueMapper;
import com.project.E_Commerce.Repository.Cart.CartRepo;
import com.project.E_Commerce.Repository.Order.CouponRepo;
import com.project.E_Commerce.Repository.Product.*;
import com.project.E_Commerce.Repository.Return.RelatedProductRepo;
import com.project.E_Commerce.Repository.User.UserFavouriteRepo;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Repository.User.WishlistRepo;
import com.project.E_Commerce.Service.Product.ProductService;
import com.project.E_Commerce.Service.Product.RelatedProductService;
import com.project.E_Commerce.dto.Product.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

//Product, ProductImage, ProductAttribue, ProductAttributeValue, Brand, Category, RelatedProduct
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
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
private RelatedProductRepo relatedProductRepo;

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
private UserFavouriteRepo userFavouriteRepo;

@Autowired
private RelatedProductService relatedProductService;


    public Product addProduct(ProductRequest product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (productRepo.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product with SKU already exists");
        }

            // 1. Get brand from DB
            Brand brand = brandRepository.findById(product.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found"));


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
                img.setProduct(newproduct);
                return img;
            }).collect(Collectors.toList());


            newproduct.setImages(images);


            return productRepo.save(newproduct);

    }

    @Override
    public Product getProductById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }


            return productRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        }


     @Override
    public String updateProduct(Integer id, ProductRequest dto) {
        if (id == null || id <= 0 || dto == null) {
            throw new IllegalArgumentException("Invalid product data or ID");
        }

        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Update core fields
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setImageAddress(dto.getImageAddress());
        existing.setPrice(dto.getPrice());
        existing.setSku(dto.getSku());
        existing.setIsAvailable(dto.getIsAvailable());

        // Update brand
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));
        existing.setBrand(brand);

        // Save product first (in case product_id is needed for images)
        Product savedProduct = productRepo.save(existing);

        // Remove old images
        productImageRepo.deleteByProductId(savedProduct.getId());

        // Add new images
        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            List<ProductImage> images = new ArrayList<>();

            for (int i = 0; i < dto.getImageUrls().size(); i++) {
                ProductImage img = new ProductImage();
                img.setProduct(savedProduct);
                img.setImageUrl(dto.getImageUrls().get(i));
                img.setAltText(savedProduct.getName() + " image " + (i + 1));

                images.add(img);
            }

            productImageRepo.saveAll(images);
        }

        return "Product updated successfully";
    }

    public String deleteProduct(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }


            Product existing = productRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
            productRepo.delete(existing);
            return "Product deleted successfully";

    }


    //Product Image
    @Override
    public String addProductImage(ProductImage image) {
        if (image == null || image.getProduct() == null || image.getProduct().getId() == null) {
            throw new IllegalArgumentException("Image or Product ID is required");
        }


            boolean exists = productImageRepo.existsById(image.getProduct().getId());
            if (!exists) {
                throw new IllegalArgumentException("Product not found with id: " + image.getProduct().getId());
            }

            ProductImage saved = productImageRepo.save(image);
            if (saved.getId() == null || saved.getId() <= 0) {
                throw new IllegalArgumentException("Image could not be saved");
            }

            return "Product image added successfully ";

    }

    @Override
    public List<ProductImage> getImagesByProductId(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }


            return productImageRepo.findByProductId(productId);

    }

    @Override
    public String deleteProductImage(int imageId) {
        if (imageId <= 0) {
            throw new IllegalArgumentException("Invalid image ID");
        }


            ProductImage image = productImageRepo.findById(imageId)
                    .orElseThrow(() -> new IllegalArgumentException("Image not found "));
            productImageRepo.delete(image);
            return "Product image deleted successfully";

    }
    //Attributes

    @Override
    public ProductAttribute addAttribute(ProductAttribute attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute cannot be null");
        }


            ProductAttribute saved = productAttributeRepo.save(attribute);
            if (saved.getId() <= 0) {
                throw new RuntimeException("Attribute could not be saved");
            }
            return saved;

    }

    @Override
    public ProductAttribute updateAttribute(int attributeId, ProductAttribute updated) {
        if (attributeId <= 0 || updated == null) {
            throw new IllegalArgumentException("Invalid data for update");
        }


            ProductAttribute existing = productAttributeRepo.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));

            existing.setName(updated.getName());
            return productAttributeRepo.save(existing);

    }

    @Override
    public ProductAttribute getAttributeById(int attributeId) {
        if (attributeId <= 0) {
            throw new IllegalArgumentException("Invalid attribute ID");
        }


            return productAttributeRepo.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));

    }

    @Override
    public String deleteAttribute(int attributeId) {
        if (attributeId <= 0) {
            throw new IllegalArgumentException("Invalid attribute ID");
        }


            ProductAttribute existing = productAttributeRepo.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));
            productAttributeRepo.delete(existing);
            return "Attribute deleted successfully";

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
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        List<ProductAttributeValue> savedValues = new ArrayList<>();

        for (ProductAttributeAssignmentRequest.AttributeValuePair pair : request.getAttributes()) {
            Integer attrId = pair.getAttributeId();
            String value = pair.getValue();

            ProductAttribute attribute = productAttributeRepo.findById(attrId)
                    .orElseThrow(() -> new IllegalArgumentException("Attribute not found with ID: " + attrId));

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
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        for (ProductAttributeAssignmentRequest.AttributeValuePair pair : request.getAttributes()) {
            ProductAttribute attribute = productAttributeRepo.findById(pair.getAttributeId())
                    .orElseThrow(() -> new IllegalArgumentException("Attribute not found"));

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
                    .orElseThrow(() -> new IllegalArgumentException("Attribute value not found with ID: " + id));

        ProductAttributeValueResponse response = new ProductAttributeValueResponse();
response.setId(pav.getId());
response.setValue(pav.getValue());
response.setAttributeName(pav.getValue());
response.setProductId(pav.getProduct().getId());
response.setProductName(pav.getProduct().getName());
response.setAttributeId(pav.getAttribute().getId());
response.setAttributeName(pav.getAttribute().getName());
return  response;

    }

    @Override
    public String deleteAttributeValue(Integer id) {

            ProductAttributeValue val = productAttributeValueRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Attribute value not found with ID: " + id));
            productAttributeValueRepo.deleteById(id);
            return "Attribute value deleted successfully";


    }



    //Brand

    @Override
    public Brand createBrand(BrandRequest brandRequest) {
        if (brandRequest == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }

            if (brandRepository.existsByBrandName(brandRequest.getBrandName())) {
                throw new IllegalArgumentException("Brand with name '" + brandRequest.getBrandName() + "' already exists.");
            }
            Brand brand = new Brand();
            brand.setBrandName(brandRequest.getBrandName());

            if (brandRequest.getCategoryIds() != null && !brandRequest.getCategoryIds().isEmpty()) {
                List<Category> categories = categoryRepository.findAllById(brandRequest.getCategoryIds());
                if (categories.size() != brandRequest.getCategoryIds().size()) {
                    throw new IllegalArgumentException("One or more category IDs are invalid");
                }
                brand.setCategories(categories);
            } else {
                throw new IllegalArgumentException("At least one category must be provided");
            }

            return brandRepository.save(brand);

    }

    @Override
    public Brand updateBrand(Integer brandId, BrandRequest updatedBrand) {
        if(brandId==null)
        {
            throw  new IllegalArgumentException("Id should not be null");
        }

            Brand existingBrand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found with ID: "));

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

    @Override
    public Brand getBrandById(Integer brandId) {
        if(brandId==null)
        {
            throw  new IllegalArgumentException("Id should not be null");
        }

            return brandRepository.findById(brandId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found  " ));


    }

    @Override
    public List<Brand> getAllBrands() {

            return brandRepository.findAll();

        }

    @Override
    public void deleteBrand(Integer brandId) {
        if(brandId==null)
        {
            throw  new IllegalArgumentException("Id should not be null");
        }

            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found with ID: " + brandId));
            brandRepository.delete(brand);


    }



    //Category

    @Override
    public Category createCategory(CategoryRequest category) {
        if(category==null)
        {
            throw new IllegalArgumentException("Category cannot be null");
        }

            if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
                throw new IllegalArgumentException("Category with this name already exists.");
            }

            Category newCategory= new Category();
            newCategory.setCategoryName(category.getCategoryName());
            Integer parentId = category.getParentId();
            if (parentId != null) {
                Category parent = categoryRepository.findById(parentId)
                        .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
                newCategory.setParent(parent);
            } else {
                newCategory.setParent(null);
            }
            categoryRepository.save(newCategory);
            return newCategory;

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

            Category existing = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));

            existing.setCategoryName(updatedCategory.getCategoryName());
            existing.setParent(updatedCategory.getParent());

            return categoryRepository.save(existing);

    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        if(categoryId==null)
        {
            throw new IllegalArgumentException("Category  id cannot be null");
        }

            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));

    }

    @Override
    public List<CategoryResponse> getAllCategories() {

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


    }


    @Override
    public void deleteCategory(Integer categoryId) {
        if(categoryId==null)
        {
            throw new IllegalArgumentException("Category  id cannot be null");
        }

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            categoryRepository.delete(category);

    }


    //Realted Products


    @Override
    public RelatedProduct createRelatedProduct(RelatedProduct relatedProduct) {

            return relatedProductRepo.save(relatedProduct);

    }

    @Override
    public RelatedProduct updateRelatedProduct(Integer id, RelatedProduct updated) {

            RelatedProduct existing = relatedProductRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Related product not found"));

            existing.setProduct(updated.getProduct());
            existing.setRelatedProduct(updated.getRelatedProduct());
            existing.setRelationshipType(updated.getRelationshipType());

            return relatedProductRepo.save(existing);

    }

    @Override
    public RelatedProduct getRelatedProductById(Integer id) {

            return relatedProductRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Related product not found"));

    }

    @Override
    public List<RelatedProduct> getAllRelatedProducts() {

            return relatedProductRepo.findAll();

    }

    @Override
    public void deleteRelatedProduct(Integer id) {

            RelatedProduct rp = relatedProductRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Related product not found"));
            relatedProductRepo.delete(rp);

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

        ProductDetail dto1 = pavMapper.toProductDetail(product);

        List<ProductImage> images = imageRepository.findByProductIdOrderByIsPrimary(productId);
        dto1.setImageUrls(pavMapper.mapImagesToUrls(images));

        List<ProductAttributeValue> pavs = productAttributeValueRepo.findByProductId(productId);
        dto1.setAttributes(pavMapper.mapAttributes(pavs));





      Optional<Discount> discount = productDiscountRepo.findDiscountDetailsByProductId(productId);
     Product product1= productRepo.findById(productId).orElseThrow(()->new IllegalArgumentException("product not found"));
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
   }else {
       dto1.setActiveDiscount(null);
   }
        List<Review> reviews = reviewRepo.findByProductId(productId);
        dto1.setReviews(pavMapper.toReviewResponseList(reviews));


        Double avgRating = reviewRepo.findAverageRating(productId);
        dto1.setAverageRating(avgRating != null ? avgRating : 0.0);

        List<RelatedProduct> related = relatedProductRepo.findActiveRelatedByProductId(productId);

        List<RelatedProductResponse> relatedProductResponses = related.stream().map(rp -> {
            Product p = rp.getRelatedProduct();

            BigDecimal discountPercent = getActiveDiscountPercent(p.getId());
            BigDecimal discountPrice = applyDiscount(p.getPrice(), discountPercent);

            RelatedProductResponse response = new RelatedProductResponse();
            response.setProductId(p.getId());
            response.setImageAdress(p.getImageAddress());
            response.setName(p.getName());
            response.setDescription(p.getDescription());
            response.setOriginalPrice(p.getPrice());
            response.setDiscountPercent(discountPercent);
            response.setDiscountPrice(discountPrice);
            response.setIsAvailable(p.getIsAvailable());
            Double avgRating1 = reviewRepo.findAverageRating(p.getId());
            response.setAverageRating(avgRating1 != null ? avgRating1 : 0.0);
            return response;
        }).collect(Collectors.toList());

        dto1.setRelatedProducts(relatedProductResponses);


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


        return dto1;
    }


    @Override
    public List<ProductList> getAllProductsAfterLogin(int userId, int page) {

        userRepo.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("user doesnot exists"));

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
            productList.setThumbnailUrl(p.getImageAddress());
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
        Product product1= productRepo.findById(productId).orElseThrow(()->new IllegalArgumentException("product not found"));
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


        }else {
            dto.setActiveDiscount(null);
        }



        //  Set reviews
        List<Review> reviews = reviewRepo.findByProductId(productId);
        dto.setReviews(pavMapper.toReviewResponseList(reviews));

        //  Set average rating
        Double avgRating = reviewRepo.findAverageRating(productId);
        dto.setAverageRating(avgRating != null ? avgRating : 0.0);

        // 9️⃣ Set related products manually
        List<RelatedProduct> related = relatedProductRepo.findActiveRelatedByProductId(productId);

        List<RelatedProductResponse> relatedProductResponses = related.stream().map(rp -> {
            Product p = rp.getRelatedProduct();

            BigDecimal discountPercent = getActiveDiscountPercent(p.getId());
            BigDecimal discountPrice = applyDiscount(p.getPrice(), discountPercent);

            RelatedProductResponse response = new RelatedProductResponse();
            response.setProductId(p.getId());
            response.setImageAdress(p.getImageAddress());
            response.setName(p.getName());
            response.setDescription(p.getDescription());
            response.setOriginalPrice(p.getPrice());
             response.setDiscountPercent(discountPercent);
             response.setDiscountPrice(discountPrice);
             response.setIsAvailable(p.getIsAvailable());
            Double avgRating1 = reviewRepo.findAverageRating(p.getId());
            response.setAverageRating(avgRating1 != null ? avgRating1 : 0.0);
            return response;
        }).collect(Collectors.toList());

        dto.setRelatedProducts(relatedProductResponses);





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

