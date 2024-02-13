package pt.iscte.condo.repository.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.enums.InvitationStatus;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CondominiumInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "condominium_id")
    private Condominium condominium;

    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @ManyToOne
    @JoinColumn(name = "invitee_id")
    private User invitee;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

}
