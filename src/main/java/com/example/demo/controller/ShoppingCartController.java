package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.model.CustomerBean;
import com.example.demo.model.CustomerBeanRepository;
import com.example.demo.model.IngredientBean;
import com.example.demo.model.IngredientRepository;
import com.example.demo.model.RecipeBean;
import com.example.demo.model.RecipeIngredientBean;
import com.example.demo.model.ShoppingCart;
import com.example.demo.service.IngredientService;
import com.example.demo.service.RecipeIngreService;
import com.example.demo.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService scService;

	@Autowired
	private CustomerBeanRepository cuRepo;

	@Autowired
	private RecipeIngreService riService;

	@PostMapping("/shoppingcart/add")
	public ShoppingCart addToCart(@RequestBody ShoppingCartDto dto) throws JSONException {
		// @RequestParam Integer ingredientId, @RequestParam Integer customerId
		// @RequestBody String json

//		JSONObject obj = new JSONObject(json);
//		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
//		Integer ingredientId = obj.isNull("ingredientId") ? null : obj.getInt("ingredientId");

//		ShoppingCartDto dto = new ShoppingCartDto();
//		dto.setCustomerId(customerId);
//		dto.setIngredientId(ingredientId);
//		dto.setQuantity(1);

		return scService.addToCart(dto);
	}

	@GetMapping("/shoppingcart/find")
	public String showCartByCustomerId(@RequestParam Integer customerId) throws JSONException {
		Optional<CustomerBean> optional = cuRepo.findById(customerId);
		CustomerBean cu = new CustomerBean();
		if (optional.isPresent()) {
			cu = optional.get();
		} else {
			return null;
		}
		Integer loginId = cu.getCustomerId();
		List<ShoppingCart> list = scService.findByCustomerId(loginId);
		JSONObject responseJson = new JSONObject();
		JSONArray array = new JSONArray();
		for (ShoppingCart sc : list) {
			JSONObject item = new JSONObject();
			item.put("ingredientId", sc.getIngredient().getIngredientId());
			item.put("ingredientName", sc.getIngredient().getIngredientName());
			item.put("ingredientPrice", sc.getIngredient().getPrice());
			item.put("ingredientPic", sc.getIngredient().getIngredientPicUrl());
			item.put("ingredientQuantity", sc.getQuantity());
			item.put("checkCart", false);
			array.put(item);
		}
		responseJson.put("list", array);
		return responseJson.toString();
	}

	@GetMapping("/shoppingcart/count")
	public Integer count(@RequestParam Integer customerId) {
		if (customerId == null) {
			return null;
		}
		Optional<CustomerBean> optional = cuRepo.findById(customerId);
		CustomerBean cu = new CustomerBean();
		if (optional.isPresent()) {
			cu = optional.get();
		}
		Integer loginId = cu.getCustomerId();
		List<ShoppingCart> list = scService.findByCustomerId(loginId);
		Integer count = 0;
		for (ShoppingCart sc : list) {
			count++;
		}
		return count;
	}

	@PutMapping("/shoppingcart/addOneVol")
	public String addOneVolToCart(@RequestBody ShoppingCartDto dto) throws JSONException {
//		JSONObject obj = new JSONObject(json);
//		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
//		Integer ingredientId = obj.isNull("ingredientId") ? null : obj.getInt("ingredientId");

		scService.addToCart(dto);
		return "增加一個成功";
	}

	@PutMapping("/shoppingcart/minusOneVol")
	public String minusOneVolToCart(@RequestBody String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer ingredientId = obj.isNull("ingredientId") ? null : obj.getInt("ingredientId");

		Optional<CustomerBean> optional = cuRepo.findById(customerId);
		CustomerBean cu = new CustomerBean();
		if (optional.isPresent()) {
			cu = optional.get();
		}
		scService.minusToCart(cu.getCustomerId(), ingredientId);
		return "減少一個成功";
	}

	@DeleteMapping("/shoppingcart/deleteCart")
	public String deleteCart(@RequestBody String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer ingredientId = obj.isNull("ingredientId") ? null : obj.getInt("ingredientId");

		System.out.println(customerId);
		System.out.println(ingredientId);
		Optional<CustomerBean> optional = cuRepo.findById(customerId);
		CustomerBean cu = new CustomerBean();
		if (optional.isPresent()) {
			cu = optional.get();
		} else {
			return "刪除失敗";
		}

		if (scService.deleteCart(cu.getCustomerId(), ingredientId)) {
			return "刪除成功";
		}
		return "刪除失敗";
	}

	// 將食譜需要的食材一鍵加入購物車
	@PostMapping("/shoppingcart/addrecipe")
	public String addRecipe(@RequestBody String json) throws JSONException {
		// RecipeBean recipe,
		// @RequestParam Integer recipeId, @RequestParam Integer customerId
		JSONObject obj = new JSONObject(json);
		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer recipeId = obj.isNull("recipeId") ? null : obj.getInt("recipeId");

		List<ShoppingCart> list2 = scService.findByCustomerId(customerId);

		List<RecipeIngredientBean> list = riService.findNeedsIngre(recipeId);
		for (RecipeIngredientBean ri : list) {
			for (ShoppingCart sc : list2) {
				if (ri.getIngredient().getIngredientId() == sc.getShoppingCartId().getIngredientId()) {
					if (ri.getIngredientVolume() + sc.getQuantity() > ri.getIngredient().getStockQuantity()) {
						return "所需食材庫存不足，新增失敗";
					}
				}
			}
			if(!(list2.contains(ri))) {
				if(ri.getIngredientVolume()>ri.getIngredient().getStockQuantity()) {
					return null;
				}
			}
		}

		for (RecipeIngredientBean ri : list) {
			scService.addRecipeToCart(ri.getIngredient().getIngredientId(), customerId, ri.getIngredientVolume());
		}
		return "新增成功";
	}

	@PostMapping("/shoppingcart/addingredientquantity")
	public ShoppingCart addIngredientQuantity(@RequestBody ShoppingCartDto dto) {
//		@RequestBody String json

//		JSONObject obj = new JSONObject(json);
//		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
//		Integer ingredientId = obj.isNull("ingredientId") ? null : obj.getInt("ingredientId");
//		Integer quantity = obj.isNull("quantity") ? null : obj.getInt("quantity");
//		@RequestParam Integer customerId, @RequestParam Integer quantity, @RequestParam Integer ingredientId
		return scService.addIngredirntQuantity(dto);

	}
}
