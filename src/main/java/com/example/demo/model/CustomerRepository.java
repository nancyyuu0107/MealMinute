package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerBean, Integer>{
	
	CustomerBean findByEmail(String email);
}
