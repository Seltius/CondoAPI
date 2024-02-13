package pt.iscte.condo.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String zoomLink;

    private Long zoomMeetingId;

    private String zoomPassword;

    @ManyToOne
    @JoinColumn(name = "organizerId", nullable = false)
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "secretaryId")
    private User secretary;

    @ManyToOne
    @JoinColumn(name = "condominiumId", nullable = false)
    private Condominium condominium;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingTopic> topics;

}
