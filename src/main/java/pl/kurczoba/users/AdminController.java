package pl.kurczoba.users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kurczoba.role.Role;
import pl.kurczoba.role.RoleRepository;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @ModelAttribute("roles")
    public List<Role> populateRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/user-edit/{userId}")
    public String showEditUserForm(@PathVariable("userId") Long userId, Model model) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        model.addAttribute("user", user);
        return "admin/editUsers";
    }

    @PostMapping("/user-edit/{userId}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("userId") Long userId) {
        user.setUserId(userId);
        userRepository.save(user);
        return "redirect:/user/success";
    }
}