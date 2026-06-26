package com.userInventory.dto;
import java.math.BigDecimal;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDTO {
	private int orderItemId;
	private int orderId;
	 private int userId;  
	private int productId;
	private String productName;
	private int quantity;
	private BigDecimal orderPrice;
	private String orderStatus;
	private BigDecimal totalAmount;

}
