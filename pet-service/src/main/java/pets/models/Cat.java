package pets.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    private String breed;
    private String colour;

    private Long ownerId;

    @ElementCollection
    @CollectionTable(name = "cat_friends", joinColumns = @JoinColumn(name = "cat_id"))
    @Column(name = "friend_id")
    @ToString.Exclude
    private List<Long> friendsId;
}
