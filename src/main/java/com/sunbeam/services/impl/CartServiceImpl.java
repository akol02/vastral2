package com.sunbeam.services.impl;

import org.springframework.stereotype.Service;
import com.sunbeam.daos.CartItemRepository;
import com.sunbeam.daos.CartRepository;
import com.sunbeam.entities.Cart;
import com.sunbeam.entities.CartItem;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.User;
import com.sunbeam.services.CartService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);
            cartItem.setCart(cart);
            cartItem.setSellingPrice(quantity * product.getSellingPrice());
            cartItem.setMrpPrice(quantity * product.getMrpPrice());
            return cartItemRepository.save(cartItem);
        }
        // If item already exists, you might want to update its quantity
        // For now, we just return the existing item
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        
        int totalMrpPrice = 0;
        int totalSellingPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalMrpPrice += cartItem.getMrpPrice();
            totalSellingPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalMrpPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalSellingPrice - cart.getCouponDiscount()); // Apply coupon discount
        cart.setDiscount(calculateDiscountPercentage(totalMrpPrice, totalSellingPrice));
        
        return cartRepository.save(cart);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0) {
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) Math.round(discountPercentage);
    }
}