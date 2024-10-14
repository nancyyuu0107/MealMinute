package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.RecipeImgBean;
import com.example.demo.service.RecipeImgService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin
public class RecipeImgController {

	@Autowired
	private RecipeImgService recipeImgService;

//	@PostMapping("/recipeImg")
//	public RecipeImgBean addRecipeImg(@RequestParam Integer recipeId,
//			@RequestPart("recipeImg") MultipartFile imageFile) {
//		return recipeImgService.saveRecipeImg(recipeId, imageFile);
//	}
	
	
	@PostMapping("/recipeImg")
	public List<RecipeImgBean> addRecipeImg(@RequestParam Integer recipeId,
	        @RequestPart("recipeImg") List<MultipartFile> imageFiles) {
	    List<RecipeImgBean> savedImages = new ArrayList<>();
	    for (MultipartFile imageFile : imageFiles) {
	        savedImages.add(recipeImgService.saveRecipeImg(recipeId, imageFile));
	    }
	    return savedImages; // 返回保存的圖片信息
	}

	@PutMapping("/recipeImg")
	public String editRecipeImg(@RequestParam Integer imgId, @RequestParam Integer recipeId,
			@RequestPart("recipeImg") MultipartFile imageFile) {

		boolean updateInfo = recipeImgService.updateRecipeImg(imgId, recipeId, imageFile);

		if (updateInfo) {
			return "update success";
		}
		return "update failed";
	}
	
	@GetMapping("/recipeImg")
	public List<RecipeImgBean> selectImgByRecipe(@RequestParam Integer recipeId){
		return recipeImgService.findImageByRecipeId(recipeId);
	}

}
