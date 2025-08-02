package com.sunbeam.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.sunbeam.daos.AddressRepository;
import com.sunbeam.daos.OrderItemRepository;
import com.sunbeam.daos.OrderRepository;
import com.sunbeam.entities.Address;
import com.sunbeam.entities.Cart;
import com.sunbeam.entities.CartItem;
import com.sunbeam.entities.OrderItem;
import com.sunbeam.entities.Orders;
import com.sunbeam.entities.User;
import com.sunbeam.exceptions.OrderException;
import com.sunbeam.models.OrderStatus;
import com.sunbeam.models.PaymentStatus;
import com.sunbeam.services.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Orders> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }
        addressRepository.save(shippingAddress);
        
        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        Set<Orders> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalMrp = items.stream().mapToInt(CartItem::getMrpPrice).sum();
            int totalSelling = items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItemCount = items.stream().mapToInt(CartItem::getQuantity).sum();

            Orders createdOrder = new Orders();
            createdOrder.setOrderId(UUID.randomUUID().toString());
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalMrp);
            createdOrder.setTotalSellingPrice(totalSelling);
            createdOrder.setTotalItem(totalItemCount);
            createdOrder.setShippingAddress(shippingAddress);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
            
            Orders savedOrder = orderRepository.save(createdOrder);
            
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setSellingPrice(item.getSellingPrice());
                orderItems.add(orderItemRepository.save(orderItem));
            }
            savedOrder.setOrderitems(orderItems);
            orders.add(orderRepository.save(savedOrder));
        }
        return orders;
    }

    @Override
    public Orders findOrderById(Long orderId) throws OrderException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with id: " + orderId));
    }

    @Override
    public List<Orders> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Orders> getShopsOrders(Long sellerId) {
        return orderRepository.findBySellerIdOrderByOrderDateDesc(sellerId);
    }

    @Override
    public Orders updateOrderStatus(Long orderId, OrderStatus orderStatus) throws OrderException {
        Orders order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public Orders cancelOrder(Long orderId, User user) throws OrderException {
        Orders order = findOrderById(orderId);
        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderException("You are not authorized to cancel this order.");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}