package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class AbstractServiceTest {
    protected static final Logger log = LoggerFactory.getLogger(AbstractServiceTest.class);

    protected static StringBuilder results;

    @BeforeClass
    public static void setUpResults() {
        results = new StringBuilder();
    }

    @Rule
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d",
                    description.getMethodName(),
                    TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms");
        }
    };

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }
}
