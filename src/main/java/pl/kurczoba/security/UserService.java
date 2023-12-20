package pl.kurczoba.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kurczoba.exception.UserAlreadyExistException;
import pl.kurczoba.role.RoleRepository;
import pl.kurczoba.users.User;
import pl.kurczoba.users.UserRepository;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void registerNewUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            // Obsługa sytuacji, gdy użytkownik z tym adresem email już istnieje
            throw new UserAlreadyExistException("There is an account with that email address: " + userDto.getEmail());
        }


        User newUser = new User();
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(roleRepository.findById(1L).orElseThrow());

        userRepository.save(newUser);
    }
}
