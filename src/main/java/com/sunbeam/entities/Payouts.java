package com.sunbeam.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.sunbeam.models.PayoutsStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Payouts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="seller_id")
    private Seller seller;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PayoutsStatus status = PayoutsStatus.PENDING;

    private LocalDateTime date = LocalDateTime.now();
}