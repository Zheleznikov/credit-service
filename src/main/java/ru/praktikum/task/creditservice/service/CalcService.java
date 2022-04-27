package ru.praktikum.task.creditservice.service;

import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;
import ru.praktikum.task.creditservice.dto.RqCredit;

@Service
@Accessors(chain = true)
public class CalcService {

    private double baseInterestRate = 10;

    public double getBaseInterestRate() {
        return baseInterestRate;
    }

    public double calcYearPayment(RqCredit data) {
        calcAll(data);
        double percent = baseInterestRate / 100;
        double payment = ((data.getRequestedAmount() * (1 + data.getLoanRepaymentTime()*(percent))) / data.getLoanRepaymentTime());
        return round(payment);
    }

    public CalcService calcAll(RqCredit data) {
        baseInterestRate = 10;
        calcInterestRateDependsOnGoal(data);
        calcInterestRateDependsOnCreditRating(data);
        calcInterestRateDependsOnRequestedAmount(data);
        calcInterestRateDependsOnIncomingSource(data);
        return this;
    }

    private double round(double value) {
        long factor = (long) Math.pow(10, 3);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public CalcService calcInterestRateDependsOnGoal(RqCredit data) {
        if (data.getGoal().equals("mortgage")) { baseInterestRate -= 2;}
        if (data.getGoal().equals("business development")) {baseInterestRate -= 0.5;}
        if (data.getGoal().equals("personal loan")) {baseInterestRate += 1.5;}
        return this;
    }

    public CalcService calcInterestRateDependsOnCreditRating(RqCredit data) {
        if (data.getCreditRating() == -1) baseInterestRate += 1.5;
        if (data.getCreditRating() == 1) baseInterestRate -= 0.25;
        if (data.getCreditRating() == 2) baseInterestRate -= 0.75;
        return this;
    }

    public CalcService calcInterestRateDependsOnRequestedAmount(RqCredit data) {
        baseInterestRate += (-Math.log10(data.getRequestedAmount()));
        return this;
    }

    public CalcService calcInterestRateDependsOnIncomingSource(RqCredit data) {
        if (data.getIncomingSource().equals("passive income")) baseInterestRate += 0.5;
        if (data.getIncomingSource().equals("employee"))  baseInterestRate -= 0.25;
        if (data.getIncomingSource().equals("own business")) baseInterestRate += 0.25;
        return this;
    }






}
