package ru.ispras.wtpractice.videorent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_full_name_phone_number",
                columnNames = {"full_name", "phone_number"}
        )
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE client SET is_deleted = true WHERE id = ?")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false, insertable = false, updatable = false)
    private Boolean is_deleted;
}
