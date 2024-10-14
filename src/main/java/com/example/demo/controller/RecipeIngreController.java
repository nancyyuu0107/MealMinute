package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RecipeIngreDto;
import com.example.demo.model.RecipeIngredientBean;
import com.example.demo.service.RecipeIngreService;

@RestController
@CrossOrigin
public class RecipeIngreController {
	
	@Autowired
	private RecipeIngreService riService;
	
	@PostMapping("/recipeIngredintsAmout")
	public ResponseEntity<Object> addIngreToRecipe(@RequestBody RecipeIngreDto dto){
		RecipeIngredientBean result = riService.saveIngreToRecipe(dto);
		
		if(result == null) {
			return ResponseEntity.ok().body("食材已存在於該食譜中，未進行新增操作。");
		}
		
		return ResponseEntity.ok(result);
		
	}
	
	// 透過食譜Id搜尋此食譜所需的所有食材
	@GetMapping("/recipeIngredintsAmout/{recipeId}")
	public List<RecipeIngredientBean> showNeedsIngre(@PathVariable Integer recipeId) {
		return riService.findNeedsIngre(recipeId);
	}
	
	// 透過食材及食譜Id搜尋此食譜所需的特定單一食材
	@GetMapping("/recipeIngredintsAmout")
	public RecipeIngredientBean showspecificIngre(@RequestParam Integer recipeId, @RequestParam Integer ingreId) {
		return riService.findByRecipeIdAndIngreId(recipeId, ingreId);
	}
	
	// 透過食材Id找到所有相關的食譜
	@GetMapping("/recipeIngredintsAmout/commonRecipe/{ingreId}")
	public List<RecipeIngredientBean> showCommonRecipe(@PathVariable Integer ingreId) {
		return riService.findCommonRecipe(ingreId);
	}
	
	
	// 修改
	@PutMapping("/recipeIngredintsAmout")
	public String editUnit(@RequestBody RecipeIngreDto dto){
		boolean updateUnit = riService.updateRecipesIngreUnit(dto);
		
		if(updateUnit) {
			return "update success";
		}
		return "update failed";
		
	}
	
	@DeleteMapping("/recipeIngredintsAmout")
	public String deleteRecipeIngre(@RequestParam Integer recipeId, @RequestParam Integer ingreId) {
		RecipeIngredientBean data = riService.findByRecipeIdAndIngreId(recipeId, ingreId);
		
		if(data != null) {
			riService.deleteUnit(data);
			return "delete success";
		}
		return "delete failed";
	}

	
	
}
