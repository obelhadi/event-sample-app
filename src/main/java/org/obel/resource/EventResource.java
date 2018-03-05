package org.obel.resource;

import org.obel.model.Event;
import org.obel.service.EventRepository;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/event")
public class EventResource {


    private final EventRepository eventRepository;

    public EventResource(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GET
    public Response getAllEvents() {
        final List<Event> allEvents = eventRepository.findAll();
        return !allEvents.isEmpty() ? Response.ok(allEvents).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/search")
    public Response searchEvent(@QueryParam("query") String keyword) {
        return Response.ok(eventRepository.search(keyword)).build();
    }

    @GET
    @Path("/{id}")
    public Response getEventById(@PathParam("id") String eventId) {
        return eventRepository.find(eventId)
                .map(event -> Response.ok(event).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEvent(Event event) {
        final Event savedEvent = eventRepository.save(event);
        return Response.ok(savedEvent).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEvent(Event event) {
        eventRepository.update(event);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEventById(@PathParam("id") String eventId) {
        eventRepository.delete(eventId);
        return Response.ok().build();
    }


}
