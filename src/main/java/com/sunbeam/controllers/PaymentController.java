package com.sunbeam.controllers;

import com.sunbeam.daos.CartItemRepository;
import com.sunbeam.daos.CartRepository;
import com.sunbeam.entities.*;
import com.sunbeam.models.PaymentMethod;
import com.sunbeam.response.ApiResponse;
import com.sunbeam.response.PaymentLinkResponse;
import com.sunbeam.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;



    @PostMapping("/api/payment/{paymentMethod}/order/{orderId}")
    public ResponseEntity<PaymentLinkResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentLinkResponse paymentResponse;

        PaymentOrder order= paymentService.getPaymentOrderById(orderId);

//        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
//            paymentResponse=paymentService.createRazorpayPaymentLink(user,
//                    order.getAmount(),
//                    order.getId());
//        }
//        else{
//            paymentResponse=paymentService.createStripePaymentLink(user,
//                    order.getAmount(),
//                    order.getId());
//        }

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/success")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @RequestParam("payment_order_id") Long paymentOrderId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentOrderId);

        // In a real app, paymentId and paymentLinkId would come from the gateway callback
        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, "mock_payment_id", "mock_link_id");
        
        ApiResponse res = new ApiResponse();

        if (paymentSuccess) {
            for (Orders order : paymentOrder.getOrders()) {
                if(order.getSellerId() != null) {
                    transactionService.createTransaction(order);
                    Seller seller = sellerService.getSellerById(order.getSellerId());
                    SellerReport report = sellerReportService.getSellerReport(seller);
                    report.setTotalOrders(report.getTotalOrders() + 1);
                    report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                    report.setTotalSales(report.getTotalSales() + order.getOrderitems().size());
                    sellerReportService.updateSellerReport(report);
                }
            }

            Cart cart = cartRepository.findByUserId(user.getId());
            if (cart != null) {
                cart.setCouponCode(null);
                cart.setCouponDiscount(0);
                if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
                    cartItemRepository.deleteAll(cart.getCartItems());
                    cart.setCartItems(new HashSet<>());
                }
                cartRepository.save(cart);
            }

            res.setMessage("Payment successful");
            res.setStatus(true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            res.setMessage("Payment failed");
            res.setStatus(false);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }
}