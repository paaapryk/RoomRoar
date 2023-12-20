package pl.kurczoba.rooms;


import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kurczoba.reservations.Reservation;
import pl.kurczoba.reservations.ReservationRepository;
import pl.kurczoba.reservations.guest.Guest;
import pl.kurczoba.reservations.guest.GuestRepository;
import pl.kurczoba.reservations.participant.Participant;
import pl.kurczoba.reservations.participant.ParticipantRepository;
import pl.kurczoba.users.User;
import pl.kurczoba.users.UserRepository;

import java.util.List;

@Controller

public class RoomController {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    private final ParticipantRepository participantRepository;

    private final GuestRepository guestRepository;

    public RoomController(RoomRepository roomRepository, ReservationRepository reservationRepository, UserRepository userRepository, ParticipantRepository participantRepository, GuestRepository guestRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.guestRepository = guestRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Room> conferenceRooms = roomRepository.findAll();
        model.addAttribute("conferenceRooms", conferenceRooms);
        return "room/list";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/room/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room/add";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/room/add")
    public String addRoom(@ModelAttribute("room") Room room) {

        roomRepository.save(room);

        return "redirect:/admin/equipment/add";
    }

    @Secured("ROLE_USER")

    @GetMapping("/user/room/details/{id}")
    public String roomDetails(@PathVariable("id") Long roomId, Model model) {
        Room room = roomRepository.findById(roomId)
                .orElse(null);

        if (room == null) {

            return "errorPage";
        }

        // Pobierz informacje o zalogowanym użytkowniku
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User loggedInUser = userRepository.findByEmail(email).orElse(null);

        // Dodaj dane do modelu
        model.addAttribute("room", room);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("loggedInUserId", loggedInUser != null ? loggedInUser.getUserId() : null);
        model.addAttribute("reservation", new Reservation());
        return "room/details";
    }

    @Secured("ROLE_USER")
    @PostMapping("/user/room/details/{id}")
    public String submitReservation(@ModelAttribute Reservation reservation,
                                    @RequestParam(required = false) List<Long> participantIds,
                                    @RequestParam(required = false) List<Guest> guests,
                                    @PathVariable Long id,
                                    RedirectAttributes redirectAttrs) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            return "errorPage";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User loggedInUser = userRepository.findByEmail(email).orElse(null);

        // Przetwarzanie uczestników
        if (participantIds != null) {
            for (Long participantId : participantIds) {
                User user = userRepository.findById(participantId).orElse(null);
                if (user != null) {
                    Participant participant = new Participant();
                    participant.setUser(user);
                    participant.setReservation(reservation);
                    reservation.getParticipants().add(participant);
                }
            }
        }

        // Przetwarzanie gości
        if (guests != null) {
            for (Guest guest : guests) {
                if (guest.getFullname() != null && !guest.getFullname().isEmpty()) {
                    guest.setReservation(reservation);
                    reservation.getGuests().add(guest);
                }
            }
        }

        reservation.setUser(loggedInUser);
        reservation.setRoom(room);

        // Walidacja pojemności sali
        int totalParticipants = reservation.getParticipants().size() + reservation.getGuests().size();
        if (totalParticipants > room.getCapacity()) {
            redirectAttrs.addFlashAttribute("error", "Liczba uczestników i gości przekracza pojemność sali.");
            return "redirect:/user/room/details/" + id;
        }

        reservationRepository.save(reservation);
        return "redirect:/user/dashboard";
    }
}

