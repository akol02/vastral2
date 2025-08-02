package com.sunbeam.services;

import com.sunbeam.entities.Cart;
import com.sunbeam.entities.CartItem;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.User;

public interface CartService {
    CartItem addCartItem(User user, Product product, String size, int quantity);
    Cart findUserCart(User user);
}