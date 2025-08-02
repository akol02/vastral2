package com.sunbeam.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.Cart;
import com.sunbeam.entities.CartItem;
import com.sunbeam.entities.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}