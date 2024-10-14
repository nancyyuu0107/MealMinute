package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.model.CustomerBean;
import com.example.demo.model.CustomerBeanRepository;
import com.example.demo.model.IngredientBean;
import com.example.demo.model.IngredientRepository;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.ShoppingCartId;
import com.example.demo.model.ShoppingCartRepository;

@Service
@Transactional
public class ShoppingCartService {
	@Autowired
	private ShoppingCartRepository shoppingCartRepo;

	@Autowired
	private CustomerBeanRepository cuRepo;

	@Autowired
	private IngredientRepository ingredRepo;

	@Transactional
	public ShoppingCart addToCart(ShoppingCartDto dto) {

		Integer customerId = dto.getCustomerId();
		Integer ingredientId = dto.getIngredientId();

		ShoppingCart dbshoppingcart = shoppingCartRepo.findByCustomerIdAndIngredientId(customerId, ingredientId);
		if (dbshoppingcart != null) {
			// 若超過食材庫存數量增加則不能再增加
			Integer scq = dbshoppingcart.getQuantity();
			Integer stockQuantity = dbshoppingcart.getIngredient().getStockQuantity();
			if (scq >= stockQuantity) {
				return dbshoppingcart;
			}

			dbshoppingcart.setQuantity(dbshoppingcart.getQuantity() + 1);
			return dbshoppingcart;
		}

		Optional<CustomerBean> optional1 = cuRepo.findById(customerId);
		CustomerBean customer = optional1.get();

		Optional<IngredientBean> optional2 = ingredRepo.findById(ingredientId);
		IngredientBean ingredient = optional2.get();

		ShoppingCartId shoppingcartid = new ShoppingCartId();
		shoppingcartid.setCustomerId(customerId);
		shoppingcartid.setIngredientId(ingredientId);

		ShoppingCart shoppingcart = new ShoppingCart();
		shoppingcart.setShoppingCartId(shoppingcartid);
		shoppingcart.setCustomer(customer);
		shoppingcart.setIngredient(ingredient);
		shoppingcart.setQuantity(1);

		return shoppingCartRepo.save(shoppingcart);

	}

	public List<ShoppingCart> findByCustomerId(Integer customerId) {
		return shoppingCartRepo.findByCustomerId(customerId);
	}

	@Transactional
	public ShoppingCart minusToCart(Integer customerId, Integer ingredientId) {
		ShoppingCart dbshoppingcart = shoppingCartRepo.findByCustomerIdAndIngredientId(customerId, ingredientId);
//		if ((dbshoppingcart.getQuantity() - 1) == 0) {
//			shoppingCartRepo.delete(dbshoppingcart);
//			return dbshoppingcart;
//		}

		if (dbshoppingcart != null) {
			dbshoppingcart.setQuantity(dbshoppingcart.getQuantity() - 1);
		}
		return dbshoppingcart;
	}

	@Transactional
	public boolean deleteCart(Integer customerId, Integer ingredientId) {
		ShoppingCart dbshoppingcart = shoppingCartRepo.findByCustomerIdAndIngredientId(customerId, ingredientId);
		if (dbshoppingcart == null) {
			return false;
		}
		shoppingCartRepo.delete(dbshoppingcart);
		return true;
	}

	// 清空購物車
	@Transactional
	public void emptyCart(Integer customerId) {
		List<ShoppingCart> list = shoppingCartRepo.findByCustomerId(customerId);
		for (ShoppingCart Cart : list) {
			shoppingCartRepo.delete(Cart);
		}
	}

	@Transactional
	public void deleteCart(ShoppingCart cart) {
		shoppingCartRepo.delete(cart);
	}

	public ShoppingCart addRecipeToCart(Integer ingredientId, Integer customerId, Integer quantity) {
		// 先判斷購物車裡面是否已有該食材
	    ShoppingCart shoppingCart = shoppingCartRepo.findByCustomerIdAndIngredientId(customerId, ingredientId);
	    if (shoppingCart != null) {
	        int newQuantity = shoppingCart.getQuantity() + quantity;
	        int stockQuantity = shoppingCart.getIngredient().getStockQuantity();

	        // 如果新數量超過庫存則無法新增
	        if (newQuantity > stockQuantity) {
	            return shoppingCart;
	        }

	        shoppingCart.setQuantity(newQuantity);
	        return shoppingCartRepo.save(shoppingCart);
	    }

	    // 新增新食材至購物車
	    Optional<CustomerBean> customerOpt = cuRepo.findById(customerId);
	    Optional<IngredientBean> ingredientOpt = ingredRepo.findById(ingredientId);

	    if (customerOpt.isEmpty() || ingredientOpt.isEmpty()) {
	        return null;
	    }

	    IngredientBean ingredient = ingredientOpt.get();
	    if (quantity > ingredient.getStockQuantity()) {
	        return null;
	    }

	    ShoppingCartId shoppingCartId = new ShoppingCartId(customerId, ingredientId);

		ShoppingCart shoppingcart = new ShoppingCart();
		shoppingcart.setShoppingCartId(shoppingCartId);
		shoppingcart.setCustomer(customerOpt.get());
		shoppingcart.setIngredient(ingredient);
		if (quantity > ingredient.getStockQuantity()) {
			return null;
		}
		shoppingcart.setQuantity(quantity);

		return shoppingCartRepo.save(shoppingcart);
	}

	public ShoppingCart addIngredirntQuantity(ShoppingCartDto dto) {
		// Integer ingredientId, Integer customerId, Integer quantity

		// 先判斷原本購物車裡面有沒有這個食材，有的話直接增加所需數量
		ShoppingCart dbshoppingcart = shoppingCartRepo.findByCustomerIdAndIngredientId(dto.getCustomerId(),
				dto.getIngredientId());
		if (dbshoppingcart != null) {
			// 若超過食材庫存數量增加則不能再增加
			Integer scq = dbshoppingcart.getQuantity();
			Integer stockQuantity = dbshoppingcart.getIngredient().getStockQuantity();
			if ((scq + dto.getQuantity()) > stockQuantity) {
				dbshoppingcart.setQuantity(dbshoppingcart.getIngredient().getStockQuantity());
				return dbshoppingcart;
			}
			dbshoppingcart.setQuantity(dbshoppingcart.getQuantity() + dto.getQuantity());
			return dbshoppingcart;
		}

		Optional<CustomerBean> optional1 = cuRepo.findById(dto.getCustomerId());
		CustomerBean customer = optional1.get();

		Optional<IngredientBean> optional2 = ingredRepo.findById(dto.getIngredientId());
		IngredientBean ingredient = optional2.get();

		ShoppingCartId shoppingcartid = new ShoppingCartId();
		shoppingcartid.setCustomerId(dto.getCustomerId());
		shoppingcartid.setIngredientId(dto.getIngredientId());

		ShoppingCart shoppingcart = new ShoppingCart();
		shoppingcart.setShoppingCartId(shoppingcartid);
		shoppingcart.setCustomer(customer);
		shoppingcart.setIngredient(ingredient);
		if (dto.getQuantity() > ingredient.getStockQuantity()) {
			return null;
		}
		shoppingcart.setQuantity(dto.getQuantity());

		return shoppingCartRepo.save(shoppingcart);
	}
}