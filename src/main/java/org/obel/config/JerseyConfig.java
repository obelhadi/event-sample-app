package org.obel.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.obel.resource.EventResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/rest")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
        register(EventResource.class);
	}

}