package com.example.demo.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredient")
@Getter
@Setter
@NoArgsConstructor
public class IngredientBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ingredientId; // 食材id

	private String ingredientName; // 食材名稱

	private String category; // 分類

	private Integer price; // 單價

	private String ingredientPicUrl; // 食材照片連結

	private String description; // 食材描述

	private Integer specification; // 食材規格

	private String unit; // 食材單位

	private Integer stockQuantity; // 庫存數量

	private String shelfLife; // 保存期限

	private Float requiredTemperature; // 所需溫度


	private String productStatus; // 商品狀態



	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime; // 新增時間

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime; // 更新時間

	private boolean recommend; // 當季推薦
	
	private Integer deleteStatus=1; //刪除狀態
	
	
	private String location; //產地
	

	@PrePersist
	public void onCreate() {
		if (createTime == null) {
			createTime = new Date();
		}
	}

	@JsonManagedReference("ingredient-reference") // 與 OrderDetail 的 ingredient 對應
	@OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderDetail> orderDetails;

}
