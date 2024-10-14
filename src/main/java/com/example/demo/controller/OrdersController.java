package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Orders;
import com.example.demo.model.ShoppingCart;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrdersService;
import com.example.demo.service.ShoppingCartService;

@CrossOrigin
@RestController
public class OrdersController {
	@Autowired
	private OrdersService oService;

	@Autowired
	private ShoppingCartService scService;

	@Autowired
	private OrderDetailService odService;

	@Autowired
	private CustomerBeanRepository cuRepo;

	// 用customerId去找他的全部購物車，再生成訂單跟訂單明細，之後再刪除購物車內容
	@PostMapping("/orders/gotocheckout")
	public Orders goToCheckout(@RequestBody List<ShoppingCartDto> dtos) {
		return oService.createOrder(dtos);
	}

//	// 用customerId去找他的全部購物車，再生成訂單跟訂單明細，之後再刪除購物車內容
//	@PostMapping("/orders/gotocheckout/{cuid}")
//	public Orders goToCheckout(@PathVariable("cuid") Integer customerId) {
//
//		Optional<CustomerBean> optional = cuRepo.findById(customerId);
//		CustomerBean cu = new CustomerBean();
//		if (optional != null) {
//			cu = optional.get();
//		}
//
//		List<ShoppingCart> list = scService.findByCustomerId(cu.getCustomerId());
//
//		// 判斷若是食材庫存不足，則不執行下面方法
//		for (ShoppingCart shoppingcart : list) {
//			if (shoppingcart.getQuantity() > shoppingcart.getIngredient().getStockQuantity()) {
//				return null;
//			}
//		}
//
//		Orders order = new Orders();
//		order.setCustomer(cu);
//		order.setOrderRemark("");
//		order.setOrderTime(new Date());
//		order.setOrderStatus("未付款");
//		order.setCancel(false);
//
//		// 計算訂單金額
//		int totalAmount = 0;
//		for (ShoppingCart shoppingcart : list) {
//			totalAmount = totalAmount + (shoppingcart.getIngredient().getPrice() * shoppingcart.getQuantity());
//		}
//		order.setOrderAmount(totalAmount);
//
//		Orders dataOrder = oService.createOrder(order);
//
//		// 產生訂單明細，同時刪除購物車
//		for (ShoppingCart shoppingcart : list) {
//			OrderDetail od = new OrderDetail();
//			od.setIngredient(shoppingcart.getIngredient());
//			od.setQuantity(shoppingcart.getQuantity());
//			od.setOrders(order);
//			od.setTotalAmount(shoppingcart.getIngredient().getPrice() * shoppingcart.getQuantity());
//			od.setOrders(dataOrder);
//
//			// 扣除商品庫存
//			shoppingcart.getIngredient()
//					.setStockQuantity(shoppingcart.getIngredient().getStockQuantity() - shoppingcart.getQuantity());
//
//			// 生成訂單明細到資料庫
//			odService.createOrderDetail(od);
//
//			// 刪除購物車內容
//			scService.deleteCart(shoppingcart);
//		}
//
//		return dataOrder;
//	}

	// 貨到付款(現金)
	@PutMapping("/orders/cashorder/{oid}")
	public Orders updateCashOrder(@PathVariable("oid") Integer orderId) {
		return oService.updateCashOrder(orderId);
	}

	// linepay付款後修改確認訂單狀態為 已付款
	@PutMapping("/orders/linepayorder")
	public Orders updateLinePayOrder(@RequestBody String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		String transactionId = obj.isNull("transactionId") ? null : obj.getString("transactionId");
		Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");
		return oService.updateLinePayOrder(orderId, transactionId);
	}

	// 顧客看自己的訂單
	@GetMapping("/orders/showorders/{cuid}")
	public List<Orders> showOrdersByCustomerId(@PathVariable("cuid") Integer customerId) {
		return oService.findByCustomerId(customerId);
	}

	@GetMapping("/orders/showorder/{oid}")
	public String showOrderByOrderId(@PathVariable("oid") Integer orderId) throws JSONException {
		JSONObject responseJson = new JSONObject();
		Orders orders = oService.findByOrderId(orderId);
		JSONObject item = new JSONObject();
		JSONArray array = new JSONArray();
		item.put("orderAmount", orders.getOrderAmount());
		item.put("orderId", orders.getOrderId());
		item.put("orderRemark", orders.getOrderRemark());
		item.put("orderStatus", orders.getOrderStatus());
		item.put("orderTime", orders.getOrderTime());
		item.put("paymentMethod", orders.getPaymentMethod());
		item.put("paymentTime", orders.getOrderTime());
		item.put("customerId", orders.getCustomer().getCustomerId());
		array.put(item);
		responseJson.put("list", array);
//		return oService.findByOrderId(orderId);
		return responseJson.toString();
	}

	// 顧客取消訂單
	@PutMapping("/orders/cancel")
	public String cancalOrderByCustomer(@RequestBody String json) throws JSONException {

		JSONObject obj = new JSONObject(json);
		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");

		if (oService.deleteOrderByCustomerId(customerId, orderId)) {
			return "取消成功";
		}
		return "取消失敗";
	}

	// 後臺取消訂單
	@PutMapping("/orders/cancel/{oid}")
	public String cancalOrder(@PathVariable("oid") Integer orderId) throws JSONException {
		JSONObject responseJson = new JSONObject();

		if (orderId == null) {
			responseJson.put("success", false);
			responseJson.put("message", "id是必要欄位");
		} else if (oService.findByOrderId(orderId) == null) {
			responseJson.put("success", false);
			responseJson.put("message", "沒有這筆訂單");
		} else if (oService.deleteOrder(orderId)) {
			responseJson.put("success", true);
			responseJson.put("message", "取消成功");
		} else {
			responseJson.put("success", false);
			responseJson.put("message", "取消失敗");
		}
		return responseJson.toString();
	}

	// 找全部訂單
	@GetMapping("/orders/find")
	public String getAllOrders() throws JSONException {
		JSONObject responseJson = new JSONObject();

		JSONArray array = new JSONArray();
		List<Orders> orders = oService.findAll();
		for (Orders o : orders) {
			JSONObject item = new JSONObject();
			item.put("orderId", o.getOrderId());
			item.put("orderAmount", o.getOrderAmount());
			item.put("orderTime", o.getOrderTime());
			item.put("orderStatus", o.getOrderStatus());
			item.put("orderRemark", o.getOrderRemark());
			item.put("paymentMethod", o.getPaymentMethod());
			item.put("paymentTime", o.getPaymentTime());
			item.put("orderDetails", o.getOrderDetails());
			array.put(item);
		}

		responseJson.put("list", array);
		return responseJson.toString();
	}

	// 顧客依訂單狀態找訂單
	@GetMapping("/orders/findstatus")
	public List<Orders> getOrdersByStatusAndCustomerId(@RequestParam Map<String, String> params) {
		Integer customerId = params.get("customerId") != null ? Integer.parseInt(params.get("customerId")) : null;
		String status = params.get("status");

		if (customerId == null || customerId == 0 || status == null || status.isEmpty()) {
			return null;
		}

		return oService.findByStatusAndCustomerId(status, customerId);
	}

	// 後台依訂單狀態找訂單
	@GetMapping("/orders/findbystatus/{status}")
	public List<Orders> getOrdersByStatus(@PathVariable("status") String status) {
		if (status == "" || status == null) {
			return null;
		}
		return oService.findByStatus(status);
	}

	// 修改訂單狀態為已退貨
	@PutMapping("/orders/return")
	public ResponseEntity<?> returnOrderByCustomer(@RequestBody String json) throws JSONException {
		try {
			JSONObject obj = new JSONObject(json);
			Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
			Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");

			if (customerId == null || orderId == null) {
				return ResponseEntity.badRequest().body("必須提供顧客ID和訂單ID。");
			}

			Orders updateReturnOrder = oService.updateReturnOrder(orderId, customerId);
			if (updateReturnOrder == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到訂單或條件不符合退貨。");
			}
			return ResponseEntity.ok(updateReturnOrder);
		} catch (JSONException e) {
			return ResponseEntity.badRequest().body("JSON格式不正確。");
		}
	}
}
