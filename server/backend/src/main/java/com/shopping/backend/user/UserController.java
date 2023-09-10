package com.shopping.backend.user;

import com.shopping.entity.Role;
import com.shopping.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    private String templatePath = "user";

    @GetMapping
    public String getUsers(Model model) {
        List<User> users = service.getUsers();

        model.addAttribute("users", users);

        return templatePath + "/users";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        List<Role> roles = service.getRoles();

        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return templatePath + "/add";
    }

    @PostMapping("/addP")
    public String addUserP(User user, RedirectAttributes redirectAttributes) {
        service.save(user);

        redirectAttributes.addFlashAttribute("message", "성공적으로 저장되었습니다.");

        return "redirect:/users";
    }

}
