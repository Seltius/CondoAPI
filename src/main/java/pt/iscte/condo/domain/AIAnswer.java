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
public class AIAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String answer;

    @ManyToOne()
    @JoinColumn(name = "question_id", nullable = false)
    private AIQuestion question;

    @ManyToOne()
    @JoinColumn(name = "transcript_id", nullable = false)
    private MeetingTranscript transcript;

}
