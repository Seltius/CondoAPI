package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.domain.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
