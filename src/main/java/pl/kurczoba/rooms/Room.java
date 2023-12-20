package pl.kurczoba.rooms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.kurczoba.equipment.Equipment;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Equipment> equipment;

}
