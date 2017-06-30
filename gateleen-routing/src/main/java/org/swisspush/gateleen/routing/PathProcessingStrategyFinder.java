package org.swisspush.gateleen.routing;

import io.vertx.core.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swisspush.gateleen.core.util.HttpRequestHeader;

import static org.swisspush.gateleen.core.util.HttpRequestHeader.PATH_PROCESSING_STRATEGY_HEADER;
import static org.swisspush.gateleen.core.util.HttpRequestHeader.getString;

/**
 * Util class to get the {@link PathProcessingStrategy} based on the static configuration and http request headers.
 *
 * @author https://github.com/mcweba [Marc-Andre Weber]
 */
public class PathProcessingStrategyFinder {

    public static final PathProcessingStrategy DEFAULT_PATH_PROCESSING_STRATEGY = PathProcessingStrategy.cleaned;

    private PathProcessingStrategy pathProcessingStrategy;
    private Logger log = LoggerFactory.getLogger(PathProcessingStrategyFinder.class);

    public PathProcessingStrategyFinder(PathProcessingStrategy pathProcessingStrategy) {
        if(pathProcessingStrategy == null){
            log.warn("Cannot initialize PathProcessingStrategyFinder with null value. " +
                    "Going to use default path processing strategy '"+DEFAULT_PATH_PROCESSING_STRATEGY.name()+"' instead");
            this.pathProcessingStrategy = DEFAULT_PATH_PROCESSING_STRATEGY;
        } else {
            this.pathProcessingStrategy = pathProcessingStrategy;
            log.info("Setting default path processing strategy to '"+this.pathProcessingStrategy.name()+"'");
        }
    }

    /**
     * Get the {@link PathProcessingStrategy} configured as default value.
     *
     * @return the default {@link PathProcessingStrategy}
     */
    public PathProcessingStrategy getDefaultPathProcessingStrategy() { return  pathProcessingStrategy; }

    /**
     * Get the {@link PathProcessingStrategy} based on the static configuration (default value) and the http
     * request headers. When the http request headers contain the {@link HttpRequestHeader#PATH_PROCESSING_STRATEGY_HEADER}
     * with a valid value, the {@link PathProcessingStrategy} relating this value will be returned. If the header is
     * missing or does not contain a valid value, the default {@link PathProcessingStrategy} will be returned.
     *
     * @param requestHeaders the http request headers of the request
     * @return {@link PathProcessingStrategy} based on the static configuration (default value) and the http request headers
     */
    public PathProcessingStrategy getPathProcessingStrategy(MultiMap requestHeaders) {
        if(requestHeaders == null){
            return pathProcessingStrategy;
        }
        if(HttpRequestHeader.containsHeader(requestHeaders, PATH_PROCESSING_STRATEGY_HEADER)){
            PathProcessingStrategy strategy = PathProcessingStrategy.fromString(getString(requestHeaders, PATH_PROCESSING_STRATEGY_HEADER));
            if(strategy != null){
                return strategy;
            }
        }
        return pathProcessingStrategy;
    }

    public enum PathProcessingStrategy {
        unmodified, cleaned;

        /**
         * Returns the enum PathProcessingStrategy which matches the specified String value.
         *
         * @param strategyStr The strategy as String
         * @return The matching PathProcessingStrategy or null if none matches.
         */
        public static PathProcessingStrategy fromString(String strategyStr) {
            for (PathProcessingStrategy strategy : values()) {
                if (strategy.name().equalsIgnoreCase(strategyStr)) {
                    return strategy;
                }
            }
            return null;
        }
    }
}
