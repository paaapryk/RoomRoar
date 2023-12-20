package pl.kurczoba.dashboard;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.kurczoba.rooms.Room;
import pl.kurczoba.rooms.RoomRepository;

import java.util.List;

@Controller
public class DashboardController{

    private final RoomRepository roomRepository;

    public DashboardController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Secured("ROLE_USER")
    @GetMapping("/user/dashboard")
    public String showCalendar(Model model) {
        List<Room> conferenceRooms = roomRepository.findAll();
        model.addAttribute("conferenceRooms", conferenceRooms);
        return "user/dashboard";

    }
}
