package com.sunbeam.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}