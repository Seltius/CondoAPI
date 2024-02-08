package pt.iscte.condo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MeetingTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String description;

    private String aiDescription;

    @ManyToOne
    @JoinColumn(name = "meetingId", nullable = false)
    private Meeting meeting;


}
