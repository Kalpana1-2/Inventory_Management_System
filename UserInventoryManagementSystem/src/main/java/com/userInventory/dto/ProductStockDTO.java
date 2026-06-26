package com.userInventory.dto;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDTO {
	private int stockId;
	private int productId;
	private String productName;
	private int quantity;
	private String stockStatus;

}
