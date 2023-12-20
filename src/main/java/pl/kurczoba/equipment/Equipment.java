package pl.kurczoba.equipment;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.kurczoba.rooms.Room;

@Getter
@Setter
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}
