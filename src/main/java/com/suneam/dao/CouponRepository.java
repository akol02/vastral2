package com.suneam.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.entities.Coupon;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
    Coupon findByCode(String couponCode);
}
