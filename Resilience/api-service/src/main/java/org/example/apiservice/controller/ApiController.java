package org.example.apiservice.controller;

import org.example.apiservice.service.UserClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserClientService service;

    public ApiController(UserClientService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<String> users() {

        return service.getUsers();

    }

}
