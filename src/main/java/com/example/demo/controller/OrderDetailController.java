package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.IngredientBean;
import com.example.demo.model.OrderDetail;
import com.example.demo.service.OrderDetailService;

@RestController
@CrossOrigin
public class OrderDetailController {
	@Autowired
	private OrderDetailService odService;

	// 根據訂單id找訂單明細
	@GetMapping("/orderDetail/order/{oid}")
	public String showByOrderId(@PathVariable("oid") Integer orderId) throws JSONException {
		JSONObject responseJson = new JSONObject();
		JSONArray array = new JSONArray();

		for (OrderDetail od : odService.findByOrderId(orderId)) {
			JSONObject item = new JSONObject();
			item.put("orderDetailId", od.getOrderDetailId());
			item.put("ingredientName", od.getIngredient().getIngredientName());
			item.put("ingredientPrice", od.getIngredient().getPrice());
			item.put("orderQuantity", od.getQuantity());
			item.put("orderDetailAmount", od.getTotalAmount());
			item.put("orderId", od.getOrders().getOrderId());
			array.put(item);
		}
		responseJson.put("list", array);

		return responseJson.toString();
	}

	@GetMapping("/orderDetail/ingredient/{iid}")
	public List<OrderDetail> showByIngredientId(@PathVariable("iid") Integer ingredientId) {
		return odService.findByIngredientId(ingredientId);
	}

	@GetMapping("/orderDetail/showall")
	public List<OrderDetail> showAllOrderDetails() {
		return odService.findAllOrderDetail();
	}

//	@PutMapping("/orderDetail/edit")
//	public OrderDetail editOrderDetail(@RequestBody String json) throws JSONException {
//		JSONObject obj = new JSONObject(json);
//		Integer orderDetailId = obj.isNull("orderDetailId") ? null : obj.getInt("orderDetailId");
//		Integer quantity = obj.isNull("quantity") ? null : obj.getInt("quantity");
//
//		return odService.updateOrderDetailQuantity(orderDetailId, quantity);
//	}

	@GetMapping("/orderDetail/paidorderingredient")
	public String showIngredientSaleData() throws JSONException {
		List<OrderDetail> list = odService.findOrderDetailByOrderStatus();
		JSONObject responseJson = new JSONObject();
		JSONObject item = new JSONObject();

		for (OrderDetail od : list) {
			String ingredientName = od.getIngredient().getIngredientName();
			Integer quantity = od.getQuantity();

			if (item.has(ingredientName)) {
				Integer ingredientQuantity = item.getInt(ingredientName);
				item.put(ingredientName, ingredientQuantity + quantity);
			} else {
				item.put(ingredientName, quantity);
			}
		}
		JSONArray array = new JSONArray();
		array.put(item);
		responseJson.put("list", array);
		return responseJson.toString();
	}

	@GetMapping("/orderDetail/paidordercategory")
	public String showCategorySaleData() throws JSONException {
		List<OrderDetail> list = odService.findOrderDetailByOrderStatus();
		JSONObject responseJson = new JSONObject();
		JSONObject item = new JSONObject();

		for (OrderDetail od : list) {
			String category = od.getIngredient().getCategory();
			Integer quantity = od.getQuantity();

			if (item.has(category)) {
				Integer categoryQuantity = item.getInt(category);
				item.put(category, categoryQuantity + quantity);
			} else {
				item.put(category, quantity);
			}
		}
		JSONArray array = new JSONArray();
		array.put(item);
		responseJson.put("list", array);
		return responseJson.toString();
	}
}