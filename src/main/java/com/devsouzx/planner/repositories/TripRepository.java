package com.devsouzx.planner.repositories;

import com.devsouzx.planner.model.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID>{
}
