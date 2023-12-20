package pl.kurczoba.security;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.kurczoba.exception.UserAlreadyExistException;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService; // Załóżmy, że UserService zawiera logikę rejestracji

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "security/register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            // Obsługa błędów walidacji
            return "security/register";
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "userDto.confirmPassword", "Passwords must match");
            return "register";
        }
        try {
            userService.registerNewUser(userDto);
        } catch (UserAlreadyExistException e) {
            // Obsługa błędu, gdy użytkownik już istnieje
            result.rejectValue("email", "user.email", "An account already exists for this email.");
            return "security/register";
        }

        return "redirect:/login";
    }
}