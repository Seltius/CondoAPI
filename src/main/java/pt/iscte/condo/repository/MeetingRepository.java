package pt.iscte.condo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iscte.condo.repository.entities.Meeting;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Integer>  {

    List<Meeting> findAllByCondominiumId(Integer condominiumId);

}
