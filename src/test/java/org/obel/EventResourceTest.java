package org.obel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.obel.model.Event;
import org.obel.resource.EventResource;
import org.obel.service.EventRepository;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventResourceTest {

    @Mock
    EventRepository eventRepository;

    EventResource eventResource;


    @Before
    public void setUp() {
        eventResource = new EventResource(eventRepository);
    }

    @Test
    public void should_save_and_return_saved_event() {
        // Given
        Event event = anEvent("Test 1", "Nanterre");
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        final Response response = eventResource.createEvent(event);

        // Then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void should_update_event() {
        // Given
        Event event = anEvent("Test 1", "Nanterre");

        // When
        final Response response = eventResource.updateEvent(event);

        // Then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        verify(eventRepository, times(1)).update(event);
    }

    @Test
    public void should_return_event_by_id() {
        // Given
        final String eventId = "1";
        Event event = anEvent("Test 1", "Nanterre");
        when(eventRepository.find(anyString())).thenReturn(Optional.of(event));

        // When
        final Response response = eventResource.getEventById(eventId);

        // Then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        Event foundEvent = (Event) response.getEntity();
        assertThat(foundEvent, is(event));
        verify(eventRepository, times(1)).find(eventId);
    }

    @Test
    public void should_return_not_found_status_if_event_id_not_found() {
        // Given
        final String eventId = "1";
        when(eventRepository.find(eq(eventId))).thenReturn(Optional.empty());

        // When

        final Response response = eventResource.getEventById(eventId);

        // Then
        assertThat(response.getStatus(), is(HttpStatus.NOT_FOUND.value()));
        verify(eventRepository, times(1)).find(eventId);
    }



    @Test
    public void should_return_all_events() {
        // Given
        List<Event> events = Arrays.asList(anEvent("Test 1", "Paris"), anEvent("Test 2", "Paris"));
        when(eventRepository.findAll()).thenReturn(events);

        // When
        final Response response = eventResource.getAllEvents();

        // Then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        List<Event> foundEvents = (List<Event>) response.getEntity();
        assertThat(foundEvents, is(events));
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void should_return_not_found_status_if_no_events_found() {
        // Given
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        final Response response = eventResource.getAllEvents();

        // Then
        assertThat(response.getStatus(), is(HttpStatus.NOT_FOUND.value()));
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void should_delete_an_event_by_id() {
        // When
        final String eventId = "1";
        final Response response = eventResource.deleteEventById(eventId);

        // Given
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        verify(eventRepository, times(1)).delete(eventId);
    }




    private Event anEvent(String name, String city) {
        Event event = new Event();
        event.setName(name);
        event.setCity(city);
        return event;
    }

}