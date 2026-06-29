package org.example.orderservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/orders")
    public String orders(){
        return "Response from Order Service: "+port;
    }
}
