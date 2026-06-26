package com.userInventory.repository.entity;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Table(name="orders")
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_id")
	private int orderId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Users user;
	
	@Column(name="quantity",nullable=false)
	private int quantity;
	
	@Column(name="order_date")
	private LocalDate orderDate;
	
	@Column(name="order_status")
	private String orderStatus;
	
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private List<OrderItems>orderItems;
	
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private List<OrderPayment>orderPayment;

}
