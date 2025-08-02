package com.sunbeam.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long id);
}