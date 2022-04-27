package ru.praktikum.task.creditservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.praktikum.task.creditservice.dto.RqCredit;
import ru.praktikum.task.creditservice.service.CalcService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalcServiceTest {

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

    @MethodSource("dataForGoalTest")
    @ParameterizedTest
    public void testChangeInterestRateDependsOnGoalBusinessDevelopmentShouldBeCalculatedCorrect(String goal, double rate) {
        CalcService service = new CalcService();
        data.setGoal(goal);
        service.calcInterestRateDependsOnGoal(data);
        assertEquals(service.getBaseInterestRate(), rate);
    }

    private static Stream<Arguments> dataForGoalTest() {
        return Stream.of(
                Arguments.of("mortgage", 8.0),
                Arguments.of("personal loan", 11.5),
                Arguments.of("business development", 9.5)
        );
    }


    @MethodSource("dataForRatingTest")
    @ParameterizedTest
    public void testChangeInterestRateDependsCreditRatingMinusOneShouldBeCalculatedCorrect(int rating, double rate) {
        CalcService service = new CalcService();
        data.setCreditRating(rating);
        service.calcInterestRateDependsOnCreditRating(data);
        assertEquals(rate, service.getBaseInterestRate());
    }

    private static Stream<Arguments> dataForRatingTest() {
        return Stream.of(
                Arguments.of(-1, 11.5),
                Arguments.of(0, 10),
                Arguments.of(1, 9.75),
                Arguments.of(2, 9.25)
        );
    }

    @MethodSource("dataForCalcAmountLog")
    @ParameterizedTest
    public void testChangeInterestRateDependsOnRequestedAmountShouldBeCalculatedCorrect(double amount, double rate) {
        CalcService service = new CalcService();
        data.setRequestedAmount(amount);
        service.calcInterestRateDependsOnRequestedAmount(data);
        assertEquals(rate, service.getBaseInterestRate(), 0.25);
    }

    private static Stream<Arguments> dataForCalcAmountLog() {
        return Stream.of(
                Arguments.of(0.1, 11),
                Arguments.of(1, 10),
                Arguments.of(10, 9),
                Arguments.of(5, 9.5),
                Arguments.of(7, 9.25)
        );
    }



    @MethodSource("dataForCalcIncome")
    @ParameterizedTest
    public void testChangeInterestRateDependsOnIncomingSourcePassiveIncomeShouldBeCalculatedCorrect(String source, double expected) {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setIncomingSource("passive income");
        service.calcInterestRateDependsOnIncomingSource(data);
        assertEquals(10.5, service.getBaseInterestRate());
    }

    private static Stream<Arguments> dataForCalcIncome() {
        return Stream.of(
                Arguments.of("passive income", 10.5),
                Arguments.of("employee", 9.75),
                Arguments.of("own business", 10.25)
        );
    }

    @MethodSource("dataForCalcTogether")
    @ParameterizedTest
    public void testCalcAllShouldCalculateCorrectMaxRate(String goal, int rating, double amount, String src, double expected) {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal(goal)
                .setCreditRating(rating)
                .setRequestedAmount(amount)
                .setIncomingSource(src);
        service.calcAll(data);
        assertEquals(expected, service.getBaseInterestRate(), 0.25);
    }

    private static Stream<Arguments> dataForCalcTogether() {
        return Stream.of(
                Arguments.of("personal loan", -1, 0.1, "own business", 14.25),
                Arguments.of("mortgage", 2, 5, "employee", 6.3)
        );
    }

    @Test
    public void tesCalcYearPaymentBasedOnMaxRateShouldCalculateCorrectly() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal("personal loan")
                .setCreditRating(-1)
                .setRequestedAmount(0.1)
                .setIncomingSource("own business")
                .setLoanRepaymentTime(1);
        assertEquals(0.114, service.calcYearPayment(data));
    }



}
