package pl.kurczoba.role;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurczoba.users.UserRole;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);
}
