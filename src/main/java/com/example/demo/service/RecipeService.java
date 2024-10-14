package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.demo.model.RecipeBean;
import com.example.demo.model.RecipeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepo;
	
	@Value("${upload.dir}")
	private String uploadDir;
	
	@Value("${domain.url}")
	private String domainUrl;
	
	public RecipeBean saveRecipe(RecipeBean recipe) {
		return recipeRepo.save(recipe);
	}
	
	public RecipeBean findById(Integer id) {
		Optional<RecipeBean> optional = recipeRepo.findById(id);
		
		if(optional.isPresent()) {
			RecipeBean recipeBean = optional.get();
			return recipeBean;
		}
		return null;
	}
	
	public List<RecipeBean> findAll(){
		return recipeRepo.findAll();
	}
	
	public boolean updateData(RecipeBean recipeBean) {
		
		recipeBean.setUpdateTime(new Date());
		
		RecipeBean update = recipeRepo.save(recipeBean);
		
		if(update != null) {
			return true;
		}
		return false;
		
	}
	
	public void deleteRecipeById(Integer id) {
			recipeRepo.deleteById(id);
	}
	
	
	public List<RecipeBean> findByCateory(String category){
		List<RecipeBean> list = recipeRepo.findByCategory(category);
		
		if(list != null) {
			return list;
		}
		return null;
	}
	

	public List<RecipeBean> findLatestRecipes() {
		List<RecipeBean> latestRecipes = recipeRepo.findLatestRecipe(PageRequest.of(0, 4));
		
		if(latestRecipes != null) {
			return latestRecipes;
		}
		return null;
	}
}
