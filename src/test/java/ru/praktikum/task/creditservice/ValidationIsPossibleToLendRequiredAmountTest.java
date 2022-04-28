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
public class ValidationIsPossibleToLendRequiredAmountTest {

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
    public void testIsPossibleToLendRequiredAmountShouldReturnTrue(double income, double amount, int loanTime) {
        data.setIncomeForLastYear(income).setLoanRepaymentTime(loanTime).setRequestedAmount(amount);
        ValidationService service = new ValidationService();
        assertTrue(service.validateIsPossibleToLendRequiredAmount(data));
    }

    private static Stream<Arguments> dataPos() {
        return Stream.of(
                Arguments.of(10, 5, 2),
                Arguments.of(10, 0.1, 10),
                Arguments.of(10, 0.1, 19),
                Arguments.of(1, 0.1, 19),
                Arguments.of(5, 0.1, 20),
                Arguments.of(1, 0.1, 1),
                Arguments.of(10, 5, 19),
                Arguments.of(1.8, 2.4, 4),
                Arguments.of(10, 10,20),
                Arguments.of(5, 10, 19),
                Arguments.of(1.8, 1.6, 3),
                Arguments.of(1.8, 2.4, 4)
        );
    }

    @MethodSource("dataNeg")
    @ParameterizedTest
    public void testIsPossibleToLendRequiredAmountShouldReturnFalse(double income, double amount, int loanTime) {
        data.setIncomeForLastYear(income).setLoanRepaymentTime(loanTime).setRequestedAmount(amount);
        ValidationService service = new ValidationService();
        assertFalse(service.validateIsPossibleToLendRequiredAmount(data));
    }

    private static Stream<Arguments> dataNeg() {
        return Stream.of(
                Arguments.of(0.1, 1, 1),
                Arguments.of(0.1, 5, 10),
                Arguments.of(5, 5, 1),
                Arguments.of(0.1, 1, 20),
                Arguments.of(5, 9.9, 2),
                Arguments.of(0.1, 9.9, 19),
                Arguments.of(1, 10, 2),
                Arguments.of(1, 9.9, 10),
                Arguments.of(5, 10, 1),
                Arguments.of(0.1, 9.9, 20),
                Arguments.of(10, 9.9, 1),
                Arguments.of(0.1, 10, 10),
                Arguments.of(1.5, 2, 3)

        );
    }

}
