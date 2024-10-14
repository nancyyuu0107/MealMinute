package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.WishListDto;
import com.example.demo.model.CustomerBean;
import com.example.demo.model.CustomerBeanRepository;
import com.example.demo.model.RecipeBean;
import com.example.demo.model.RecipeRepository;
import com.example.demo.model.WishListBean;
import com.example.demo.model.WishListId;
import com.example.demo.model.WishListRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class WishListService {

	@Autowired
	private WishListRepository wishListRepo;

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private CustomerBeanRepository customerBeanRepo;

	public WishListBean savefavoriteItem(WishListDto dto) {
		
		if (wishListRepo.existCustomerAndRecipe(dto.getCustomerId(), dto.getRecipeId()) != null) {
			// 如果存在，則直接返回 null
			return null;
		}
		
		Optional<CustomerBean> customer = customerBeanRepo.findById(dto.getCustomerId());
		if (customer.isEmpty()) {
			throw new EntityNotFoundException("客戶不存在");
		}
		CustomerBean customerBean = customer.get();
		
		Optional<RecipeBean> recipe = recipeRepo.findById(dto.getRecipeId());
		if(recipe.isEmpty()) {
			throw new EntityNotFoundException("食譜不存在");
		}
		RecipeBean recipeBean = recipe.get();
		
		// 設定複合主鍵
		WishListId wishListId = new WishListId();
		wishListId.setCustomerId(dto.getCustomerId());
		wishListId.setRecipeId(dto.getRecipeId());
		
		// 把複合主鍵和其他屬性放入 wishList
		WishListBean wishListBean = new WishListBean();
		wishListBean.setWishListId(wishListId);
		wishListBean.setCustomer(customerBean);
		wishListBean.setRecipe(recipeBean);
		
		return wishListRepo.save(wishListBean);
	}
	
	// 透過 customerId 找到所有收藏的食譜
	public List<WishListBean> findFavoriteItemBycustomerId(Integer customerId) {
		return wishListRepo.findByCutomerId(customerId);
	}
	
	
	public boolean findItemInWishList(Integer customerId, Integer recipeId) {
		WishListBean existBean = wishListRepo.existCustomerAndRecipe(customerId, recipeId);
		
		// 如果 existBean 不為 null，表示存在，返回 true，否則返回 false
	    return existBean != null;
	}
	
	
	public String deleteFavoriteItemByRecipeId(WishListDto dto) {
		WishListBean recipe = wishListRepo.existCustomerAndRecipe(dto.getCustomerId(),dto.getRecipeId());
		
		if(recipe != null) {
			wishListRepo.delete(recipe);
			return "delete success";
		}
		return "delete failed";
	}

}
