package org.lance.cloud.resilience.test;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerOpenException;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.function.Predicate;

import static io.vavr.API.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CircuitBreakerTest {

    // Create a custom configuration for a CircuitBreaker
    CircuitBreakerConfig customConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .ringBufferSizeInHalfOpenState(2)
            .ringBufferSizeInClosedState(2)
            .build();

    @Test
    public void testBase(){
        // Given
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

        // When I decorate my function
        CheckedFunction0<String> decoratedSupplier = CircuitBreaker
                .decorateCheckedSupplier(circuitBreaker, () -> "This can be any method which returns: 'Hello");

        // and chain an other function with map
        Try<String> result = Try.of(decoratedSupplier)
                .map(value -> value + " world'");

        // Then the Try Monad returns a Success<String>, if all functions ran successfully.
        assertThat(result.isSuccess(), is(true));
        assertThat(result.get(), equalTo("This can be any method which returns: 'Hello world'"));
    }

    @Test
    public void testChain(){
        // Given
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");
        CircuitBreaker anotherCircuitBreaker = CircuitBreaker.ofDefaults("anotherTestName");

        // When I create a Supplier and a Function which are decorated by different CircuitBreakers
        CheckedFunction0<String> decoratedSupplier = CircuitBreaker
                .decorateCheckedSupplier(circuitBreaker, () -> "Hello");

        CheckedFunction1<String, String> decoratedFunction = CircuitBreaker
                .decorateCheckedFunction(anotherCircuitBreaker, (input) -> input + " world");

        // and I chain a function with map
        Try<String> result = Try.of(decoratedSupplier)
                .mapTry(decoratedFunction);

        assertThat(result.isSuccess(), is(true));
        assertThat(result.get(), equalTo("Hello world"));
    }

    @Test
    public void testOpen(){
        // Given
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .ringBufferSizeInClosedState(2)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .build();
        CircuitBreaker circuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);

        // Simulate a failure attempt
        circuitBreaker.onError(0, new RuntimeException());
        // CircuitBreaker is still CLOSED, because 1 failure is allowed
        assertThat(circuitBreaker.getState(), equalTo(CircuitBreaker.State.CLOSED));
        // Simulate a failure attempt
        circuitBreaker.onError(0, new RuntimeException());
        // CircuitBreaker is OPEN, because the failure rate is above 50%
        assertThat(circuitBreaker.getState(), equalTo(CircuitBreaker.State.OPEN));

        // When I decorate my function and invoke the decorated function
        Try<String> result = Try.of(CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> "Hello"))
                .map(value -> value + " world");

        // Then the call fails, because CircuitBreaker is OPEN
        assertThat(result.isFailure(), is(true));
        // Exception is CircuitBreakerOpenException
        assertThat(result.failed().get(), instanceOf(CircuitBreakerOpenException.class));
    }

    @Test
    public void testRecover(){
        // Given
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

        // When I decorate my function and invoke the decorated function
        CheckedFunction0<String> checkedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
            throw new RuntimeException("BAM!");
        });
        Try<String> result = Try.of(checkedSupplier)
                .recover(throwable -> "Hello Recovery");

        // Then the function should be a success, because the exception could be recovered
        assertThat(result.isSuccess(), is(true));
        // and the result must match the result of the recovery function.
        assertThat(result.get(), equalTo("Hello Recovery"));
    }

    @Test
    public void testFailure(){
        // Given
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .ringBufferSizeInClosedState(2)
                .ringBufferSizeInHalfOpenState(2)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .recordFailure(throwable -> Match(throwable).of(
                        Case($(Predicate.isEqual(WebServiceException.class)), true),
                        Case($(), false)))
                .build();
        CircuitBreaker circuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);

        // Simulate a failure attempt
        circuitBreaker.onError(0, new WebServiceException());
        // CircuitBreaker is still CLOSED, because 1 failure is allowed
        assertThat(circuitBreaker.getState(), equalTo(CircuitBreaker.State.CLOSED));

        //When
        CheckedRunnable checkedRunnable = CircuitBreaker.decorateCheckedRunnable(circuitBreaker, () -> {
            throw new SocketTimeoutException("BAM!");
        });
        Try result = Try.run(checkedRunnable);

        //Then
        assertThat(result.isFailure(), is(true));
        Assert.isTrue(result.isFailure(), "");
        // CircuitBreaker is still CLOSED, because SocketTimeoutException has not been recorded as a failure
        assertThat(circuitBreaker.getState(), equalTo(CircuitBreaker.State.CLOSED));
        assertThat(result.failed().get(), instanceOf(IOException.class));
    }

    @Test
    public void testEvent(){
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

        circuitBreaker.getEventPublisher()
                .onError(event -> assertThat(event.getThrowable().getMessage(), equalTo("Something wrong")));

        // Simulate a failure attempt
        circuitBreaker.onError(0, new WebServiceException("Something wrong"));
    }
}
