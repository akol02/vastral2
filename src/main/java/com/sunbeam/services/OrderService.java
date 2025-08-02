package com.sunbeam.services;

import java.util.List;
import java.util.Set;
import com.sunbeam.entities.Address;
import com.sunbeam.entities.Cart;
import com.sunbeam.entities.Orders;
import com.sunbeam.entities.User;
import com.sunbeam.exceptions.OrderException;
import com.sunbeam.models.OrderStatus;

public interface OrderService {
    Set<Orders> createOrder(User user, Address shippingAddress, Cart cart);
    Orders findOrderById(Long orderId) throws OrderException;
    List<Orders> usersOrderHistory(Long userId);
    List<Orders> getShopsOrders(Long sellerId);
    Orders updateOrderStatus(Long orderId, OrderStatus orderStatus) throws OrderException;
    void deleteOrder(Long orderId) throws OrderException;
    Orders cancelOrder(Long orderId, User user) throws OrderException;
}