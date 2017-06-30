package org.swisspush.gateleen.routing;

import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.swisspush.gateleen.routing.PathProcessingStrategyFinder.PathProcessingStrategy;

import static org.swisspush.gateleen.core.util.HttpRequestHeader.PATH_PROCESSING_STRATEGY_HEADER;

/**
 * <p>
 * Tests for the {@link PathProcessingStrategyFinder} class
 * </p>
 *
 * @author https://github.com/mcweba [Marc-Andre Weber]
 */
@RunWith(VertxUnitRunner.class)
public class PathProcessingStrategyFinderTest {

    @Test
    public void testGetDefaultPathProcessingStrategy(TestContext context){
        PathProcessingStrategyFinder strategyFinder = new PathProcessingStrategyFinder(null);
        context.assertEquals(PathProcessingStrategy.cleaned, strategyFinder.getDefaultPathProcessingStrategy());

        strategyFinder = new PathProcessingStrategyFinder(PathProcessingStrategy.unmodified);
        context.assertEquals(PathProcessingStrategy.unmodified, strategyFinder.getDefaultPathProcessingStrategy());

        strategyFinder = new PathProcessingStrategyFinder(PathProcessingStrategy.cleaned);
        context.assertEquals(PathProcessingStrategy.cleaned, strategyFinder.getDefaultPathProcessingStrategy());
    }

    @Test
    public void testGetPathProcessingStrategy(TestContext context){
        PathProcessingStrategyFinder strategyFinder = new PathProcessingStrategyFinder(PathProcessingStrategy.unmodified);

        // no headers, fallback to default
        context.assertEquals(PathProcessingStrategy.unmodified, strategyFinder.getPathProcessingStrategy(null));

        // no path-processing-strategy header, fallback to default
        context.assertEquals(PathProcessingStrategy.unmodified, strategyFinder.getPathProcessingStrategy(new CaseInsensitiveHeaders()));

        // invalid path-processing-strategy header, fallback to default
        context.assertEquals(PathProcessingStrategy.unmodified, strategyFinder.getPathProcessingStrategy(
                new CaseInsensitiveHeaders().set(PATH_PROCESSING_STRATEGY_HEADER.getName(), "booom")));

        // valid path-processing-strategy header. this value should be returned
        context.assertEquals(PathProcessingStrategy.unmodified, strategyFinder.getPathProcessingStrategy(
                new CaseInsensitiveHeaders().set(PATH_PROCESSING_STRATEGY_HEADER.getName(), "unmodified")));
        context.assertEquals(PathProcessingStrategy.cleaned, strategyFinder.getPathProcessingStrategy(
                new CaseInsensitiveHeaders().set(PATH_PROCESSING_STRATEGY_HEADER.getName(), "cleaned")));
    }
}
