package io.xmljim.json.parsertest.util;

import io.github.xmljim.json.parser.util.Timer;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TimerTest {

    private static Timer<String> testTimer;
    private static List<String> subjects = List.of("TEST_1", "TEST_2", "TEST_3", "TEST_4");

    @BeforeAll
    static void init() {
        testTimer = new Timer<>();
    }

    @AfterAll
    static void cleanup() {
        testTimer = null;
    }

    @Test
    @Order(1)
    void start() {
        subjects.forEach(subject -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String actual = testTimer.start(subject);
            assertEquals(subject, actual);
        });
    }

    @Test
    @Order(2)
    void stop() {
        subjects.forEach(subject -> {
            try {
                Thread.sleep(100);
                String actual = testTimer.stop(subject);
                assertEquals(subject, actual);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @Order(3)
    void getSequences() {
        assertTrue(testTimer.getSequences().stream().map(Timer.Tick::getSubject).toList().containsAll(subjects));
    }

    @Test
    @Order(4)
    void getAccumulatedTime() {
        assertTrue(testTimer.getAccumulatedTime() > 0);
    }

    @Test
    @Order(5)
    void get() {

        System.out.println(testTimer);
    }
}