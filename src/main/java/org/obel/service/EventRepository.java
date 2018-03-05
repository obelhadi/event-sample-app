package org.obel.service;

import org.obel.model.Event;

import java.util.List;

public interface EventRepository extends CrudRepository<Event> {
    List<Event> search(String keyword);
}
