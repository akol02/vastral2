package com.sunbeam.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByUserId(Long userId);
}