package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.model.CustomerBean;
import com.example.demo.model.CustomerBeanRepository;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderDetailRepository;
import com.example.demo.model.Orders;
import com.example.demo.model.OrdersRepository;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.ShoppingCartId;
import com.example.demo.model.ShoppingCartRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class OrdersService {
	@Autowired
	private OrdersRepository oRepo;

	@Autowired
	private ShoppingCartRepository scRepo;

	@Autowired
	private CustomerBeanRepository cuRepo;

	@Autowired
	private OrderDetailRepository odRepo;

	public Orders createOrder(List<ShoppingCartDto> dtos) {

		Integer customerId = 0;

		for (ShoppingCartDto scDto : dtos) {
			customerId = scDto.getCustomerId();
		}

		Optional<CustomerBean> cuResult = cuRepo.findById(customerId);
		if (cuResult.isEmpty()) {
			throw new EntityNotFoundException("顧客不存在");
		}
		CustomerBean cu = cuResult.get();

		List<ShoppingCart> carts = new ArrayList<>();
		for (ShoppingCartDto scDto : dtos) {
			carts.add(scRepo.findByCustomerIdAndIngredientId(customerId, scDto.getIngredientId()));
		}

		// 判斷若是食材庫存不足，則不執行下面方法
		for (ShoppingCart shoppingcart : carts) {
			if (shoppingcart.getQuantity() > shoppingcart.getIngredient().getStockQuantity()) {
				return null;
			}
		}

		Orders order = new Orders();
		order.setCustomer(cu);
		order.setOrderRemark("");
		order.setOrderTime(new Date());
		order.setOrderStatus("未付款");
		order.setTransactionId(null);

		// 計算訂單金額
		int totalAmount = 0;
		for (ShoppingCart shoppingcart : carts) {
			totalAmount = totalAmount + (shoppingcart.getIngredient().getPrice() * shoppingcart.getQuantity());
		}
		order.setOrderAmount(totalAmount);

		Orders dataOrder = oRepo.save(order);

		// 產生訂單明細，同時刪除購物車
		for (ShoppingCart shoppingcart : carts) {
			OrderDetail od = new OrderDetail();
			od.setIngredient(shoppingcart.getIngredient());
			od.setQuantity(shoppingcart.getQuantity());
			od.setOrders(order);
			od.setTotalAmount(shoppingcart.getIngredient().getPrice() * shoppingcart.getQuantity());
			od.setOrders(dataOrder);

			// 扣除商品庫存
			shoppingcart.getIngredient()
					.setStockQuantity(shoppingcart.getIngredient().getStockQuantity() - shoppingcart.getQuantity());

			// 生成訂單明細到資料庫
			odRepo.save(od);

			// 刪除購物車內容
			scRepo.delete(shoppingcart);
		}

		return dataOrder;

		// return oRepo.save(order);
	}

//	public Orders createOrder(Orders o) {
//		return oRepo.save(o);
//	}

	public Orders updateLinePayOrder(Integer orderId, String transactionId) {
		Optional<Orders> optional = oRepo.findById(orderId);
		if (optional.isPresent()) {
			Orders order = optional.get();
			order.setOrderStatus("已付款");
			order.setPaymentMethod("LINEPAY");
			order.setPaymentTime(new Date());
			order.setTransactionId(transactionId);
			return order;
		}
		return null;
	}

	public Orders updateCashOrder(Integer orderId) {
		Optional<Orders> optional = oRepo.findById(orderId);
		if (optional.isPresent()) {
			Orders order = optional.get();
			order.setOrderStatus("已付款");
			order.setPaymentMethod("貨到付款(現金)");
			order.setPaymentTime(new Date());
			order.setTransactionId(null);
			return order;
		}
		return null;
	}

	public Orders updateReturnOrder(Integer orderId, Integer customerId) {
		Optional<Orders> optional = oRepo.findById(orderId);
		if (optional.isPresent()) {
			Orders order = optional.get();
			if (!"已付款".equals(order.getOrderStatus()) || !order.getCustomer().getCustomerId().equals(customerId)) {
				return null;
			}
			order.setOrderStatus("已退貨");
			oRepo.save(order);
			return order;
		}
		return null;
	}

	public List<Orders> findByCustomerId(Integer customerId) {
		return oRepo.findByCustomerId(customerId);
	}

	public Orders findByOrderId(Integer orderId) {
		Optional<Orders> optional = oRepo.findById(orderId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public boolean deleteOrderByCustomerId(Integer customerId, Integer orderId) {
		Optional<Orders> optional = oRepo.findById(orderId);
		Orders order = new Orders();
		if (optional.isPresent()) {
			order = optional.get();
		}

		// 先判斷操作的人是否是訂單所有人
		if (customerId.equals(order.getCustomer().getCustomerId())) {
			order.setOrderStatus("已取消");
			// 將原本訂單商品數量重新加入庫存
			List<OrderDetail> list = odRepo.findByOrderId(orderId);
			for (OrderDetail od : list) {
				od.getIngredient().setStockQuantity(od.getIngredient().getStockQuantity() + od.getQuantity());
			}
			return true;
		}
		return false;
	}

	public List<Orders> findAll() {
		return oRepo.findAll();
	}

	public boolean deleteOrder(Integer orderId) {
		Optional<Orders> optional = oRepo.findById(orderId);
		Orders order = new Orders();
		if (optional.isPresent()) {
			order = optional.get();

			if ("已取消".equals(order.getOrderStatus()) || "已付款".equals(order.getOrderStatus())) {
				return false;
			}
			order.setOrderStatus("已取消");
			// 將原本訂單商品數量重新加入庫存
			List<OrderDetail> list = odRepo.findByOrderId(orderId);
			for (OrderDetail od : list) {
				od.getIngredient().setStockQuantity(od.getIngredient().getStockQuantity() + od.getQuantity());
			}
			return true;
		} else {
			return false;
		}
	}

	public List<Orders> findByStatusAndCustomerId(String status, Integer customerId) {
		return oRepo.findByStatusAndCustomerId(status, customerId);
	}

	public List<Orders> findByStatus(String status) {
		return oRepo.findByStatus(status);
	}
}
