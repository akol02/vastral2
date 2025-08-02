package com.sunbeam.services.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.sunbeam.daos.ReviewRepository;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Review;
import com.sunbeam.entities.User;
import com.sunbeam.request.CreateReviewRequest;
import com.sunbeam.services.ReviewService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImage());
        
        product.getReviews().add(review);
        // Here you might want to recalculate product's average rating
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new Exception("You are not authorized to update this review.");
        }
        review.setReviewText(reviewText);
        review.setRating(rating);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new Exception("You are not authorized to delete this review.");
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("Review not found with id: " + reviewId));
    }
}