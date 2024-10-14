package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ShoppingCartId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer customerId;

	private Integer ingredientId;

	@Override
	public int hashCode() {
		return Objects.hash(customerId, ingredientId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingCartId other = (ShoppingCartId) obj;
		return Objects.equals(customerId, other.customerId) && Objects.equals(ingredientId, other.ingredientId);
	}

}
