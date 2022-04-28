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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ValidationIncomingSourceTest {
    RqCredit data;

    @BeforeEach
    public void prepareData() {
        data = new RqCredit()
                .setIncomeForLastYear(10)
                .setRequestedAmount(5)
                .setGoal("mortgage")
                .setAge(30)
                .setCreditRating(2)
                .setIncomingSource("unemployed")
                .setSex("male")
                .setLoanRepaymentTime(10);
    }

    @Test
    public void testIncomingSourceShouldReturnFalse() {
        ValidationService service = new ValidationService();
        data.setIncomingSource("unemployed");
        assertFalse(service.validateIfUnemployed(data));
    }

    @MethodSource("dataPos")
    @ParameterizedTest
    public void testIncomingSourceShouldReturnTrue(String src) {
        ValidationService service = new ValidationService();
        data.setIncomingSource(src);
        assertTrue(service.validateCreditRating(data));
    }

    private static Stream<Arguments> dataPos() {
        return Stream.of(
                Arguments.of("mortgage"),
                Arguments.of("personal loan"),
                Arguments.of("car loan")
        );
    }

}
