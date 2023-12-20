package pl.kurczoba.reservations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.kurczoba.reservations.guest.Guest;
import pl.kurczoba.reservations.participant.Participant;
import pl.kurczoba.rooms.Room;
import pl.kurczoba.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Participant> participants;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Guest> guests;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Reservation() {
        this.participants = new ArrayList<>();
        this.guests = new ArrayList<>();
    }
}
