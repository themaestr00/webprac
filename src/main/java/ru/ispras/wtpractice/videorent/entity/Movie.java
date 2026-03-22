package ru.ispras.wtpractice.videorent.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(
            nullable = false,
            check = @CheckConstraint(constraint = "release_date >= '1900-01-01'")
    )
    private LocalDate releaseDate;

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private String company;
}
