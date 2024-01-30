package pt.iscte.condo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.enums.TranscriptStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transcript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;

    @ManyToOne //TODO IN CASE OF A TRANSCRIPT COMES FROM A FILE INSTEAD OF A MEETING PROVIDER ???
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    @Enumerated(EnumType.STRING)
    private TranscriptStatus status;

}