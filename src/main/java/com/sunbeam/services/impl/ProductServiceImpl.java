package com.sunbeam.services.impl;

import com.sunbeam.daos.CategoryRepository;
import com.sunbeam.daos.ProductRepository;
import com.sunbeam.entities.Category;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Seller;
import com.sunbeam.exceptions.ProductException;
import com.sunbeam.request.CreateProductRequest;
import com.sunbeam.services.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) throws ProductException {
        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Category category1 = findOrCreateCategory(req.getCategory(), 1, null);
        Category category2 = findOrCreateCategory(req.getCategory2(), 2, category1);
        Category category3 = findOrCreateCategory(req.getCategory3(), 3, category2);

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountPercent(discountPercentage);
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setCreatedAt(LocalDateTime.now());
        product.setQuantity(10); // Default quantity

        return productRepository.save(product);
    }

    private Category findOrCreateCategory(String categoryId, int level, Category parent) {
        if (categoryId == null || categoryId.isBlank()) {
            return parent; // or throw an exception if a level is required
        }
        Category category = categoryRepository.findByCategoryId(categoryId);
        if (category == null) {
            category = new Category();
            category.setCategoryId(categoryId);
            category.setLevel(level);
            category.setParentCategory(parent);
            category.setName(categoryId.replace("_", " "));
            return categoryRepository.save(category);
        }
        return category;
    }

    private int calculateDiscountPercentage(double mrpPrice, double sellingPrice) {
        if (mrpPrice <= 0) {
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        return (int) Math.round((discount / mrpPrice) * 100);
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product reqProduct) throws ProductException {
        Product product = findProductById(productId);
        // Update fields if they are provided in the request
        if (reqProduct.getTitle() != null) product.setTitle(reqProduct.getTitle());
        if (reqProduct.getDescription() != null) product.setDescription(reqProduct.getDescription());
        if (reqProduct.getMrpPrice() > 0) product.setMrpPrice(reqProduct.getMrpPrice());
        if (reqProduct.getSellingPrice() > 0) product.setSellingPrice(reqProduct.getSellingPrice());
        if (reqProduct.getColor() != null) product.setColor(reqProduct.getColor());
        if (reqProduct.getSizes() != null) product.setSizes(reqProduct.getSizes());
        if (reqProduct.getImages() != null && !reqProduct.getImages().isEmpty()) product.setImages(reqProduct.getImages());
        if (reqProduct.getQuantity() >= 0) product.setQuantity(reqProduct.getQuantity());

        product.setDiscountPercent(calculateDiscountPercentage(product.getMrpPrice(), product.getSellingPrice()));
        
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id: " + id));
    }

    @Override
    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProduct(String category, String brand, String color, String size,
                                       Integer minPrice, Integer maxPrice, Integer minDiscount,
                                       String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null && !category.isBlank()) {
                Join<Product, Category> categoryJoin = root.join("category");
                Predicate p1 = cb.equal(categoryJoin.get("categoryId"), category);
                Predicate p2 = cb.equal(categoryJoin.get("parentCategory").get("categoryId"), category);
                Predicate p3 = cb.equal(categoryJoin.get("parentCategory").get("parentCategory").get("categoryId"), category);
                predicates.add(cb.or(p1, p2, p3));
            }
            if (color != null && !color.isBlank()) {
                predicates.add(cb.equal(root.get("color"), color));
            }
            if (size != null && !size.isBlank()) {
                predicates.add(cb.like(root.get("Sizes"), "%" + size + "%"));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null && stock.equals("in_stock")) {
                predicates.add(cb.greaterThan(root.get("quantity"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sorting = Sort.unsorted();
        if (sort != null && !sort.isBlank()) {
            switch (sort) {
                case "price_low":
                    sorting = Sort.by("sellingPrice").ascending();
                    break;
                case "price_high":
                    sorting = Sort.by("sellingPrice").descending();
                    break;
            }
        }
        
        Pageable pageable = PageRequest.of(pageNumber, 10, sorting);
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}