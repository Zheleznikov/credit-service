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
public class ValidationPossibleAmountDependsOnIncomingSourceTest {

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

    @MethodSource("dataPos")
    @ParameterizedTest
    public void testIfAmountLessThanPossibleShouldReturnTrue(String src, double amount) {
        data.setIncomingSource(src).setRequestedAmount(amount);
        ValidationService service = new ValidationService();
        assertTrue(service.validatePossibleAmountDependsOnIncomingSource(data));
    }

    private static Stream<Arguments> dataPos() {
        return Stream.of(
                Arguments.of("passive income", 0.1),
                Arguments.of("passive income", 0.5),
                Arguments.of("passive income", 0.9),
                Arguments.of("passive income", 1),

                Arguments.of("employee", 0.5),
                Arguments.of("employee", 1),
                Arguments.of("employee", 1.1),
                Arguments.of("employee", 3),
                Arguments.of("employee", 4.9),
                Arguments.of("employee", 5),

                Arguments.of("own business", 0.1),
                Arguments.of("own business", 1),
                Arguments.of("own business", 5),
                Arguments.of("own business", 5.1),
                Arguments.of("own business", 7.3),
                Arguments.of("own business", 9.9),
                Arguments.of("own business", 10)
        );
    }

    @MethodSource("dataNeg")
    @ParameterizedTest
    public void testIfAmountMoreThanPossibleShouldReturnFalse(String src, double amount) {
        data.setIncomingSource("passive income").setRequestedAmount(amount);
        ValidationService service = new ValidationService();
        assertFalse(service.validatePossibleAmountDependsOnIncomingSource(data));
    }

    private static Stream<Arguments> dataNeg() {
        return Stream.of(
                Arguments.of("passive income", 1.1),
                Arguments.of("passive income", 5),
                Arguments.of("passive income", 5.5),
                Arguments.of("passive income", 9.9),
                Arguments.of("passive income", 10),

                Arguments.of("employee", 5.1),
                Arguments.of("employee", 7.5),
                Arguments.of("employee", 9.9),
                Arguments.of("employee", 10),

                Arguments.of("own business", 10.1),
                Arguments.of("own business", 15)
        );
    }




}
