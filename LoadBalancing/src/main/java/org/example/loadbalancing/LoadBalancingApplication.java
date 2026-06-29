package org.example.loadbalancing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LoadBalancingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancingApplication.class, args);
    }

}
