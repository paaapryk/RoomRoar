package pl.kurczoba.rooms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kurczoba.equipment.Equipment;
import pl.kurczoba.reservations.Reservation;
import pl.kurczoba.reservations.ReservationRepository;
import pl.kurczoba.reservations.guest.Guest;
import pl.kurczoba.reservations.guest.GuestRepository;
import pl.kurczoba.reservations.participant.Participant;
import pl.kurczoba.reservations.participant.ParticipantRepository;
import pl.kurczoba.users.User;
import pl.kurczoba.users.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/screen")
public class ScreenRoomController {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;


    public ScreenRoomController(ReservationRepository reservationRepository, RoomRepository roomRepository, ParticipantRepository participantRepository, GuestRepository guestRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.participantRepository = participantRepository;
        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/room/{id}")
    public String screen(@PathVariable("id") Long roomId, Model model) {
        Room room = roomRepository.findById(roomId)
                .orElse(null);
        if (room == null) {
            return "errorPage";
        }
        model.addAttribute("room", room);

        LocalDateTime now = LocalDateTime.now();

        // Sprawdź, czy trwa aktualne spotkanie
        Optional<Reservation> currentReservation = reservationRepository
                .findFirstByRoomRoomIdAndStartTimeBeforeAndEndTimeAfter(roomId, now, now);

        Reservation nextOrCurrentReservation;

        if (currentReservation.isPresent()) {
            // Użyj aktualnego spotkania
            nextOrCurrentReservation = currentReservation.get();
        } else {
            // Znajdź najbliższe nadchodzące spotkanie
            nextOrCurrentReservation = reservationRepository
                    .findFirstByRoomRoomIdAndStartTimeAfterOrderByStartTimeAsc(roomId, now)
                    .orElse(null);
        }

        model.addAttribute("nextReservation", nextOrCurrentReservation);

        return "screen/roomScreen";
    }

    @GetMapping("/room/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));

        Room room = roomRepository.findById(reservation.getRoom().getRoomId()).orElse(null);

        Set<Equipment> equipment = room != null ? room.getEquipment() : new HashSet<>();

        List<Participant> participants = participantRepository.findByReservationReservationId(id);

        List<Guest> guests = guestRepository.findByReservationReservationId(id);

        User user = userRepository.findById(reservation.getUser().getUserId()).orElse(null);

        model.addAttribute("reservation", reservation);
        model.addAttribute("room", room);
        model.addAttribute("equipment", equipment);
        model.addAttribute("participants", participants);
        model.addAttribute("guests", guests);
        model.addAttribute("user", user);
        return "screen/reservationDetails";
    }
}
