package pac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pac.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
