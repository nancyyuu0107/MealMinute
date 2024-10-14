package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customersDetail")
@Getter
@Setter
@NoArgsConstructor
public class CustomerDetailBean {

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "customers"))
	@Id
	@GeneratedValue(generator = "generator")
	private Integer id;

	private String photo; // 會員相片

	@Column
	private String address; //會員住址
	
	private Character gender; //會員性別
	
	private Date birthday; //會員生日
	
	//有疑慮，先保留
	private boolean permission; //使用權限(會員或管理員)
	
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
//	@Temporal(TemporalType.TIMESTAMP)
//	private LocalDateTime createdAt; //會員創建時間

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE", timezone = "GMT+8")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	private Integer height;

	private Integer weight;
	
	//設計前端時不要顯示就好
	private boolean status; //使用狀態(可使用或除權)

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	private CustomerBean customers;

//	@PrePersist
//	public void onCreate() {
//		if(createdAt == null) {
//			createdAt = LocalDateTime.now(); 
//		}
//	}
}
