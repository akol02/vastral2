package com.sunbeam.entities;

import com.sunbeam.models.HomeCategorySection;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String image;
    private String categoryId;
    
    @Enumerated(EnumType.STRING)
    private HomeCategorySection section;
}