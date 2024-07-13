package com.devsouzx.planner.controllers;

import com.devsouzx.planner.model.activity.dtos.ActivityData;
import com.devsouzx.planner.model.activity.dtos.ActivityRequestPayload;
import com.devsouzx.planner.model.activity.dtos.ActivityResponse;
import com.devsouzx.planner.model.trip.dtos.TripData;
import com.devsouzx.planner.services.ActivityService;
import com.devsouzx.planner.model.link.dtos.LinkData;
import com.devsouzx.planner.model.link.dtos.LinkRequestPayload;
import com.devsouzx.planner.model.link.dtos.LinkResponse;
import com.devsouzx.planner.services.LinkService;
import com.devsouzx.planner.model.participant.dtos.ParticipantCreateResponse;
import com.devsouzx.planner.model.participant.dtos.ParticipantData;
import com.devsouzx.planner.model.participant.dtos.ParticipantRequestPayload;
import com.devsouzx.planner.services.ParticipantService;
import com.devsouzx.planner.model.trip.Trip;
import com.devsouzx.planner.model.trip.dtos.TripCreateResponse;
import com.devsouzx.planner.model.trip.dtos.TripRequestPayload;
import com.devsouzx.planner.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = this.tripService.registerTrip(payload);

        this.participantService.registerParticipantsToEvents(payload.emails_to_invite(), newTrip);
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripData> getTripDetails(@PathVariable UUID id) {
        TripData trip = this.tripService.findTrip(id);

        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.tripService.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setDestination(payload.destination());
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));

            this.tripService.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripService.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);

            this.tripService.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripService.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            ParticipantCreateResponse participantResponse  = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if(rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participants = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.tripService.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.tripService.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(linkDataList);
    }
}
