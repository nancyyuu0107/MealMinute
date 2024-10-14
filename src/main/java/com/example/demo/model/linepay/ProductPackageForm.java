package com.example.demo.model.linepay;

import java.math.BigDecimal;
import java.util.List;

public class ProductPackageForm {
	private String id;

	private String name;

	private BigDecimal amount;

	private List<ProductForm> products;

	private BigDecimal userFee;

	private BigDecimal shippingFee;

	public BigDecimal getUserFee() {
		return userFee;
	}

	public void setUserFee(BigDecimal userFee) {
		this.userFee = userFee;
	}

	public BigDecimal getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<ProductForm> getProducts() {
		return products;
	}

	public void setProducts(List<ProductForm> products) {
		this.products = products;
	}

}
