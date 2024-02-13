package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.repository.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
