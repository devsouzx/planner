package com.devsouzx.planner.trip;

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

        return this.tripRepository.save(newTrip);
    }

    public Optional<Trip> findById(UUID id) {
        return this.tripRepository.findById(id);
    }

    public void save(Trip trip) {
        this.tripRepository.save(trip);
    }
}
