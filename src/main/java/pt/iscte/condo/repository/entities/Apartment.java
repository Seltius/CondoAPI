package pt.iscte.condo.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fraction;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; //TODO: WHO IS THE OWNER, IS THIS RELATION CORRECT?

}
