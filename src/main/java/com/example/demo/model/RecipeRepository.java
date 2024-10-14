package com.example.demo.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<RecipeBean, Integer> {

	List<RecipeBean> findByCategory(String category);
	
	@Query("select r from RecipeBean r order by r.recipeId desc")
	List<RecipeBean> findLatestRecipe(Pageable pageable);
}
