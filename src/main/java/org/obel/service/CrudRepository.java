package org.obel.service;


import org.obel.model.HasId;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends HasId> {
    T save(T item);
    void update(T item);
    void delete(String itemId);
    Optional<T> find(String itemId);
    List<T> findAll();
}
