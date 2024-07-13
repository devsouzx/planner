package com.devsouzx.planner.model.participant.dtos;

import java.util.UUID;

public record ParticipantData(UUID id, String name, String email, Boolean isConfirmed) {
}
