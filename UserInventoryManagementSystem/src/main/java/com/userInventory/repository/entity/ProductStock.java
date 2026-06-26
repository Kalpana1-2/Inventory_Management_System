package com.userInventory.repository.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter

@Entity
@Table(name="product_stock")
public class ProductStock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="stock_id")
	private int stockId;
	
	@OneToOne
	@JoinColumn(name="product_id")
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Product product;
	
	@Column(name="quantity",nullable=false)
	private int quantity;

}
