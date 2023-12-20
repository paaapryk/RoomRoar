package pl.kurczoba.reservations.guest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByReservationReservationId(Long id);
}
