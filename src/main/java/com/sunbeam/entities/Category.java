package com.sunbeam.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    private Integer level;
    
    @Column(unique = true)
    private String categoryId;
    
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    @JsonIgnore
    private Category parentCategory;
}