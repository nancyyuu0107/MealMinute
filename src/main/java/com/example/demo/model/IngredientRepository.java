package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IngredientRepository extends JpaRepository<IngredientBean, Integer> {

	@Query("select ingre from IngredientBean ingre where ingre.deleteStatus = 1")
	List<IngredientBean> findAll();
	
	List<IngredientBean> findByCategory(String category);	
	
	@Query("select ingre from IngredientBean ingre where ingre.recommend = true")
	List<IngredientBean> findByRecommend();
}
