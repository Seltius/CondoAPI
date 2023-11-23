package pt.iscte.condo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime meetingDate;

    @Column(nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    private String zoomLink;

    private String zoomMeetingId;

    private String zoomPassword;

}
