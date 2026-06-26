package com.userInventory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userInventory.dto.OrderItemsDTO;
import com.userInventory.service.OrderItemService;

@RestController
@RequestMapping("/orderItems")
public class OrderItemController {
	@Autowired
	private OrderItemService orderItemService;
	

    // ADD ORDER ITEM

    @PostMapping("/add")

    public ResponseEntity<Object> addOrderItem(

            @RequestBody OrderItemsDTO dto){

        try{

            OrderItemsDTO item=

                    orderItemService

                    .saveOrderItem(dto);

            return ResponseEntity

                    .status(201)

                    .body(item);

        }

        catch(RuntimeException e){

            return ResponseEntity

                    .status(400)

                    .body(e.getMessage());

        }

    }


    // VIEW ALL ORDER ITEMS

    @GetMapping

    public ResponseEntity<List<OrderItemsDTO>>

    viewOrderItems(){

        return ResponseEntity.ok(

                orderItemService

                .viewOrderItems());

    }


    // SEARCH ORDER ITEM

    @GetMapping("/{id}")

    public ResponseEntity<Object>

    searchOrderItem(

            @PathVariable int id){

        Optional<OrderItemsDTO> item=

                orderItemService

                .searchOrderItem(id);

        if(item.isPresent()){

            return ResponseEntity

                    .ok(item.get());

        }

        return ResponseEntity

                .status(404)

                .body("Order Item Not Found!");

    }


    // GET ITEMS BY ORDER

    @GetMapping("/order/{orderId}")

    public ResponseEntity<List<OrderItemsDTO>>

    getByOrder(

            @PathVariable int orderId){

        return ResponseEntity.ok(

                orderItemService

                .getOrderItemsByOrder(orderId));

    }


    // DELETE ORDER ITEM

    @DeleteMapping("/{id}")

    public ResponseEntity<Object>

    deleteOrderItem(

            @PathVariable int id){

        try{

            orderItemService

            .deleteOrderItem(id);

            return ResponseEntity

                    .ok(

                    "Order Item Deleted Successfully");

        }

        catch(RuntimeException e){

            return ResponseEntity

                    .status(404)

                    .body(e.getMessage());

        }

    }

}
