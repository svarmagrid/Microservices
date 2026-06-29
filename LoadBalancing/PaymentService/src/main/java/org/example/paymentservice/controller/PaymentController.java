package org.example.paymentservice.controller;

import org.example.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/hello")
    public String hello(){
        return paymentService.hello();
    }

    @GetMapping("/pay")
    public String pay() {
        return paymentService.callOrderService();
    }

    @GetMapping("/pay/verify")
    public Map<String, Object> verifyLoadBalancing(
            @RequestParam(defaultValue = "9") int count) {
        return paymentService.verifyLoadBalancing(count);
    }
}
