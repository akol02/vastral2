package com.sunbeam.services;

import com.sunbeam.entities.Product;
import com.sunbeam.entities.Seller;
import com.sunbeam.exceptions.ProductException;
import com.sunbeam.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest req, Seller seller) throws ProductException;
    void deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long id) throws ProductException;
    List<Product> searchProduct(String query);
    Page<Product> getAllProduct(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber);
    List<Product> getProductBySellerId(Long sellerId);
}