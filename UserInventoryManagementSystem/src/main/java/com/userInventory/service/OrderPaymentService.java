package com.userInventory.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userInventory.dto.OrderPaymentDTO;
import com.userInventory.repository.OrderPaymentRepository;
import com.userInventory.repository.OrderRepository;
import com.userInventory.repository.entity.OrderPayment;
import com.userInventory.repository.entity.Orders;

@Service
public class OrderPaymentService {

    @Autowired
    private OrderPaymentRepository paymentRepo;

    @Autowired
    private OrderRepository orderRepo;


    // ENTITY TO DTO

    private OrderPaymentDTO convertToDTO(
            OrderPayment payment){

        OrderPaymentDTO dto=
                new OrderPaymentDTO();

        dto.setPaymentId(
                payment.getPaymentId());

        dto.setPaymentMethod(
                payment.getPaymentMethod());

        dto.setPaymentStatus(
                payment.getPaymentStatus());

        dto.setTransaction_id(
                payment.getTransactionId());

        dto.setAmountPaid(
                payment.getAmountPaid());

        dto.setPaymentDate(
                payment.getPaymentDate());

        if(payment.getOrder()!=null){

            dto.setOrder_id(
                    payment.getOrder()
                    .getOrderId());

        }

        return dto;

    }


    // DTO TO ENTITY

    private OrderPayment convertToEntity(
            OrderPaymentDTO dto){

        OrderPayment payment=
                new OrderPayment();

        payment.setPaymentMethod(
                dto.getPaymentMethod());

        payment.setPaymentStatus(
                dto.getPaymentStatus());

        payment.setTransactionId(
                dto.getTransaction_id());

        payment.setAmountPaid(
                dto.getAmountPaid());

        payment.setPaymentDate(
                dto.getPaymentDate());

        Orders order=
                orderRepo.findById(
                dto.getOrder_id())

                .orElseThrow(

                () -> new RuntimeException(
                "Order Not Found"));

        payment.setOrder(order);

        return payment;

    }


    // SAVE PAYMENT

    public OrderPaymentDTO savePayment(
            OrderPaymentDTO dto){

        Orders order = orderRepo
                .findById(dto.getOrder_id())
                .orElseThrow(
                () -> new RuntimeException(
                "Order Not Found"));

        OrderPayment payment =
                new OrderPayment();

        payment.setOrder(order);

        payment.setPaymentMethod(
                dto.getPaymentMethod());

        if("CASH_ON_DELIVERY".equalsIgnoreCase(
                dto.getPaymentMethod())) {

            payment.setPaymentStatus("PENDING");

        } else {

            payment.setPaymentStatus("SUCCESS");
        }

        payment.setTransactionId(
                dto.getTransaction_id());

        payment.setAmountPaid(
                dto.getAmountPaid());

        payment.setPaymentDate(
                LocalDateTime.now());

        OrderPayment savedPayment =
                paymentRepo.save(payment);

        // UPDATE ORDER STATUS

        if("SUCCESS".equalsIgnoreCase(
                dto.getPaymentStatus())) {

            order.setOrderStatus(
                    "Confirmed");

            orderRepo.save(order);
        }

        return convertToDTO(savedPayment);
    }


    // VIEW ALL PAYMENTS

    public List<OrderPaymentDTO>
    viewPayments(){

        return paymentRepo.findAll()

                .stream()

                .map(this::convertToDTO)

                .collect(Collectors.toList());

    }


    // SEARCH PAYMENT

    public Optional<OrderPaymentDTO>
    searchPayment(int id){

        return paymentRepo

                .findById(id)

                .map(this::convertToDTO);

    }


    // GET PAYMENT BY ORDER

    public List<OrderPaymentDTO>
    getPaymentByOrder(int orderId){

        return paymentRepo

                .findByOrder_OrderId(orderId)

                .stream()

                .map(this::convertToDTO)

                .collect(Collectors.toList());

    }


    // DELETE PAYMENT

    public void deletePayment(int id){

        OrderPayment payment=

                paymentRepo.findById(id)

                .orElseThrow(

                () -> new RuntimeException(

                "Payment Not Found"));

        paymentRepo.delete(payment);

    }

}