package pl.kurczoba.equipment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kurczoba.rooms.Room;
import pl.kurczoba.rooms.RoomRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/equipment")
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;

    private final RoomRepository roomRepository;

    public EquipmentController(EquipmentRepository equipmentRepository, RoomRepository roomRepository) {
        this.equipmentRepository = equipmentRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/add")
    public String showCreateForm(Model model) {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "roomId"));
        model.addAttribute("equipmentForm", new EquipmentForm());
        model.addAttribute("rooms", rooms);
        return "equipment/add";
    }

    @PostMapping
    public String createEquipment(@ModelAttribute EquipmentForm equipmentForm) {
        Room room = roomRepository.findById(equipmentForm.getRoomId()).orElseThrow();
        for (Equipment equipmentItem : equipmentForm.getEquipment()) {
            if (equipmentItem.getItemName() != null && !equipmentItem.getItemName().trim().isEmpty()) {
                equipmentItem.setRoom(room);
                equipmentRepository.save(equipmentItem);
            }
        }
        return "redirect:/user/dashboard";
    }
}

