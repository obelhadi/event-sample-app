package org.obel.config;

import org.elasticsearch.client.Client;
import org.obel.resource.EventResource;
import org.obel.service.EventRepository;
import org.obel.service.impl.EsEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EventRepository esEventService(Client client) {
        return new EsEventService(client);
    }

    @Bean
    public EventResource eventResource(EventRepository eventRepository) {
        return new EventResource(eventRepository);
    }

}
