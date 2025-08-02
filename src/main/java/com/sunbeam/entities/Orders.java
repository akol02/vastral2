package com.sunbeam.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.sunbeam.models.OrderStatus;
import com.sunbeam.models.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`orders`") // Using backticks for the reserved keyword 'order'
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String orderId;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    private Long sellerId;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderitems = new ArrayList<>();
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="shipping_address_id")
    private Address shippingAddress;
    
    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();
    
    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer discount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    private int totalItem;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime deliveryDate;
}