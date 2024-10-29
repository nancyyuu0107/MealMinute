package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.IngredientBean;
import com.example.demo.service.IngredientService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class IngredientController {

	@Autowired
	private IngredientService ingreService;

	@PostMapping("/ingredient/info")
	public IngredientBean addIngredient(@RequestPart String ingredientJson,
			@RequestPart("imageFile") MultipartFile imageFile) throws IOException  {
		
		// new ObjectMapper().readValue(ingredientJson, IngredientBean.class) 
		// 是一個用於將 JSON 字串轉換為 Java 對象的操作。這個操作涉及到 Jackson 庫的 ObjectMapper 類。
		// ObjectMapper 是 Jackson 庫中的一個核心類，用於處理 JSON 數據。它提供了將 Java 對象與 JSON 數據相互轉換的功能。
		// ObjectMapper 可以將 JSON 字串解析為 Java 對象，也可以將 Java 對象序列化為 JSON 字串。
		
		// readValue 方法是 ObjectMapper 類中的一個方法，它用於將 JSON 字串轉換為 Java 對象。
		IngredientBean ingredient = new ObjectMapper().readValue(ingredientJson,  IngredientBean.class);
		// readValue(String content, Class<T> valueType) throws IOException
		// content：要轉換的 JSON 字符串。
		// valueType：將 JSON 轉換成的目標 Java 類的類型。
		
		return ingreService.saveIngredient(ingredient, imageFile);
	}
	
//	@PostMapping("/ingredient/info")
//	public IngredientBean addIngredient(@RequestPart IngredientBean ingredient,
//			@RequestPart("imageFile") MultipartFile imageFile) {
//		return ingreService.saveIngredient(ingredient, imageFile);
//	}

	@GetMapping("/ingredient/info/{id}")
	public IngredientBean showIngreInfo(@PathVariable Integer id) {
		return ingreService.findById(id);
	}

	@GetMapping("/ingredient/info")
	public List<IngredientBean> showAllIngre() {
		return ingreService.findAll();
	}

	@PutMapping("/ingredient/info")
	public String editIngre(@RequestPart String ingredientJson,
			@RequestPart("imageFile") MultipartFile imageFile) throws IOException {
		
		IngredientBean ingredient = new ObjectMapper().readValue(ingredientJson,  IngredientBean.class);
		
		boolean newInfo = ingreService.updateInfo(ingredient,imageFile);

		if (newInfo) {
			return "update success";
		}
		return "update failed";
	}

	@DeleteMapping("/ingredient/info/{id}")
	public String deleteIngreInfo(@PathVariable Integer id) {
		IngredientBean ingre = ingreService.findById(id);

		if (ingre != null) {
			ingreService.deleteIngreById(id);
			return "delete success";
		}
		return "delete failed";

	}

	// 依分類搜尋食材
	@GetMapping("/ingredient/info/category")
	public List<IngredientBean> showIngreByCategory(@RequestParam String category){
		return ingreService.findByCategory(category);
	}

	@GetMapping("/ingredient/info/commend")
	public List<IngredientBean> showRecommend(){
		return ingreService.findIngreByRecommend();
	}


}
