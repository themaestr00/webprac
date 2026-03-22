package ru.ispras.wtpractice.videorent.dbtypes;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IssuedExemplarKey implements Serializable {
    private Long clientId;
    private Long exemplarId;
}
