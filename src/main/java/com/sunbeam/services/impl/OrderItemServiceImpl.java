package com.sunbeam.services.impl;
import org.springframework.stereotype.Service;
import com.sunbeam.daos.OrderItemRepository;
import com.sunbeam.entities.OrderItem;
import com.sunbeam.exceptions.OrderException;
import com.sunbeam.services.OrderItemService;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

@Override
public OrderItem getOrderItemById(Long id) throws OrderException {
    return orderItemRepository.findById(id)
            .orElseThrow(() -> new OrderException("Order item not found with id: " + id));
}
}