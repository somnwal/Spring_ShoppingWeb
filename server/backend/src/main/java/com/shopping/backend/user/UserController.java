package com.shopping.backend.user;

import com.shopping.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = service.getUsers();

        model.addAttribute("users", users);

        return "users";
    }
}
