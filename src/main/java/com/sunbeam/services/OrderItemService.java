package com.sunbeam.services;

import com.sunbeam.entities.OrderItem;
import com.sunbeam.exceptions.OrderException;

public interface OrderItemService {
    OrderItem getOrderItemById(Long id) throws OrderException;
}