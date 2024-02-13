package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.repository.entities.AIQuestion;

public interface AIQuestionRepository extends JpaRepository<AIQuestion, Integer> {

}