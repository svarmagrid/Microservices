package org.example.userservice.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    @GetMapping
    public List<String> getUsers() {

        return List.of(
                "John",
                "Alice",
                "Bob"
        );
    }

}
