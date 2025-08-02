package com.sunbeam.controllers;

import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sunbeam.entities.*;
import com.sunbeam.exceptions.*;
import com.sunbeam.models.PaymentMethod;
import com.sunbeam.response.PaymentLinkResponse;
import com.sunbeam.services.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;

    @PostMapping("/")
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Orders> orders = orderService.createOrder(user, shippingAddress, cart);
        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);
        PaymentLinkResponse res = new PaymentLinkResponse();

        // This is a placeholder as you don't have a real payment gateway implemented
        // In a real app, you would call paymentService.createRazorpayPaymentLink or similar
        res.setPayment_link_url("/mock-payment-page/" + paymentOrder.getId());
        res.setPayment_link_id(paymentOrder.getId().toString());
        
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Orders>> usersOrderHistoryHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Orders> orders = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        // You might want to add logic to ensure the user owns this order
        Orders order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId, @RequestHeader("Authorization") String jwt) throws Exception {
        // You might want to add logic to ensure the user owns this order item
        OrderItem orderItem = orderItemService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Orders> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException, SellerException {
        User user = userService.findUserProfileByJwt(jwt);
        Orders order = orderService.cancelOrder(orderId, user);
        
        if (order.getSellerId() != null) {
            Seller seller = sellerService.getSellerById(order.getSellerId());
            SellerReport report = sellerReportService.getSellerReport(seller);
            report.setCancelOrders(report.getCancelOrders() + 1);
            report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
            sellerReportService.updateSellerReport(report);
        }

        return ResponseEntity.ok(order);
    }
}