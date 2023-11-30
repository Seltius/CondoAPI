package pt.iscte.condo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToOne
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String zoomLink;

    private Long zoomMeetingId;

    private String zoomPassword;

    @ManyToMany
    private List<User> users = new ArrayList<>();

}
