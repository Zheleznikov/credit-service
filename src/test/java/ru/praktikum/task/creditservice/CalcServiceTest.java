package ru.praktikum.task.creditservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.praktikum.task.creditservice.dto.RqCredit;
import ru.praktikum.task.creditservice.service.CalcService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalcServiceTest {

    @Test
    public void increaseInterestRateDependsOnGoalBusinessDevelopmentShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal("business development");
        service.calcInterestRateDependsOnGoal(data);
        assertEquals(service.getBaseInterestRate(), 9.5);
    }

    @Test
    public void increaseInterestRateDependsOnGoalMortgageShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal("mortgage");
        service.calcInterestRateDependsOnGoal(data);
        assertEquals(service.getBaseInterestRate(), 8.0);
    }

    @Test
    public void increaseInterestRateDependsOnGoalPersonalLoanShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal("personal loan");
        service.calcInterestRateDependsOnGoal(data);
        assertEquals(service.getBaseInterestRate(), 11.5);
    }

    @Test
    public void increaseInterestRateDependsCreditRatingMinusOneShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setCreditRating(-1);
        service.calcInterestRateDependsOnCreditRating(data);
        assertEquals(service.getBaseInterestRate(), 11.5);
    }

    @Test
    public void increaseInterestRateDependsCreditRatingZeroShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setCreditRating(0);
        service.calcInterestRateDependsOnCreditRating(data);
        assertEquals(service.getBaseInterestRate(), 10);
    }

    @Test
    public void increaseInterestRateDependsCreditRatingPlusOneShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setCreditRating(1);
        service.calcInterestRateDependsOnCreditRating(data);
        assertEquals(service.getBaseInterestRate(), 9.75);
    }

    @Test
    public void increaseInterestRateDependsCreditRatingPlusTwoShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setCreditRating(2);
        service.calcInterestRateDependsOnCreditRating(data);
        assertEquals(service.getBaseInterestRate(), 9.25);
    }

    @Test
    public void increaseInterestRateDependsOnRequestedAmountMinShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setRequestedAmount(0.1);
        service.calcInterestRateDependsOnRequestedAmount(data);
        assertEquals(service.getBaseInterestRate(), 11);
    }

    @Test
    public void increaseInterestRateDependsOnRequestedAmountAverageShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setRequestedAmount(1);
        service.calcInterestRateDependsOnRequestedAmount(data);
        assertEquals(service.getBaseInterestRate(), 10);
    }

    @Test
    public void increaseInterestRateDependsOnRequestedAmountMaxShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setRequestedAmount(10);
        service.calcInterestRateDependsOnRequestedAmount(data);
        assertEquals(service.getBaseInterestRate(), 9);
    }

    @Test
    public void increaseInterestRateDependsOnIncomingSourcePassiveIncomeShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setIncomingSource("passive income");
        service.calcInterestRateDependsOnIncomingSource(data);
        assertEquals(service.getBaseInterestRate(), 10.5);
    }

    @Test
    public void increaseInterestRateDependsOnIncomingSourceEmployeeShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setIncomingSource("employee");
        service.calcInterestRateDependsOnIncomingSource(data);
        assertEquals(service.getBaseInterestRate(), 9.75);
    }

    @Test
    public void increaseInterestRateDependsOnIncomingSourceOwnBusinessShouldBeCalculatedCorrect() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setIncomingSource("own business");
        service.calcInterestRateDependsOnIncomingSource(data);
        assertEquals(service.getBaseInterestRate(), 10.25);
    }

    @Test
    public void calcAllShouldCalculateCorrectMinRate() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal("mortgage")
                .setCreditRating(2)
                .setRequestedAmount(10)
                .setIncomingSource("employee");

        service.calcInterestRateDependsOnIncomingSource(data);
        service.calcInterestRateDependsOnGoal(data);
        service.calcInterestRateDependsOnCreditRating(data);
        service.calcInterestRateDependsOnRequestedAmount(data);
        assertEquals(6, service.getBaseInterestRate());
    }

    @Test
    public void testCalcAllShouldCalculateCorrectMaxRate() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal("personal loan")
                .setCreditRating(-1)
                .setRequestedAmount(0.1)
                .setIncomingSource("own business");

        service.calcInterestRateDependsOnIncomingSource(data);
        service.calcInterestRateDependsOnGoal(data);
        service.calcInterestRateDependsOnCreditRating(data);
        service.calcInterestRateDependsOnRequestedAmount(data);
        assertEquals(14.25, service.getBaseInterestRate());
    }

    @Test
    public void tesCalcYearPaymentBasedOnMaxRateShouldCalculateCorrectly() {
        CalcService service = new CalcService();
        RqCredit data = new RqCredit().setGoal("personal loan")
                .setCreditRating(-1)
                .setRequestedAmount(0.1)
                .setIncomingSource("own business")
                .setLoanRepaymentTime(1);
        double v = service.calcYearPayment(data);
        assertEquals(0.11, service.getBaseInterestRate());
    }


}
