package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RecipeIngreDto;
import com.example.demo.model.IngredientBean;
import com.example.demo.model.IngredientRepository;
import com.example.demo.model.RecipeBean;
import com.example.demo.model.RecipeIngreRepository;
import com.example.demo.model.RecipeIngredientBean;
import com.example.demo.model.RecipeIngredientId;
import com.example.demo.model.RecipeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecipeIngreService {

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private IngredientRepository ingreRepo;

	@Autowired
	private RecipeIngreRepository recipeIngreRepo;

	public RecipeIngredientBean saveIngreToRecipe(RecipeIngreDto dto) {
		// 檢查組合是否已經存在於資料庫中，避免重複保存相同的關聯數據。
		if (recipeIngreRepo.existsByRecipeIngreId_RecipeIdAndRecipeIngreId_IngredientId(dto.getRecipeId(),
				dto.getIngredientId())) {
			// 如果存在，則直接返回 null，避免重複的數據輸入
			return null;
		}

		Optional<RecipeBean> recipe = recipeRepo.findById(dto.getRecipeId());
		if (recipe.isEmpty()) {
			throw new EntityNotFoundException("食譜不存在");
		}
		RecipeBean recipeBean = recipe.get();

		Optional<IngredientBean> ingre = ingreRepo.findById(dto.getIngredientId());
		if (ingre.isEmpty()) {
			throw new EntityNotFoundException("食材不存在");
		}
		IngredientBean ingredientBean = ingre.get();

		// 設定複合主鍵
		RecipeIngredientId recipeIngreId = new RecipeIngredientId();
		recipeIngreId.setRecipeId(dto.getRecipeId());
		recipeIngreId.setIngredientId(dto.getIngredientId());

		// 把複合主鍵和其他屬性放入 recipeIngre
		RecipeIngredientBean recipeIngre = new RecipeIngredientBean();
		recipeIngre.setRecipeIngreId(recipeIngreId);
		recipeIngre.setRecipe(recipeBean);
		recipeIngre.setIngredient(ingredientBean);
		recipeIngre.setIngredientUnit(dto.getIngredientUnit());
		recipeIngre.setIngredientVolume(dto.getIngredientVolume());

		return recipeIngreRepo.save(recipeIngre);

	}

	// 透過食譜Id搜尋此食譜所需的所有食材
	public List<RecipeIngredientBean> findNeedsIngre(Integer recipeid) {
		return recipeIngreRepo.findByRecipeId(recipeid);
	}
	
	// 透過食材Id搜尋相關的食譜
	public List<RecipeIngredientBean> findCommonRecipe(Integer IngreId) {
		return recipeIngreRepo.findByIngreId(IngreId);
	}

	// 透過食材及食譜Id搜尋此食譜所需的特定單一食材
	public RecipeIngredientBean findByRecipeIdAndIngreId(Integer recipeId, Integer ingreId) {
		RecipeIngredientBean bean = recipeIngreRepo.findByRecipeIdAndIngreId(recipeId, ingreId);

		if (bean != null) {
			return bean;
		}
		return null;
	}

	// 修改數量
	public boolean updateRecipesIngreUnit(RecipeIngreDto dto) {

		RecipeIngredientBean bean = recipeIngreRepo.findByRecipeIdAndIngreId(dto.getRecipeId(), dto.getIngredientId());

		if (bean == null) {
			return false;
		}

		bean.setIngredientUnit(dto.getIngredientUnit());
		bean.setIngredientVolume(dto.getIngredientVolume());

		try {
			RecipeIngredientBean updateUnit = recipeIngreRepo.save(bean);
			return updateUnit != null;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

	}

	public void deleteUnit(RecipeIngredientBean data) {
		
		recipeIngreRepo.delete(data);



	}

}
