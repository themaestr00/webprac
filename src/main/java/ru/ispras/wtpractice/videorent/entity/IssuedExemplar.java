package ru.ispras.wtpractice.videorent.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.ispras.wtpractice.videorent.dbtypes.IssuedExemplarKey;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IssuedExemplar {
    @EmbeddedId
    private IssuedExemplarKey id;
}
