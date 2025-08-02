package com.sunbeam.entities;

import com.sunbeam.models.AccountStatus;
import com.sunbeam.models.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String sellerName;
    private String mobile;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    
    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();
    
    @Embedded
    private BankDetails bankDetails = new BankDetails();
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pickup_address_id")
    private Address pickupAddress;
    
    private String GSTIN;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    private boolean isEmailVerified = false;
    
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}