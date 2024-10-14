package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, ShoppingCartId>{
	@Query("from ShoppingCart sc where sc.shoppingCartId.customerId = :cuid and sc.shoppingCartId.ingredientId = :ingredId")
	ShoppingCart findByCustomerIdAndIngredientId(@Param("cuid") Integer cuid, @Param("ingredId") Integer ingredId);

	@Query("from ShoppingCart sc where sc.shoppingCartId.customerId = :cuid")
	List<ShoppingCart> findByCustomerId(@Param("cuid") Integer customerid);

}
