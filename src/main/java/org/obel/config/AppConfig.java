package org.obel.config;

import org.elasticsearch.client.Client;
import org.obel.resource.EventResource;
import org.obel.repository.EventRepository;
import org.obel.repository.impl.EsEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EventRepository esEventService(Client client) {
        return new EsEventRepository(client);
    }

    @Bean
    public EventResource eventResource(EventRepository eventRepository) {
        return new EventResource(eventRepository);
    }

}
