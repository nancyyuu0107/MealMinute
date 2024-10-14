package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.OrderDetail;
import com.example.demo.model.Orders;
import com.example.demo.model.linepay.CheckoutPaymentRequestForm;
import com.example.demo.model.linepay.ConfirmData;
import com.example.demo.model.linepay.ProductForm;
import com.example.demo.model.linepay.ProductPackageForm;
import com.example.demo.model.linepay.RedirectUrls;
import com.example.demo.model.linepay.RefundData;
import com.example.demo.model.linepay.util.ApiUtil;
import com.example.demo.model.linepay.util.LinepayUtil;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrdersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/linepay")
public class LinepayController {

	@Autowired
	private OrdersService oService;

	@Autowired
	private OrderDetailService odService;

	@PostMapping("/request/{oid}")
	public JsonNode linepayRequest(@PathVariable("oid") Integer orderId) throws JsonProcessingException {
		Orders order = oService.findByOrderId(orderId);
		if (order == null || "已付款".equals(order.getOrderStatus()) || "已取消".equals(order.getOrderStatus())) {
			return null;
		}
		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
		form.setAmount(new BigDecimal(order.getOrderAmount()));
		form.setCurrency("TWD");
		String formOrderId = "ml"+order.getOrderId() + "";
		form.setOrderId(formOrderId);

		List<OrderDetail> odlist = odService.findByOrderId(orderId);

		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal userFee = BigDecimal.ZERO; // 設為零或實際費用
		BigDecimal shippingFee = BigDecimal.ZERO; // 設為零或實際費用

		List<ProductPackageForm> packageForms = new ArrayList<>(); // 用來累積所有的 package

		for (OrderDetail od : odlist) {
			ProductPackageForm productPackageForm = new ProductPackageForm();
			productPackageForm.setId(String.valueOf(od.getOrderDetailId()));
			productPackageForm.setName("MealMinute");
			productPackageForm.setAmount(new BigDecimal(od.getTotalAmount()));

			// 設置 userFee 和 shippingFee
			productPackageForm.setUserFee(userFee);
			productPackageForm.setShippingFee(shippingFee);

			ProductForm productForm = new ProductForm();
			productForm.setId(String.valueOf(od.getIngredient().getIngredientId()));
			productForm.setName(od.getIngredient().getIngredientName());
			productForm.setImageUrl("");
			productForm.setQuantity(new BigDecimal(od.getQuantity()));
			productForm.setPrice(new BigDecimal(od.getIngredient().getPrice()));

			productPackageForm.setProducts(Arrays.asList(productForm));

			form.setPackages(Arrays.asList(productPackageForm));

//		    // 累積包裹
			packageForms.add(productPackageForm);

			// 累積總金額
			totalAmount = totalAmount.add(productPackageForm.getAmount());
		}

		// 加上 userFee 和 shippingFee
		totalAmount = totalAmount.add(userFee).add(shippingFee);

		// 將累積的 packageForms 加入到 form
		form.setPackages(packageForms);

		// 確認 form.amount 與 totalAmount 一致
		if (form.getAmount().compareTo(totalAmount) != 0) {
			form.setAmount(totalAmount);
		}

		System.out.println("form.getAmount():" + form.getAmount());
		System.out.println("totalAmount:" + totalAmount);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setConfirmUrl("http://localhost:5173/confirm/" + orderId);
		form.setRedirectUrls(redirectUrls);

		String requestUri = "/v3/payments/request";
		String requestHttpsUri = "https://sandbox-api-pay.line.me/v3/payments/request";
		String nonce = UUID.randomUUID().toString();

		String channelId = "2006131078";
		ObjectMapper mapper = new ObjectMapper();
		String ChannelSecret = "6af8b5989f5bcb3623ef0c9e840d82f7";

		String signature = LinepayUtil.encrypt(ChannelSecret,
				ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);

		JsonNode sendPost = ApiUtil.sendPost(channelId, nonce, signature, requestHttpsUri,
				mapper.writeValueAsString(form));

//		System.out.println("body => " + mapper.writeValueAsString(form));
//		System.out.println("nonce => " + nonce);
//		System.out.println("signature => " + signature);
//		JsonNode responseBody = ApiUtil.sendPost(channelId, nonce, signature, requestHttpsUri,
//				mapper.writeValueAsString(form));
//		System.out.println("responseBody => " + responseBody);
//		System.out.println();

		return sendPost;

//		JSONObject responseJson = null;
//		try {
//			responseJson.put("body", mapper.writeValueAsString(form));
//			responseJson.put("X-LINE-ChannelId", channelId);
//			responseJson.put("X-LINE-Authorization-Nonce", nonce);
//			responseJson.put("X-LINE-Authorization", false);
//			responseJson.put("Content-Type", "application/json");
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}

	@PostMapping("/confirm")
	public JsonNode linepayConfirm(@RequestBody String json) throws JSONException, JsonProcessingException {
		JSONObject obj = new JSONObject(json);
		String transactionId = obj.isNull("transactionId") ? null : obj.getString("transactionId");
		Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");

		Orders order = oService.findByOrderId(orderId);

		ConfirmData confirmData = new ConfirmData();
		confirmData.setAmount(new BigDecimal(order.getOrderAmount()));
		confirmData.setCurrency("TWD");
		String confirmNonce = UUID.randomUUID().toString();
		String confirmUri = "/v3/payments/" + transactionId + "/confirm";
		String confirmRequestHttpsUri = "https://sandbox-api-pay.line.me/v3/payments/" + transactionId + "/confirm";

		String channelId = "2006131078";
		ObjectMapper mapper = new ObjectMapper();
		String ChannelSecret = "6af8b5989f5bcb3623ef0c9e840d82f7";
		String signatureConfirm = LinepayUtil.encrypt(ChannelSecret,
				ChannelSecret + confirmUri + mapper.writeValueAsString(confirmData) + confirmNonce);
		JsonNode sendPost = ApiUtil.sendPost(channelId, confirmNonce, signatureConfirm, confirmRequestHttpsUri,
				mapper.writeValueAsString(confirmData));
		return sendPost;
	}

	@PostMapping("/refund/{oid}")
	public JsonNode linepayRefund(@PathVariable("oid") Integer orderId) throws JsonProcessingException {
		Orders order = oService.findByOrderId(orderId);

		RefundData refundData = new RefundData();
		refundData.setRefundAmount(new BigDecimal(order.getOrderAmount()));
		String refundNonce = UUID.randomUUID().toString();
		String refundUri = "/v3/payments/" + order.getTransactionId() + "/refund";
		String refundRequestHttpUri = "https://sandbox-api-pay.line.me/v3/payments/" + order.getTransactionId()
				+ "/refund";

		String channelId = "2006131078";
		ObjectMapper mapper = new ObjectMapper();
		String ChannelSecret = "6af8b5989f5bcb3623ef0c9e840d82f7";
		String signatureRefund = LinepayUtil.encrypt(ChannelSecret,
				ChannelSecret + refundUri + mapper.writeValueAsString(refundData) + refundNonce);
		JsonNode sendPost = ApiUtil.sendPost(channelId, refundNonce, signatureRefund, refundRequestHttpUri,
				mapper.writeValueAsString(refundData));
		return sendPost;
	}
}
