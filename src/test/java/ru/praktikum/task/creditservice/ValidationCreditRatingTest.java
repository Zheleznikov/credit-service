package ru.praktikum.task.creditservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import ru.praktikum.task.creditservice.dto.RqCredit;
import ru.praktikum.task.creditservice.service.ValidationService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ValidationCreditRatingTest {

    RqCredit data;

    @BeforeEach
    public void prepareData() {
        data = new RqCredit()
                .setIncomeForLastYear(10)
                .setRequestedAmount(5)
                .setGoal("mortgage")
                .setAge(30)
                .setCreditRating(2)
                .setIncomingSource("employee")
                .setSex("male")
                .setLoanRepaymentTime(10);
    }

    @Test
    public void testCreditRatingShouldReturnFalse() {
        ValidationService service = new ValidationService();
        data.setCreditRating(-2);
        assertFalse(service.validateCreditRating(data));
    }

    @MethodSource("dataPos")
    @ParameterizedTest
    public void testCreditRatingShouldReturnTrue(int rating) {
        ValidationService service = new ValidationService();
        data.setCreditRating(rating);
        assertTrue(service.validateCreditRating(data));
    }

    private static Stream<Arguments> dataPos() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(2)
        );
    }
}
