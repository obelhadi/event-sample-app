package org.obel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.obel.exception.ElasticSearchQueryException;
import org.obel.exception.JsonParsingException;
import org.obel.model.HasId;
import org.obel.service.CrudRepository;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class EsCrudRepository<T extends HasId> implements CrudRepository<T> {

    private final Client client;
    private final String type;
    private final String indexName;


    public EsCrudRepository(Client client, String type, String indexName) {
        this.client = client;
        this.type = type;
        this.indexName = indexName;
    }


    @Override
    public T save(T item) {
        item.setId(UUID.randomUUID().toString());
        try {
            return toJson(item).map(jsonValue ->
                    client.prepareIndex(indexName, type, item.getId())
                            .setSource(jsonValue, XContentType.JSON)
                            .execute()
                            .actionGet())
                    .map(DocWriteResponse::getId)
                    .map(id -> addItemId(item, id))
                    .orElse(item);
        } catch (JsonParsingException je) {
            throw je;
        } catch (Exception e) {
            log.error("save query error :", e);
            throw new ElasticSearchQueryException(e);
        }
    }


    @Override
    public void update(T item) {
        try {
            toJson(item).ifPresent(jsonValue ->
                    client.prepareUpdate(indexName, type, item.getId())
                            .setDoc(jsonValue, XContentType.JSON)
                            .get()
            );
        } catch (JsonParsingException je) {
            throw je;
        } catch (Exception e) {
            log.error("update query error :", e);
            throw new ElasticSearchQueryException(e);
        }
    }


    @Override
    public void delete(String itemId) {
        client.prepareDelete(indexName, type, itemId).get();
    }

    @Override
    public Optional<T> find(String itemId) {
        try {
            final GetResponse getResponse = client.prepareGet(indexName, type, itemId).get();
            return Optional.ofNullable(getResponse.getSourceAsString())
                    .flatMap(this::toItem);

        } catch (JsonParsingException je) {
            throw je;
        } catch (Exception e) {
            log.error("find query error :", e);
            throw new ElasticSearchQueryException(e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            final SearchResponse searchResponse = client.prepareSearch(indexName).setTypes(type).setSize(100).get();
            final SearchHit[] hits = searchResponse.getHits().getHits();
            return Stream.of(hits).map(SearchHit::getSourceAsString)
                    .map(this::toItem)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (JsonParsingException je) {
            throw je;
        } catch (Exception e) {
            log.error("findAll query error :", e);
            throw new ElasticSearchQueryException(e);
        }
    }


    private Optional<String> toJson(T item) {
        try {
            return Optional.ofNullable(new ObjectMapper().writeValueAsString(item));
        } catch (JsonProcessingException e) {
            log.error("Serializing Item error :", e);
            throw new JsonParsingException("Serializing Item error", e);
        }
    }

    private Optional<T> toItem(String json) {
        try {
            final Class<T> itemClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return Optional.ofNullable(new ObjectMapper().readValue(json, itemClass));
        } catch (IOException e) {
            log.error("Deserializing Item error :", e);
            throw new JsonParsingException("Deserializing Item error", e);
        }
    }

    private T addItemId(T item, String id) {
        item.setId(id);
        return item;
    }
}
