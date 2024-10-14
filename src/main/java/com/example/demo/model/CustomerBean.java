package com.example.demo.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class CustomerBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer customerId; // 會員id

	@Column(nullable = false)
	private String customerName; // 會員名稱

	@Column(unique = true, nullable = false)
	private String email; // 會員信箱

	@Column(nullable = false)
	private String password; // 會員密碼

	@Column(nullable = false)
	private String phoneNumber; // 會員電話

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
	private List<Orders> orders;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customers")
	@JsonManagedReference
	private CustomerDetailBean CustomerDetail;

}
