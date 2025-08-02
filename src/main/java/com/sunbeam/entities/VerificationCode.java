package com.sunbeam.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String otp;
    private String email;
    
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    
    @OneToOne
    @JoinColumn(name="seller_id")
    private Seller seller;
}