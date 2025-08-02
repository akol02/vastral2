package com.sunbeam.services.impl;

import com.sunbeam.daos.WishlistRepository;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.User;
import com.sunbeam.entities.Wishlist;
import com.sunbeam.exceptions.WishlistNotFoundException;
import com.sunbeam.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId());
        if (wishlist == null) {
            return createWishlist(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) throws WishlistNotFoundException {
        Wishlist wishlist = getWishlistByUserId(user);
        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product); // Toggle: remove if exists
        } else {
            wishlist.getProducts().add(product); // Add if not exists
        }
        return wishlistRepository.save(wishlist);
    }
}