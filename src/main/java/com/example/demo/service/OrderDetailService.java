package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderDetailRepository;
import com.example.demo.model.Orders;
import com.example.demo.model.OrdersRepository;

@Service
@Transactional
public class OrderDetailService {
	@Autowired
	private OrderDetailRepository odRepo;

	@Autowired
	private OrdersRepository oRepo;

	public OrderDetail createOrderDetail(OrderDetail od) {
		return odRepo.save(od);
	}

	public List<OrderDetail> findByOrderId(Integer orderId) {
		return odRepo.findByOrderId(orderId);
	}

	public List<OrderDetail> findByIngredientId(Integer ingredientId) {
		return odRepo.findByIngredientId(ingredientId);
	}

	public List<OrderDetail> findAllOrderDetail() {
		return odRepo.findAll();
	}

	public OrderDetail updateOrderDetailQuantity(Integer orderDetailId, Integer quantity) {
		Optional<OrderDetail> optional = odRepo.findById(orderDetailId);
		OrderDetail dataod = new OrderDetail();
		if (optional.isPresent()) {
			dataod = optional.get();
		}

		// 判斷修改後數量是否超過庫存
		if (quantity > (dataod.getIngredient().getStockQuantity() + dataod.getQuantity())) {
			return null;
		}

		// 還原原本食材庫存
		dataod.getIngredient().setStockQuantity(dataod.getIngredient().getStockQuantity() + dataod.getQuantity());

		// 扣除修改後庫存
		dataod.getIngredient().setStockQuantity(dataod.getIngredient().getStockQuantity() - quantity);

		dataod.setQuantity(quantity);

		// 計算原本訂單金額扣除修改前訂單金額
		dataod.getOrders().setOrderAmount(dataod.getOrders().getOrderAmount() - dataod.getTotalAmount());

		// 計算修改後訂單明細金額
		dataod.setTotalAmount(dataod.getIngredient().getPrice() * quantity);

		// 計算修改後訂單金額
		dataod.getOrders().setOrderAmount(dataod.getOrders().getOrderAmount() + dataod.getTotalAmount());

		return dataod;
	}

	// 取得已付款的訂單明細
	public List<OrderDetail> findOrderDetailByOrderStatus() {
		List<Orders> list = oRepo.findByStatus("已付款");
		List<OrderDetail> list2 = new ArrayList<>();

		for (Orders o : list) {
			List<OrderDetail> list3 = odRepo.findByOrderId(o.getOrderId());
			for (OrderDetail od : list3) {
				list2.add(od);
			}
		}
		return list2;
	}
	
	public OrderDetail findByOrderDetailId(Integer orderDetailId) {
		Optional<OrderDetail> optional = odRepo.findById(orderDetailId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
}
