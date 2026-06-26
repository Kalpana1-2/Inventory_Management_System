package com.userInventory.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userInventory.dto.OrderDTO;
import com.userInventory.dto.OrderStatusDTO;
import com.userInventory.dto.PlaceOrderDTO;
import com.userInventory.repository.OrderItemRepository;
import com.userInventory.repository.OrderRepository;
import com.userInventory.repository.ProductRepository;
import com.userInventory.repository.UserAddressRepository;
import com.userInventory.repository.UserRepository;
import com.userInventory.repository.entity.OrderItems;
import com.userInventory.repository.entity.Orders;
import com.userInventory.repository.entity.Product;
import com.userInventory.repository.entity.UserAddress;
import com.userInventory.repository.entity.Users;

@Service
public class OrderService {
	@Autowired
	OrderRepository order_repo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ProductRepository productRepo;
	@Autowired
	OrderItemRepository orderItemRepo;
	@Autowired
	UserAddressRepository addressRepo;
	//ENTITY TO DTO 
	private OrderDTO convertToDTO(Orders order) {
		OrderDTO dto=new OrderDTO();
		dto.setOrderId(order.getOrderId());
		dto.setQuantity(order.getQuantity());
		dto.setOrderDate(order.getOrderDate());
		dto.setOrderStatus(order.getOrderStatus());
		if(order.getUser()!= null) {
			dto.setUserId(order.getUser().getUserId());
			dto.setUserName(order.getUser().getUserName());
		}
		return dto;
	}
	public OrderDTO saveOrder(OrderDTO dto) {

	    Orders order = new Orders();

	    order.setQuantity(dto.getQuantity());

	    order.setOrderDate(dto.getOrderDate());

	    order.setOrderStatus(dto.getOrderStatus());

	    Users user = userRepo.findById(dto.getUserId())
	            .orElseThrow(
	                    () -> new RuntimeException(
	                            "User Not Found"));

	    order.setUser(user);

	    Orders savedOrder = order_repo.save(order);

	    return convertToDTO(savedOrder);

	}
	
	public OrderDTO updateOrder(
	        int orderId,
	        OrderDTO dto) {

	    Orders order = order_repo
	            .findById(orderId)
	            .orElseThrow(
	                    () -> new RuntimeException(
	                            "Order Not Found"));

	    order.setQuantity(dto.getQuantity());

	    order.setOrderDate(dto.getOrderDate());

	    order.setOrderStatus(dto.getOrderStatus());

	    Orders updatedOrder =
	            order_repo.save(order);

	    return convertToDTO(updatedOrder);

	}
	
	public void deleteOrder(int id) {

	    Orders order = order_repo
	            .findById(id)
	            .orElseThrow(
	                    () -> new RuntimeException(
	                            "Order Not Found"));

	    order_repo.delete(order);

	}
	
	public List<OrderDTO> viewOrders(){
		return order_repo.findAll().
				stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	public Optional<OrderDTO> searchOrder(int id) {
		return order_repo.findById(id).map(this::convertToDTO);
	}
	// ── Get Orders by Status ──────────────────────
	public List<OrderDTO> getOrdersByStatus(String status) {
	    return order_repo.findByOrderStatus(status)
	           .stream()
	           .map(this::convertToDTO)
	           .collect(Collectors.toList());
	}

	// ── Get Orders by User ────────────────────────
	public List<OrderDTO> getOrdersByUser(int userId) {
	    return order_repo.findByUser_UserId(userId)
	           .stream()
	           .map(this::convertToDTO)
	           .collect(Collectors.toList());
	}

	// ── Order Report / Summary ────────────────────
	public Map<String, Object> getOrderReport() {

	    Map<String, Object> report = new LinkedHashMap<>();
	    report.put("totalOrders",     order_repo.count());
	    report.put("pendingOrders",   order_repo
	                                  .countByOrderStatus("Pending"));
	    report.put("deliveredOrders", order_repo
	                                  .countByOrderStatus("Delivered"));
	    report.put("cancelledOrders", order_repo
	                                  .countByOrderStatus("Cancelled"));
	    report.put("shippedOrders",   order_repo
	                                  .countByOrderStatus("Shipped"));
	    return report;
	}
	
	public OrderDTO placeOrder(
	        PlaceOrderDTO dto){

	    Users user = userRepo
	            .findById(dto.getUserId())
	            .orElseThrow(
	            () -> new RuntimeException(
	            "User Not Found"));

	    UserAddress address =
	            addressRepo
	            .findByUser_UserId(
	            dto.getUserId())
	            .orElseThrow(
	            () -> new RuntimeException(
	            "Please Add Address First"));

	    Product product =
	            productRepo
	            .findById(dto.getProductId())
	            .orElseThrow(
	            () -> new RuntimeException(
	            "Product Not Found"));

	    if(product.getStock().getQuantity()
	            < dto.getQuantity()){

	        throw new RuntimeException(
	        "Insufficient Stock");

	    }
	    
	    //CREATE ORDER

	    Orders order = new Orders();

	    order.setUser(user);

	    order.setQuantity(dto.getQuantity());

	    order.setOrderDate(LocalDate.now());

	    order.setOrderStatus("Pending");

	    Orders savedOrder =
	            order_repo.save(order);
	    
	    //CREATE ORDER ITEM

	    OrderItems item =
	            new OrderItems();

	    item.setOrder(savedOrder);

	    item.setProduct(product);

	    item.setQuantity(dto.getQuantity());

	    item.setOrderPrice(
	            product.getPrice());

	    item.setTotalAmount(
	            product.getPrice()
	            .multiply(
	            BigDecimal.valueOf(
	            dto.getQuantity())));

	    item.setOrderStatus("Pending");

	    orderItemRepo.save(item);

	    product.getStock().setQuantity(

	            product.getStock()
	            .getQuantity()

	            - dto.getQuantity());

	    productRepo.save(product);

	    return convertToDTO(savedOrder);

	}
	public OrderDTO updateOrderStatus(
	        int orderId,
	        OrderStatusDTO dto) {

	    Orders order = order_repo.findById(orderId)
	            .orElseThrow(() ->
	            new RuntimeException(
	            "Order Not Found"));

	    order.setOrderStatus(
	            dto.getOrderStatus());

	    Orders updatedOrder =
	            order_repo.save(order);

	    return convertToDTO(updatedOrder);
	}
	public String cancelOrder(int orderId) {

	    Orders order = order_repo.findById(orderId)
	            .orElseThrow(() ->
	            new RuntimeException(
	            "Order Not Found"));

	    String status = order.getOrderStatus();

	    if("Shipped".equalsIgnoreCase(status)
	            || "Delivered".equalsIgnoreCase(status)) {

	        throw new RuntimeException(
	                "Order Cannot Be Cancelled");
	    }

	    // RESTORE STOCK

	    List<OrderItems> items =
	            orderItemRepo.findByOrder_OrderId(
	            orderId);

	    for(OrderItems item : items) {

	        Product product =
	                item.getProduct();

	        product.getStock().setQuantity(

	                product.getStock()
	                .getQuantity()

	                + item.getQuantity());

	        productRepo.save(product);
	    }

	    // UPDATE ORDER STATUS

	    order.setOrderStatus("Cancelled");

	    order_repo.save(order);

	    return "Order Cancelled Successfully";
	}

}
