package demo.demo.amazonAws.repository;

import demo.demo.amazonAws.model.Role;
import demo.demo.amazonAws.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role>findByName(RoleName roleName);

}
