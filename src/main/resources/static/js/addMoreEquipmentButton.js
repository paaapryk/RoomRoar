
    document.addEventListener('DOMContentLoaded', function () {
    let equipmentCounter = 1;
    document.getElementById('addMoreEquipment').addEventListener('click', function () {
    let container = document.getElementById('equipmentFieldsContainer');
    let newField = document.createElement('div');
    newField.classList.add('form-group', 'equipment-field');
    newField.innerHTML = `
                <label for="itemName${equipmentCounter}">Nazwa wyposażenia</label>
                <input type="text" class="form-control" id="itemName${equipmentCounter}" name="equipment[${equipmentCounter}].itemName" placeholder="Wpisz nazwę wyposażenia">
            `;
    container.appendChild(newField);
    equipmentCounter++;
});
});
