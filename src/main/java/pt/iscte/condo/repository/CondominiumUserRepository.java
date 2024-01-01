package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.domain.CondominiumUser;

public interface CondominiumUserRepository extends JpaRepository<CondominiumUser, Integer> {


}
