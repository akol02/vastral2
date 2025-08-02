package com.sunbeam.services;

import java.util.List;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Review;
import com.sunbeam.entities.User;
import com.sunbeam.request.CreateReviewRequest;

public interface ReviewService {
    Review createReview(CreateReviewRequest req, User user, Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;
    void deleteReview(Long reviewId, Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;
}