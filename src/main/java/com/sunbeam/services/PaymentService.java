package com.sunbeam.services;

import java.util.Set;
import com.sunbeam.entities.Orders;
import com.sunbeam.entities.PaymentOrder;
import com.sunbeam.entities.User;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Orders> orders);
    PaymentOrder getPaymentOrderById(Long id) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception;
    boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId);
}