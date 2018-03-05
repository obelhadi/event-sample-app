package org.obel.exception;

public class ElasticSearchQueryException extends RuntimeException {

    public ElasticSearchQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElasticSearchQueryException(Throwable cause) {
        super(cause);
    }
}
