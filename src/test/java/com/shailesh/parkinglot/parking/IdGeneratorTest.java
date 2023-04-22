package com.shailesh.parkinglot.parking;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class IdGeneratorTest {

    IdGenerator idGenerator = new IdGenerator(1, "%03d");

    @Test
    public void shouldReturnNewIdsFormatedTo3Place() {

        // then
        MatcherAssert.assertThat(idGenerator.nextId(), IsEqual.equalTo("001"));
        MatcherAssert.assertThat(idGenerator.nextId(), IsEqual.equalTo("002"));
        MatcherAssert.assertThat(idGenerator.nextId(), IsEqual.equalTo("003"));

    }

    @Test
    public void shouldReturnNewIdsFormattedTo3PlaceInMultiThreading() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<String>> results = new ArrayList<>();

        // when
        for (int i = 0; i < 1000; i++) {
            results.add(executorService.submit(() -> idGenerator.nextId()));
        }

        // then
        MatcherAssert.assertThat(results.stream().map(result ->  getId(result)).distinct().count(), IsEqual.equalTo(1000L));
    }

    private static String getId(Future<String> result) {
        try {
            return result.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}