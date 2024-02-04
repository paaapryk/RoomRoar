package pl.kurczoba.microsoft.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import pl.kurczoba.role.Role;
import pl.kurczoba.role.RoleRepository;
import pl.kurczoba.users.User;
import pl.kurczoba.users.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        System.out.println("Attributes from Microsoft: " + attributes);

        String email = (String) attributes.get("email");
        Optional<User> userOptional = userRepository.findByEmail(email);

        try {
            if (!userOptional.isPresent()) {
                User user = new User();
                user.setEmail(email);
                user.setFirstName((String) attributes.get("given_name"));
                user.setLastName((String) attributes.get("family_name"));

                Role defaultRole = roleRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Default role not found"));
                user.setRole(defaultRole);

                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                userRepository.save(user);
            }
        } catch (Exception e) {
            OAuth2Error oauth2Error = new OAuth2Error("user_processing_error",
                    "Error processing user data: " + e.getMessage(),
                    null);
            throw new OAuth2AuthenticationException(oauth2Error, e.getMessage(), e);
        }

        return oidcUser;
    }
}
