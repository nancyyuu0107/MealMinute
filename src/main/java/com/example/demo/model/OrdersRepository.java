package com.example.demo.model;

import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	@Query("from Orders o where o.customer.customerId =:ccc")
	public List<Orders> findByCustomerId(@Param("ccc") Integer customerId);

	@Query("from Orders o where o.orderStatus =:os and o.customer.customerId =:ccc")
	public List<Orders> findByStatusAndCustomerId(@Param("os") String status, @Param("ccc") Integer customerId);
	
	@Query("from Orders o where o.orderStatus =:os")
	public List<Orders> findByStatus(@Param("os") String status);



}
