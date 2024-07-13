package com.devsouzx.planner.services;

import com.devsouzx.planner.model.link.Link;
import com.devsouzx.planner.model.link.dtos.LinkData;
import com.devsouzx.planner.model.link.dtos.LinkRequestPayload;
import com.devsouzx.planner.model.link.dtos.LinkResponse;
import com.devsouzx.planner.model.trip.Trip;
import com.devsouzx.planner.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;

    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);

        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<LinkData> getAllLinksFromTrip(UUID tripId){
        return this.linkRepository.findByTripId(tripId).stream().map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
