package com.sunbeam.daos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}