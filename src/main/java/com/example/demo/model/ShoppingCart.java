package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shoppingcart")
public class ShoppingCart {
	@EmbeddedId
	private ShoppingCartId shoppingCartId;

	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("customerId")
    @JoinColumn(name = "customerId")
	@JsonManagedReference
	private CustomerBean customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("ingredientId")
    @JoinColumn(name = "ingredientId")
	@JsonManagedReference
	private IngredientBean ingredient;
}
