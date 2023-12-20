document.addEventListener('DOMContentLoaded', function() {
    // Obsługa pokazywania/ukrywania listy uczestników
    document.getElementById('choose-participants-btn').addEventListener('click', function() {
        let participantsList = document.getElementById('participants-list');
        participantsList.style.display = participantsList.style.display === 'none' ? 'block' : 'none';
    });

    // Obsługa zmiany stylów uczestników
    let participants = document.querySelectorAll('#participants-list .participant');
    participants.forEach(function(participant) {
        let checkbox = participant.querySelector('.participant-checkbox');
        participant.addEventListener('click', function() {
            checkbox.checked = !checkbox.checked;
            updateParticipantStyle(participant, checkbox.checked);
        });
    });

    function updateParticipantStyle(participant, isChecked) {
        participant.style.backgroundColor = isChecked ? '#007bff' : '';
        participant.style.color = isChecked ? 'white' : '';
    }

    // Dodawanie gościa
    document.getElementById('add-guest-btn').addEventListener('click', function() {
        const guestsContainer = document.getElementById('guests-container');
        const newIndex = guestsContainer.querySelectorAll('.guest-input').length;
        const guestInput = document.createElement('div');
        guestInput.className = 'guest-input';
        guestInput.innerHTML = `
            <input type="text" name="guests[${newIndex}].fullname" placeholder="Imię i nazwisko gościa">
            <input type="email" name="guests[${newIndex}].email" placeholder="Email gościa (opcjonalnie)">
        `;
        guestsContainer.appendChild(guestInput);
    });

    // Przetwarzanie formularza
    document.querySelector('form').addEventListener('submit', function(event) {
        // Obsługa uczestników
        const selectedParticipantIds = Array.from(document.querySelectorAll('.participant-checkbox:checked')).map(checkbox => checkbox.value);
        selectedParticipantIds.forEach(id => {
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'participantIds';
            hiddenInput.value = id;
            this.appendChild(hiddenInput);
        });

        // Goście
        const guestInputs = document.querySelectorAll('.guest-input');
        guestInputs.forEach((guestInput, index) => {
            const fullnameInput = guestInput.querySelector(`input[type='text']`);
            const emailInput = guestInput.querySelector(`input[type='email']`);
            if (!fullnameInput.value.trim()) {
                guestInput.remove();
            } else {
                fullnameInput.name = `guests[${index}].fullname`;
                emailInput.name = `guests[${index}].email`;
            }
        });
    });
});