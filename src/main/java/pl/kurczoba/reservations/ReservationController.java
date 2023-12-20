package pl.kurczoba.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kurczoba.equipment.Equipment;
import pl.kurczoba.exception.ResourceNotFoundException;
import pl.kurczoba.reservations.guest.Guest;
import pl.kurczoba.reservations.guest.GuestRepository;
import pl.kurczoba.reservations.participant.Participant;
import pl.kurczoba.reservations.participant.ParticipantRepository;
import pl.kurczoba.rooms.Room;
import pl.kurczoba.rooms.RoomRepository;
import pl.kurczoba.users.User;
import pl.kurczoba.users.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;


    public ReservationController(ReservationRepository reservationRepository, RoomRepository roomRepository, ParticipantRepository participantRepository, GuestRepository guestRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.participantRepository = participantRepository;
        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/reservation/details/{id}")
    public String showReservationDetails(@PathVariable Long id, Model model) {

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

        return "reservation/details";
    }
}

