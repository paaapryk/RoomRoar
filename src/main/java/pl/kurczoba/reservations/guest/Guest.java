package pl.kurczoba.reservations.guest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.kurczoba.reservations.Reservation;

@Getter
@Setter
@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}
