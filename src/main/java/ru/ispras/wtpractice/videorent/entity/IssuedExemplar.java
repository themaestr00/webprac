package ru.ispras.wtpractice.videorent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import ru.ispras.wtpractice.videorent.dbtypes.IssuedExemplarKey;

@Entity
@Table(name = "issued_exemplar")
@Immutable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IssuedExemplar {
    @EmbeddedId
    private IssuedExemplarKey id;
}
