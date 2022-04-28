package ru.praktikum.task.creditservice;

import org.junit.jupiter.api.BeforeEach;
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
public class ValidationPossibleAmountDependsOnRatingTest {

    RqCredit data;

    @BeforeEach
    public void prepareData() {
        data = new RqCredit()
                .setIncomeForLastYear(10)
                .setRequestedAmount(5)
                .setGoal("mortgage")
                .setAge(30)
                .setCreditRating(2)
                .setSex("male")
                .setLoanRepaymentTime(10);
    }

    @MethodSource("dataPos")
    @ParameterizedTest
    public void testIfAmountLessThanPossibleShouldReturnTrue(int rating, double amount) {
        data.setCreditRating(rating).setRequestedAmount(amount);
        ValidationService service = new ValidationService();
        assertTrue(service.validatePossibleAmountDependsOnCreditRating(data));
    }

    private static Stream<Arguments> dataPos() {
        return Stream.of(
                Arguments.of(-1, 0.1),
                Arguments.of(-1, 0.5),
                Arguments.of(-1, 0.9),
                Arguments.of(-1, 1),

                Arguments.of(0, 0.5),
                Arguments.of(0, 1),
                Arguments.of(0, 1.1),
                Arguments.of(0, 3),
                Arguments.of(0, 4.9),
                Arguments.of(0, 5),

                Arguments.of(1, 0.1),
                Arguments.of(1, 1),
                Arguments.of(1, 5),
                Arguments.of(1, 5.1),
                Arguments.of(1, 7.3),
                Arguments.of(1, 9.9),
                Arguments.of(1, 10),

                Arguments.of(2, 0.1),
                Arguments.of(2, 1),
                Arguments.of(2, 5),
                Arguments.of(2, 5.1),
                Arguments.of(2, 7.3),
                Arguments.of(2, 9.9),
                Arguments.of(2, 10)
        );
    }

    @MethodSource("dataNeg")
    @ParameterizedTest
    public void testIfAmountMoreThanPossibleShouldReturnFalse(int rating, double amount) {
        data.setCreditRating(rating).setRequestedAmount(amount);
        ValidationService service = new ValidationService();
        assertFalse(service.validatePossibleAmountDependsOnCreditRating(data));
    }

    private static Stream<Arguments> dataNeg() {
        return Stream.of(
                Arguments.of(-1, 1.1),
                Arguments.of(-1, 5),
                Arguments.of(-1, 5.5),
                Arguments.of(-1, 9.9),
                Arguments.of(-1, 10),

                Arguments.of(0, 5.1),
                Arguments.of(0, 7.5),
                Arguments.of(0, 9.9),
                Arguments.of(0, 10),

                Arguments.of(1, 10.1),
                Arguments.of(1, 15),

                Arguments.of(2, 10.1),
                Arguments.of(2, 15)
        );
    }

}
