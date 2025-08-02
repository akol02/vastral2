package com.sunbeam.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {}