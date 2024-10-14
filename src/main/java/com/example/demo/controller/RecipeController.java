package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.RecipeBean;
import com.example.demo.service.RecipeService;

@RestController
@CrossOrigin
public class RecipeController {
	
	@Autowired
	private RecipeService recipeService;
	
	@PostMapping("/recipe/info")
	public RecipeBean addRecipe(@RequestBody RecipeBean recipeInfo) {
		return recipeService.saveRecipe(recipeInfo);
	}
	
	@GetMapping("/recipe/info/{id}")
	public RecipeBean showRecipe(@PathVariable Integer id) {
		return recipeService.findById(id);
	}
	
	@GetMapping("/recipe/info")
	public List<RecipeBean> showAllRecipeImg(){
		return recipeService.findAll();
	}

	@PutMapping("/recipe/info")
	public String editRecipe(@RequestBody RecipeBean recipe) {
		boolean updateRecipe = recipeService.updateData(recipe);
		if(updateRecipe) {
			return "update success";
		}
		return "update failed";
	}
	
	@DeleteMapping("/recipe/info/{id}")
	public String deleteRecipe(@PathVariable Integer id) {
		RecipeBean recipe = recipeService.findById(id);
		
		if(recipe != null) {
			recipeService.deleteRecipeById(id);
			return "delete success";
		}
		return "delete failed";
	}
	
	@GetMapping("recipe/info/category")
	public List<RecipeBean> showRecipeByCategory(@RequestParam String category){
		return recipeService.findByCateory(category);
	}
	
	
	// 找到最新4筆食譜
	@GetMapping("recipe/info/latest")
	public List<RecipeBean> showLatestRecipesBeans() {
		return recipeService.findLatestRecipes();
	}
}
