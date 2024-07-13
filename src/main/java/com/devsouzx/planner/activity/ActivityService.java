package com.devsouzx.planner.activity;

import com.devsouzx.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip rawTrip) {
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), rawTrip);

        if (newActivity.getOccursAt().isAfter(rawTrip.getEndsAt()) || newActivity.getOccursAt().isBefore(rawTrip.getStartsAt())) {
            throw new ActivityException("The activity date must be between the trip start and end dates.");
        }

        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId){
        return this.activityRepository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
