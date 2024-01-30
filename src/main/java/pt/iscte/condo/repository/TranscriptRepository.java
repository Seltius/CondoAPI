package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.domain.Transcript;
import pt.iscte.condo.enums.TranscriptStatus;

import java.util.List;
import java.util.Optional;

public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {

    Optional<List<Transcript>> findAllByStatus(TranscriptStatus status);

}