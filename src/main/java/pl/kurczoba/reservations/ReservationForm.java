package pl.kurczoba.reservations;

import lombok.Getter;
import lombok.Setter;
import pl.kurczoba.reservations.guest.Guest;
import pl.kurczoba.reservations.participant.Participant;

import java.util.List;

@Getter
@Setter
public class ReservationForm {
    private Reservation reservation;
    private List<Participant> participants;
    private List<Guest> guests;
}
