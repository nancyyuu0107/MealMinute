package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WishListId implements Serializable{
	
	private Integer customerId;
	
	private Integer recipeId;

	@Override
	public int hashCode() {
		return Objects.hash(customerId, recipeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WishListId other = (WishListId) obj;
		return Objects.equals(customerId, other.customerId) && Objects.equals(recipeId, other.recipeId);
	}
	
	
	
}
