package com.sunbeam.services;

import com.sunbeam.entities.Product;
import com.sunbeam.entities.User;
import com.sunbeam.entities.Wishlist;
import com.sunbeam.exceptions.WishlistNotFoundException;

public interface WishlistService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product) throws WishlistNotFoundException;
}