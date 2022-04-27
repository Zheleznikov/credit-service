package ru.praktikum.task.creditservice;

import org.junit.jupiter.api.BeforeAll;
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

/**
 * Юнит-тесты для проверки пункта  [02.1] Если возраст превышает пенсионный возраст на момент возврата кредита, кредит не выдаётся
 *       [мужчина, возраст + срок погашения = 40, 59, 60, 64, 65, 66, 80; женщина, возраст + срок погашения 50, 59, 60, 61, 70]
 */
@SpringBootTest
public class ValidationRetirementAgeTest {

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

    @MethodSource("dataForMaleTrue")
    @ParameterizedTest
    public void testRetirementAgeForMaleShouldReturnTrue(int age, int loanTime) {
        data.setAge(age).setLoanRepaymentTime(loanTime).setSex("male");
        ValidationService service = new ValidationService();
        assertTrue(service.validateRetirementAge(data));
    }

    private static Stream<Arguments> dataForMaleTrue() {
        return Stream.of(
                Arguments.of(30, 1),
                Arguments.of(40, 19),
                Arguments.of(40, 20),
                Arguments.of(30, 10),
                Arguments.of(30, 14)
        );
    }

    @MethodSource("dataForMaleFalse")
    @ParameterizedTest
    public void testRetirementAgeForMaleShouldReturnFalse(int age, int loanTime) {
        data.setAge(age).setLoanRepaymentTime(loanTime).setSex("male");
        ValidationService service = new ValidationService();
        assertFalse(service.validateRetirementAge(data));
    }

    private static Stream<Arguments> dataForMaleFalse() {
        return Stream.of(
                Arguments.of(64, 1),
                Arguments.of(61, 5),
                Arguments.of(60, 20)
        );
    }

    @MethodSource("dataForFemaleTrue")
    @ParameterizedTest
    public void testRetirementAgeForFemaleShouldReturnTrue(int age, int loanTime) {
        data.setAge(age).setLoanRepaymentTime(loanTime).setSex("female");
        ValidationService service = new ValidationService();
        assertTrue(service.validateRetirementAge(data));
    }

    private static Stream<Arguments> dataForFemaleTrue() {
        return Stream.of(
                Arguments.of(30, 20),
                Arguments.of(40, 19)
        );
    }

    @MethodSource("dataForFemaleFalse")
    @ParameterizedTest
    public void testRetirementAgeForFemaleShouldReturnFalse(int age, int loanTime) {
        data.setAge(age).setLoanRepaymentTime(loanTime).setSex("female");
        ValidationService service = new ValidationService();
        assertFalse(service.validateRetirementAge(data));
    }

    private static Stream<Arguments> dataForFemaleFalse() {
        return Stream.of(
                Arguments.of(59, 1),
                Arguments.of(42, 19),
                Arguments.of(50, 20)
        );
    }



}
