package com.sunbeam.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.HomeCategory;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {}