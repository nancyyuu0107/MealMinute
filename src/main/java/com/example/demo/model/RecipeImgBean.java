package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipeImg")
@Getter
@Setter
@NoArgsConstructor
public class RecipeImgBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer recipeImgId; // 食譜照片編號
	
	private String imgUrl; // 食譜照片
	
	private byte[] img; // 食譜照片(備用)
	
//	@Column(name = "recipeId", insertable = false, updatable = false)
//	private Integer recipeId; // 食譜編號(FK)
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipeId")
	@JsonBackReference
	private RecipeBean recipeBean;
	
	
	
}
