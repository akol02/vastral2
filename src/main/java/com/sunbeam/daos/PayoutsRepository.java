package com.sunbeam.daos;

import com.sunbeam.entities.Payouts;
import com.sunbeam.models.PayoutsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PayoutsRepository extends JpaRepository<Payouts, Long> {
    List<Payouts> findPayoutsBySellerId(Long sellerId);
    List<Payouts> findAllByStatus(PayoutsStatus status);
}