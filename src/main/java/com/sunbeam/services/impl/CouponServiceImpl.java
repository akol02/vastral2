package com.sunbeam.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.sunbeam.daos.CartRepository;
import com.sunbeam.daos.CouponRepository;
import com.sunbeam.daos.UserRepository;
import com.sunbeam.entities.Cart;
import com.sunbeam.entities.Coupon;
import com.sunbeam.entities.User;
import com.sunbeam.services.CouponService;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (coupon == null) {
            throw new Exception("Coupon code is not valid.");
        }
        if (user.getUsedCoupons().contains(coupon)) {
            throw new Exception("Coupon has already been used.");
        }
        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new Exception("Order value must be at least " + coupon.getMinimumOrderValue() + " to apply this coupon.");
        }
        if (!coupon.isActive() || LocalDate.now().isBefore(coupon.getValidityStartDate()) || LocalDate.now().isAfter(coupon.getValidityEndDate())) {
            throw new Exception("Coupon is not active or has expired.");
        }

        user.getUsedCoupons().add(coupon);
        userRepository.save(user);

        double discountValue = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100.0;
        cart.setCouponDiscount(discountValue);
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountValue);
        cart.setCouponCode(code);

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new Exception("Coupon code not found.");
        }

        user.getUsedCoupons().remove(coupon);
        userRepository.save(user);

        Cart cart = cartRepository.findByUserId(user.getId());
        // Recalculate total selling price without the coupon discount
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + cart.getCouponDiscount());
        cart.setCouponDiscount(0);

        return cartRepository.save(cart);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon getCouponById(Long couponId) throws Exception {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new Exception("Coupon not found with id: " + couponId));
    }
}