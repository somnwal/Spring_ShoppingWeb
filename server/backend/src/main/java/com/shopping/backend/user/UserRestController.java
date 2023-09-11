package com.shopping.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    @Autowired
    private UserService service;

    @PostMapping("/users/check_email")
    public String checkEmail(@Param("id") Long id, @Param("email") String email) {
        return !service.isEmailExists(id, email) ? "OK" : "EXIST";
    }
}
