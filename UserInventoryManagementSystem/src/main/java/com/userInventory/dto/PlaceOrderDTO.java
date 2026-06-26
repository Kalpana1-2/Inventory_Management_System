package com.userInventory.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDTO {
	private int userId;
	private int productId;
	private int quantity;

}
