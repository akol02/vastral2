package com.sunbeam.services.impl;

import com.sunbeam.daos.CartRepository;
import com.sunbeam.daos.OrderRepository;
import com.sunbeam.daos.PaymentOrderRepository;
import com.sunbeam.entities.Orders;
import com.sunbeam.entities.PaymentOrder;
import com.sunbeam.entities.User;
import com.sunbeam.models.PaymentOrderStatus;
import com.sunbeam.models.PaymentStatus;
import com.sunbeam.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Override
    public PaymentOrder createOrder(User user, Set<Orders> orders) {
        long amount = orders.stream().mapToLong(Orders::getTotalSellingPrice).sum();
        
        // This assumes a single cart for the user, which is a reasonable design.
        double couponDiscount = cartRepository.findByUserId(user.getId()).getCouponDiscount();

        PaymentOrder order = new PaymentOrder();
        order.setUser(user);
        order.setAmount((long) (amount - couponDiscount));
        order.setOrders(orders);
        order.setStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepository.save(order);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id)
                .orElseThrow(() -> new Exception("Payment order not found with id: " + id));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
        if (paymentOrder == null) {
            throw new Exception("Payment order not found with payment link id: " + paymentId);
        }
        return paymentOrder;
    }

    @Override
    public boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            // Since there is no payment gateway integration, we assume the payment is successful.
            for (Orders order : paymentOrder.getOrders()) {
                order.setPaymentStatus(PaymentStatus.COMPLETED);
                orderRepository.save(order);
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }
}