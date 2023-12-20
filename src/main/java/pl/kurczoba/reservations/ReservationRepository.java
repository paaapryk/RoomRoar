package pl.kurczoba.reservations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoomRoomId(Long roomId);

    Optional<Reservation> findFirstByRoomRoomIdAndStartTimeBeforeAndEndTimeAfter(Long roomId, LocalDateTime now, LocalDateTime now1);

    Optional<Reservation> findFirstByRoomRoomIdAndStartTimeAfterOrderByStartTimeAsc(Long roomId, LocalDateTime now);

}
