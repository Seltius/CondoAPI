package pt.iscte.condo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.iscte.condo.enums.DocumentType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Document {

    @Id
    @GeneratedValue()
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private LocalDateTime uploadDate;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User uploader;

    private byte[] fileData;

}
