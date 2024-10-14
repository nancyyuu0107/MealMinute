package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

	@Query("from OrderDetail od where od.orders.orderId = :oid")
	public List<OrderDetail> findByOrderId(@Param("oid") Integer orderId);
	
	@Query("from OrderDetail od where od.ingredient.ingredientId = :iid")
	public List<OrderDetail> findByIngredientId(@Param("iid") Integer ingredientId);
}
