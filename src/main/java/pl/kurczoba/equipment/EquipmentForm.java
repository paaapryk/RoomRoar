package pl.kurczoba.equipment;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EquipmentForm {
    private Long roomId;
    private List<Equipment> equipment;

}

