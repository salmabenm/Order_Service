package com.project.microservices.order_service.service;

import com.project.microservices.order_service.client.InventoryClient;
import com.project.microservices.order_service.dto.OrderRequest;
import com.project.microservices.order_service.model.Order;
import com.project.microservices.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(),orderRequest.quantity());

        if(isProductInStock){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);
        }else{
            throw new RuntimeException("Product with Skucode"+ orderRequest.skuCode() + "is not in stock");
        }


    }
}
