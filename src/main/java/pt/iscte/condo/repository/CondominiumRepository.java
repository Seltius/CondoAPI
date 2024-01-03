package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.domain.Condominium;

public interface CondominiumRepository extends JpaRepository<Condominium, Integer> {


}