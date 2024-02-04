package pl.kurczoba.role;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(UserRole.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(UserRole.ROLE_USER));
        }

        if (roleRepository.findByName(UserRole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(UserRole.ROLE_ADMIN));
        }
    }
}
