package com.userInventory.repository.entity;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Table(name="product")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int productId;
	
	@Column(name="product_name",length=20)
	private String productName;
	
	@Column(name="description",length=255)
	private String description;
	
	@Column(name="price",precision = 10,scale = 2 )
	private BigDecimal price;
	
	@Column(name="img_url",length=255)
	private String imgUrl;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="category_id")
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Category category;
	
	@OneToOne(mappedBy = "product",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private ProductStock stock;



}
