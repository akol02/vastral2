package com.sunbeam.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunbeam.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {}