package com.example.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@NoArgsConstructor
public class RecipeBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer recipeId; // 食譜編號
	
	private String recipeName; // 食譜名稱
	
	private String category; // 分類
	
	private String recipeDescription; // 食譜描述
	
	private String steps; // 食譜步驟
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime; // 新增時間
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime; // 更新時間
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recipeBean", cascade = CascadeType.ALL) // mappedBy = "recipeBean"
	@JsonManagedReference
	private Set<RecipeImgBean> recipeImg = new HashSet<RecipeImgBean>(0);
	
	@PrePersist
	public void onCreate() {
		if(createTime==null) {
			createTime = new Date();
		}
	}

	public Set<RecipeImgBean> getRecipeImg() {
		return recipeImg;
	}

	public void setRecipeImg(Set<RecipeImgBean> recipeImg) {
		this.recipeImg = recipeImg;
	}
	
	
	
}
