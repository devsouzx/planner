package com.devsouzx.planner.model.trip.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripData(UUID id, String destination, LocalDateTime startsAt, LocalDateTime endsAt, Boolean isConfirmed, String ownerName, String ownerEmail) {
}
