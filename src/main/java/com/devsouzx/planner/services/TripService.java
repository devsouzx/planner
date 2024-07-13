package com.devsouzx.planner.services;

import com.devsouzx.planner.infra.exceptions.EntityNotFoundException;
import com.devsouzx.planner.infra.exceptions.TripException;
import com.devsouzx.planner.model.trip.Trip;
import com.devsouzx.planner.model.trip.dtos.TripData;
import com.devsouzx.planner.model.trip.dtos.TripRequestPayload;
import com.devsouzx.planner.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public Trip registerTrip(TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        if (newTrip.getEndsAt().isBefore(newTrip.getStartsAt())) {
            throw new TripException("The end date must be after the start date.");
        }

        return this.tripRepository.save(newTrip);
    }

    public Optional<Trip> findById(UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isEmpty()) {
            throw new EntityNotFoundException("Trip not found");
        }

        return trip;
    }

    public void save(Trip trip) {
        this.tripRepository.save(trip);
    }

    public TripData findTrip(UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isEmpty()) {
            throw new EntityNotFoundException("Trip not found");
        }

        Trip rawTrip = trip.get();

        return new TripData(rawTrip.getId(), rawTrip.getDestination(), rawTrip.getStartsAt(), rawTrip.getEndsAt(), rawTrip.getIsConfirmed(), rawTrip.getOwnerName(), rawTrip.getOwnerEmail());
    }
}
