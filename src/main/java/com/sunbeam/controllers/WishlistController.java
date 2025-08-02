package com.sunbeam.controllers;

import com.sunbeam.entities.Product;
import com.sunbeam.entities.User;
import com.sunbeam.entities.Wishlist;
import com.sunbeam.exceptions.ProductException;
import com.sunbeam.exceptions.UserException;
import com.sunbeam.exceptions.WishlistNotFoundException;
import com.sunbeam.services.ProductService;
import com.sunbeam.services.UserService;
import com.sunbeam.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws WishlistNotFoundException, ProductException, UserException {
        Product product = productService.findProductById(productId);
        User user = userService.findUserProfileByJwt(jwt);
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(user, product);
        return ResponseEntity.ok(updatedWishlist);
    }
}