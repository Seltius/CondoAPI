package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.domain.MeetingTranscript;
import pt.iscte.condo.enums.TranscriptStatus;

import java.util.List;
import java.util.Optional;

public interface MeetingTranscriptRepository extends JpaRepository<MeetingTranscript, Integer> {

    Optional<List<MeetingTranscript>> findAllByStatus(TranscriptStatus status);

}