package com.sunbeam.entities;

import java.util.HashSet;
import java.util.Set;
import com.sunbeam.models.PaymentMethod;
import com.sunbeam.models.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    private String paymentLinkId;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    @OneToMany
    private Set<Orders> orders = new HashSet<>();
}