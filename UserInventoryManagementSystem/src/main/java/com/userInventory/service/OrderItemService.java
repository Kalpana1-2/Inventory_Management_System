package com.userInventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.userInventory.dto.OrderItemsDTO;
import com.userInventory.repository.OrderItemRepository;
import com.userInventory.repository.OrderRepository;
import com.userInventory.repository.ProductRepository;
import com.userInventory.repository.entity.OrderItems;
import com.userInventory.repository.entity.Orders;
import com.userInventory.repository.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
	private final OrderItemRepository orderItemRepo;
	private final OrderRepository orderRepo;
	private final ProductRepository productRepo;
	
	//ENTITY TO DTO
	private OrderItemsDTO convertToDTO(OrderItems item){

	    OrderItemsDTO dto = new OrderItemsDTO();

	    dto.setOrderItemId(item.getOrderItemId());
	    dto.setUserId(
	            item.getOrder()
	                .getUser()
	                .getUserId());

	    dto.setOrderPrice(item.getOrderPrice());

	    dto.setOrderStatus(item.getOrderStatus());

	    dto.setTotalAmount(item.getTotalAmount());

	    dto.setQuantity(item.getQuantity());

	    if(item.getOrder()!=null){

	        dto.setOrderId(
	                item.getOrder().getOrderId());

	    }

	    if(item.getProduct()!=null){

	        dto.setProductId(
	                item.getProduct().getProductId());

	        dto.setProductName(
	                item.getProduct().getProductName());

	    }

	    return dto;
	}
	
	//DTO TO ENTITY
	private OrderItems convertToEntity(
	        OrderItemsDTO dto){

	    OrderItems item = new OrderItems();

	    item.setOrderPrice(dto.getOrderPrice());

	    item.setOrderStatus(dto.getOrderStatus());

	    item.setTotalAmount(dto.getTotalAmount());

	    item.setQuantity(dto.getQuantity());

	    Orders order = orderRepo
	            .findById(dto.getOrderId())
	            .orElseThrow(
	            () -> new RuntimeException(
	            "Order Not Found"));

	    item.setOrder(order);

	    Product product = productRepo
	            .findById(dto.getProductId())
	            .orElseThrow(
	            () -> new RuntimeException(
	            "Product Not Found"));

	    item.setProduct(product);

	    return item;
	}
	
	//SAVE ORDER 
	public OrderItemsDTO saveOrderItem(
	        OrderItemsDTO dto){

	    OrderItems item =
	            convertToEntity(dto);

	    OrderItems savedItem =
	            orderItemRepo.save(item);

	    return convertToDTO(savedItem);

	}
	
	//VIEW ALL ORDER ITEMS
	public List<OrderItemsDTO>
	viewOrderItems(){

	    return orderItemRepo.findAll()

	            .stream()

	            .map(this::convertToDTO)

	            .collect(Collectors.toList());

	}
	//SEARCH ORDER ITEM
	public Optional<OrderItemsDTO>
	searchOrderItem(int id){

	    return orderItemRepo

	            .findById(id)

	            .map(this::convertToDTO);

	}
	//GET ORDER ITEM BY ORDER
	public List<OrderItemsDTO>
	getOrderItemsByOrder(int orderId){

	    return orderItemRepo

	            .findByOrder_OrderId(orderId)

	            .stream()

	            .map(this::convertToDTO)

	            .collect(Collectors.toList());

	}
	//DELETE ORDER ITEM
	public void deleteOrderItem(int id){

	    OrderItems item =
	            orderItemRepo

	            .findById(id)

	            .orElseThrow(
	            () -> new RuntimeException(
	            "Order Item Not Found"));

	    orderItemRepo.delete(item);

	}

}
