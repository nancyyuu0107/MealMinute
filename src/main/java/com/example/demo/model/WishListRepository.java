package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListRepository extends JpaRepository<WishListBean, WishListId> {

	@Query("select w from WishListBean w where w.wishListId.customerId = :customerId and w.wishListId.recipeId = :recipeId")
	WishListBean existCustomerAndRecipe(@Param("customerId") Integer customerId, @Param("recipeId") Integer recipeId);
	
	@Query("select w from WishListBean w where w.wishListId.customerId = :customerId")
	List<WishListBean> findByCutomerId(@Param("customerId")  Integer customerId);
	
	@Query("select w from WishListBean w where w.wishListId.recipeId = :recipeId")
	WishListBean findByRecipeId(@Param("recipeId") Integer recipeId);
}
