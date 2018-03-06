package org.obel.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.obel.repository.impl.EsEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class AppStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    Client client;

    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        IndicesAdminClient indicesClient = client.admin().indices();
        final String indexName = EsEventRepository.INDEX_NAME;
        IndicesExistsResponse existsResponse = indicesClient.prepareExists(indexName).get();
        if (!existsResponse.isExists()) {
            try {
                log.info("Creating index [{}] with event mapping ... ", indexName);
                final Resource resource = resourceLoader.getResource("classpath:mapping/eventMapping.json");
                final String mappingJson = FileUtils.readFileToString(resource.getFile(), UTF_8);
                final CreateIndexResponse createIndexResponse = indicesClient.prepareCreate(indexName)
                        .addMapping(EsEventRepository.TYPE, mappingJson, XContentType.JSON)
                        .get();
                if (createIndexResponse.isAcknowledged()) {
                    log.info("Index [{}] and event mapping created with success ", indexName);
                }

            } catch (Exception e) {
                log.error("Error while creating index", e);
            }

        }
    }
}
