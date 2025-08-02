package com.sunbeam.daos;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sunbeam.entities.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(Long userId);
    List<Orders> findBySellerIdOrderByOrderDateDesc(Long sellerId);
    List<Orders> findBySellerIdAndOrderDateBetween(Long sellerId, LocalDateTime startDate, LocalDateTime endDate);
}