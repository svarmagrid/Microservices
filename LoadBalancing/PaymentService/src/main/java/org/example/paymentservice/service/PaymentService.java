package org.example.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private RestTemplate restTemplate;

    public String hello(){
        return "hello";
    }

    public String callOrderService(){
        return restTemplate.getForObject(
                        "http://Orderservice/orders",
                String.class
        );
    }

    public Map<String, Object> verifyLoadBalancing(int count) {
        Map<String, Integer> distribution = new LinkedHashMap<>();

        for (int i = 0; i < count; i++) {
            String response = callOrderService();
            distribution.merge(response, 1, Integer::sum);
        }

        return Map.of(
                "totalRequests", count,
                "distribution", distribution
        );
    }
}
