package com.userInventory.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userInventory.repository.*;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepo;


    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;
    
    @Autowired
    private OrderPaymentRepository paymentRepo;

    public Map<String,Object> getDashboard(){

        Map<String,Object> dashboard =
                new LinkedHashMap<>();

        dashboard.put("totalUsers",
                userRepo.count());

        dashboard.put("totalProducts",
                productRepo.count());

        dashboard.put("totalOrders",
                orderRepo.count());

        dashboard.put("pendingOrders",
                orderRepo.countByOrderStatus(
                        "Pending"));

        dashboard.put("confirmedOrders",
                orderRepo.countByOrderStatus(
                        "Confirmed"));

        dashboard.put("shippedOrders",
                orderRepo.countByOrderStatus(
                        "Shipped"));

        dashboard.put("deliveredOrders",
                orderRepo.countByOrderStatus(
                        "Delivered"));

        dashboard.put("cancelledOrders",
                orderRepo.countByOrderStatus(
                        "Cancelled"));

        return dashboard;
    }
    
    public Map<String,Object> getRevenueReport(){

        Map<String,Object> report =
                new LinkedHashMap<>();

        report.put(
                "totalRevenue",
                paymentRepo.getTotalRevenue());

        report.put(
                "successfulPayments",
                paymentRepo.countByPaymentStatus(
                        "SUCCESS"));

        report.put(
                "failedPayments",
                paymentRepo.countByPaymentStatus(
                        "FAILED"));

        report.put(
                "totalOrders",
                orderRepo.count());

        report.put(
                "deliveredOrders",
                orderRepo.countByOrderStatus(
                        "Delivered"));

        return report;
    }
}