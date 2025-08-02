package com.sunbeam.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Review;
import com.sunbeam.entities.User;
import com.sunbeam.request.CreateReviewRequest;
import com.sunbeam.response.ApiResponse;
import com.sunbeam.services.ProductService;
import com.sunbeam.services.ReviewService;
import com.sunbeam.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/reviews/product/{productId}")
    public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/reviews/product/{productId}")
    public ResponseEntity<Review> writeReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(req, user, product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.updateReview(reviewId, req.getReviewText(), req.getReviewRating(), user.getId());
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res = new ApiResponse("Review Deleted Successfully", true);
        return ResponseEntity.ok(res);
    }
}