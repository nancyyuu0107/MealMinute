package com.example.demo.model;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "favoritesList")
public class WishListBean {
	
	@EmbeddedId // 複合主鍵
	private WishListId wishListId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addedAt; // 新增收藏的時間
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("customerId")
	@JsonManagedReference
	@JoinColumn(name = "customerId")
	private CustomerBean customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("recipeId")
	@JsonManagedReference
	@JoinColumn(name = "recipeId")
	private RecipeBean recipe;
	
	@PrePersist
	public void onCreate() {
		if(addedAt == null) {
			addedAt = new Date();
		}
	}
	
}
