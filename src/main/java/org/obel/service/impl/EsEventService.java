package org.obel.service.impl;

import org.elasticsearch.client.Client;
import org.obel.model.Event;
import org.obel.service.EventRepository;

public class EsEventService extends EsCrudRepository<Event> implements EventRepository{

    private static String TYPE = "event";
    private static String INDEX_NAME = "test";

    public EsEventService(Client client) {
        super(client, TYPE, INDEX_NAME);
    }
}
