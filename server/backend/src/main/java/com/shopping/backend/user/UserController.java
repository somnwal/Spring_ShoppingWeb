package com.shopping.backend.user;

import com.shopping.backend.util.FileUploadUtil;
import com.shopping.entity.Role;
import com.shopping.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    private String templatePath = "user";

    @GetMapping
    public String getUsers(Model model) {
        return getUsersByPaging(1, model);
    }

    @GetMapping("/page/{pageNum}")
    public String getUsersByPaging(@PathVariable(name="pageNum") int pageNum, Model model) {
        Page<User> page = service.getUsersByPaging(pageNum);

        List<User> users = page.getContent();

        long startCount = (pageNum - 1) * UserService.PAGE_SIZE + 1;
        long endCount = startCount + UserService.PAGE_SIZE - 1;

        if( endCount > page.getTotalElements() ) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("maxPageNum", page.getTotalPages());

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("ttcn", page.getTotalElements());
        model.addAttribute("users", users);

        return templatePath + "/users";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        List<Role> roles = service.getRoles();

        User user = new User();

        model.addAttribute("title", "사용자 추가");
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return templatePath + "/form";
    }

    @PostMapping("/addP")
    public String addUserP(User user, RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhoto(fileName);

            User savedUser = service.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhoto().isEmpty()) {
                user.setPhoto(null);
                service.save(user);
            }
        }

        redirectAttributes.addFlashAttribute("message", "성공적으로 저장되었습니다.");

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = service.getUser(id);
            List<Role> roles = service.getRoles();

            model.addAttribute("title", "사용자 편집");
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);

            return templatePath + "/form";
        } catch (Exception e ) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/users";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = service.getUser(id);

            service.deleteUser(id);

            redirectAttributes.addFlashAttribute("message", "성공적으로 삭제 되었습니다.");
            return "redirect:/users";
        } catch (Exception e ) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users";
        }
    }
}
