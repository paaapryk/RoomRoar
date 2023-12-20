package pl.kurczoba.reservations.participant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.kurczoba.reservations.Reservation;
import pl.kurczoba.users.User;

@Getter
@Setter
@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
