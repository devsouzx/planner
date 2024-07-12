package com.devsouzx.planner.participant;

import com.devsouzx.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToEvents(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.participantRepository.saveAll(participants);

        System.out.println(participants.getFirst().getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
    }

    public void triggerConfirmationEmailToParticipant(String email) {
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);
        return new ParticipantCreateResponse(participant.getId());
    }

    public List<ParticipantData> getAllParticipantsFromEvent(UUID id) {
        return this.participantRepository.findByTripId(id).stream().map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())).toList();
    }

    public Optional<Participant> findById(UUID id) {
        return this.participantRepository.findById(id);
    }

    public void save(Participant participant) {
        this.participantRepository.save(participant);
    }
}
