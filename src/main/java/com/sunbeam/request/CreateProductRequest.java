package com.sunbeam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private String brand;
    private String color;
    private List<String> images;
    private String category;    // Corresponds to level 1
    private String category2;   // Corresponds to level 2
    private String category3;   // Corresponds to level 3
    private String sizes;
}