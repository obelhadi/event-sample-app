package org.obel.repository;

import org.obel.model.Event;

import java.util.List;

public interface EventRepository extends CrudRepository<Event> {
    List<Event> search(String keyword);
}
