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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    // Extra meeting date and time
    private LocalDateTime extraMeetingDate;
    private LocalDateTime extraStartTime;
    private LocalDateTime extraEndTime;

    @Column(nullable = false)
    private String link;

    private Long accessId;

    private String password;

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

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingAttachment> attachments;

}
