package pl.kurczoba.calendar;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kurczoba.reservations.Reservation;
import pl.kurczoba.reservations.ReservationRepository;
import pl.kurczoba.rooms.Room;
import pl.kurczoba.rooms.RoomRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CalendarController {

    private final ReservationRepository reservationRepository;

    public CalendarController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations/events")
    public List<ReservationEvent> getReservations(@RequestParam Long roomId) {
        return reservationRepository.findByRoomRoomId(roomId).stream()
                .map(this::convertToReservationEvent)
                .collect(Collectors.toList());
    }

    @GetMapping("/all-reservations/events")
    public List<ReservationEvent> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToReservationEvent)
                .collect(Collectors.toList());
    }

    private ReservationEvent convertToReservationEvent(Reservation reservation) {
        ReservationEvent event = new ReservationEvent();
        event.setId(reservation.getReservationId());
        event.setTitle(reservation.getTitle());
        event.setStart(reservation.getStartTime().toString());
        event.setEnd(reservation.getEndTime().toString());

        return event;
    }



    // Klasa reprezentująca wydarzenie w kalendarzu
    @Getter
    @Setter
    static class ReservationEvent {
        private Long id;
        private String title;
        private String start;
        private String end;

    }
}
