package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.WishListDto;
import com.example.demo.model.WishListBean;
import com.example.demo.service.WishListService;

@RestController
@CrossOrigin
public class WishListController {
	
	@Autowired
	private WishListService wishService;

	@PostMapping("/wishList")
	public ResponseEntity<Object> addToWishList(@RequestBody WishListDto dto) {
		WishListBean result = wishService.savefavoriteItem(dto);
		
		if(result == null) {
			return ResponseEntity.ok().body("食譜已在收藏清單中。");
		}
		return ResponseEntity.ok("已加入收藏");
	}
	
	// 透過 customerId 找到所有收藏的食譜
	@GetMapping("/wishList/{customerId}")
	public List<WishListBean> showWishList(@PathVariable Integer customerId) {
		return wishService.findFavoriteItemBycustomerId(customerId);
	}
	
	@GetMapping("/wishList/exist")
	public ResponseEntity<Object> alreadyInWishList(@RequestParam Integer customerId, @RequestParam Integer recipeId) {
		boolean itemInWishList = wishService.findItemInWishList(customerId, recipeId);
		
		if (itemInWishList) {
	        return ResponseEntity.ok("食譜有在收藏中");
	    }
	    return ResponseEntity.ok("食譜不在收藏中");
	}
	
	@DeleteMapping("/wishList")
	public ResponseEntity<Object> deleteFavoriteItem(@RequestBody WishListDto dto) {
		String deleteMessage = wishService.deleteFavoriteItemByRecipeId(dto);
		
		if("delete success" == deleteMessage) {
			return ResponseEntity.ok().body("移除成功");
		}
		return ResponseEntity.ok().body("移除失敗");
		
	}
}
