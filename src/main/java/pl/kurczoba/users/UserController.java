package pl.kurczoba.users;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kurczoba.security.UserDto;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        User existingUser = userRepository.findByEmail(email).orElse(null);
        if (existingUser != null) {
            model.addAttribute("userDto", existingUser);
            return "user/edit/user";
        } else {
            return "errorPage";
        }
    }

    @PostMapping("/edit")
    public String updateUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (existingUser != null) {
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setEmail(userDto.getEmail());
            userRepository.save(existingUser);
            return "redirect:/user/dashboard";
        } else {
            return "errorPage";
        }
    }
    @GetMapping("/changePassword")
    public String showChangePasswordForm() {
        return "user/edit/password";
    }

    @PostMapping("/changePassword")
    public String changeUserPassword(String oldPassword, String newPassword, String confirmPassword, Authentication authentication) {
        String email = authentication.getName();
        User existingUser = userRepository.findByEmail(email).orElse(null);
        if (existingUser != null && passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            if (newPassword.equals(confirmPassword)) {
                existingUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(existingUser);
                return "redirect:/user/success";
            } else {
                return "redirect:/user/changePassword?error=passwordsDontMatch";
            }
        } else {
            return "redirect:/user/changePassword?error=invalidOldPassword";
        }
    }
    @GetMapping("/success")
    public String showSuccessPage() {
        return "user/success"; // nazwa widoku HTML
    }
}
