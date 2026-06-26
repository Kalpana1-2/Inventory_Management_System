package com.userInventory.repository.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Table(name="order_items")
public class OrderItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_item_id")
	private int orderItemId;
	
	@ManyToOne
	@JoinColumn(name="order_id",nullable=false)
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Orders order;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Product product;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="order_price",precision = 10,scale = 2)
	private BigDecimal orderPrice;
	
	@Column(name="order_status")
	private String orderStatus;
	
	@Column(name="total_amount",precision = 10,scale = 2)
	private BigDecimal totalAmount;

}
