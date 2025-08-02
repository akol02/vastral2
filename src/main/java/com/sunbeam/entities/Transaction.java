package com.sunbeam.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    private User customer;
    
    @OneToOne
    @JoinColumn(name="order_id")
    private Orders order;
    
    @ManyToOne
    @JoinColumn(name="seller_id")
    private Seller seller;
    
    private LocalDateTime date = LocalDateTime.now();
}