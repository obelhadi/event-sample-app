package org.obel.config;

import org.elasticsearch.client.Client;
import org.obel.service.impl.EsEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EsEventService esEventService(Client client) {
        return new EsEventService(client);
    }

}
