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

@SpringBootTest
public class BaseValidationTest {

    @MethodSource("testData")
    @ParameterizedTest
    public void testBaseValidationShouldReturnFalse(RqCredit data) {
        ValidationService service = new ValidationService();
        data.setCreditRating(-2);
        assertFalse(service.baseValidation(data));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(new RqCredit()
                        .setAge(-1)
                        .setSex("female")
                        .setIncomingSource("beauty eyes")
                        .setIncomeForLastYear(1)
                        .setCreditRating(3)
                        .setRequestedAmount(5)
                        .setLoanRepaymentTime(20)
                        .setGoal("party")
                ),
                Arguments.of(new RqCredit()
                        .setAge(18)
                        .setSex("unknown")
                        .setIncomingSource("employee")
                        .setIncomeForLastYear(0)
                        .setCreditRating(0)
                        .setRequestedAmount(5)
                        .setLoanRepaymentTime(0)
                        .setGoal("personal loan")
                ),
                Arguments.of(new RqCredit()
                        .setAge(0)
                        .setSex("unknown")
                        .setIncomingSource("yeys")
                        .setIncomeForLastYear(-0.1)
                        .setCreditRating(-1)
                        .setRequestedAmount(4.5)
                        .setLoanRepaymentTime(1)
                        .setGoal("car loan")
                ),
                Arguments.of(new RqCredit()
                        .setAge(17)
                        .setSex("female")
                        .setIncomingSource("own business")
                        .setIncomeForLastYear(1)
                        .setCreditRating(-3)
                        .setRequestedAmount(-0.5)
                        .setLoanRepaymentTime(1)
                        .setGoal("personal loan")
                ),
                Arguments.of(new RqCredit()
                        .setAge(30)
                        .setSex("female")
                        .setIncomingSource("eues")
                        .setIncomeForLastYear(0)
                        .setCreditRating(0)
                        .setRequestedAmount(0)
                        .setLoanRepaymentTime(1)
                        .setGoal("party")
                ),
                Arguments.of(new RqCredit()
                        .setAge(-1)
                        .setSex("unknown")
                        .setIncomingSource("own business")
                        .setIncomeForLastYear(0)
                        .setCreditRating(-3)
                        .setRequestedAmount(10.1)
                        .setLoanRepaymentTime(20)
                        .setGoal("car loan")
                )

                // TODO дописать аргументы на основе параметра


        );
    }

}
