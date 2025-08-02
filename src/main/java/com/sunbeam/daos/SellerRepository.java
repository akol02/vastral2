package com.sunbeam.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.Seller;
import com.sunbeam.models.AccountStatus;
import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}