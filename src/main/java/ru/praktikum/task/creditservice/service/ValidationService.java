package ru.praktikum.task.creditservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.praktikum.task.creditservice.dto.RqCredit;

import java.util.Arrays;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public boolean validateRq(RqCredit data) {
        return baseValidation(data)
                        && validateRetirementAge(data)
                        && validateIsPossibleToLendRequiredAmount(data)
                        && validateCreditRating(data)
                        && validateIfUnemployed(data)
                        && validatePossibleAmountDependsOnIncomingSource(data)
                        && validatePossibleAmountDependsOnCreditRating(data)
                        && validateIfIncomingForYear(data);
    }

    /**
     * Если возраст превышает пенсионный возраст на момент возврата кредита --> кредит не выдаётся
     */

    public boolean baseValidation(RqCredit data) {
        if (data.getAge() < 18) return false;
        if (data.getRequestedAmount() > 10.0 || data.getRequestedAmount() <= 0) return false;
        if (data.getIncomeForLastYear() <= 0) return false;
        if (data.getLoanRepaymentTime() <= 0 || data.getLoanRepaymentTime() > 20) return false;

        int[] creditRatingList = {-2,-1,0,1,2};
        if (IntStream.of(creditRatingList).noneMatch(x -> x == data.getCreditRating())) return false;

        String [] possible_incoming = {"passive income", "employee", "own business", "unemployed"};
        if (Arrays.stream(possible_incoming).noneMatch(data.getIncomingSource()::equals)) return false;

        String [] possible_goal = {"mortgage", "business development", "car loan", "personal loan"};
        if (Arrays.stream(possible_goal).noneMatch(data.getGoal()::equals)) return false;

        if (!data.getSex().equals("female") && !data.getSex().equals("male")) return false;
        return true;
    }

    public boolean validateRetirementAge(RqCredit data) {
        return !(((data.getAge() + data.getLoanRepaymentTime()) >= 65 && data.getSex().equals("male"))
                || ((data.getAge() + data.getLoanRepaymentTime()) >= 60 && data.getSex().equals("female")));
    }

    /**
     * Если результат деления запрошенной суммы на срок погашения в годах более трети годового дохода --> кредит не выдаётся
     */
    public boolean validateIsPossibleToLendRequiredAmount(RqCredit data) {
        double amount = data.getRequestedAmount();
        int time = data.getLoanRepaymentTime();
        double income = data.getIncomeForLastYear();
        boolean answer = (amount / time) <= (income / 3);
        System.out.println("Если это " + amount / time + " больше, чем это " + income / 3  + ", то не выдаем и возвращаем false");
        return answer;
    }

    /**
     * Если кредитный рейтинг -2 --> кредит не выдаётся
     */
    public boolean validateCreditRating(RqCredit data) {
        return !(data.getCreditRating() > 2 || data.getCreditRating() < -1);
    }

    /**
     * Если в источнике дохода указано "безработный" --> кредит не выдаётся
     */
    public boolean validateIfUnemployed(RqCredit data) {
        return !data.getIncomingSource().equals("unemployed");
    }

    /**
     * При пассивном доходе выдаётся кредит на сумму до 1 млн, наёмным работникам - до 5 млн, собственное дело - до 10 млн
     */
    public boolean validatePossibleAmountDependsOnIncomingSource(RqCredit data) {
        if (data.getIncomingSource().equals("passive income") && data.getRequestedAmount() <= 1) return true;
        if (data.getIncomingSource().equals("employee") && data.getRequestedAmount() <= 5) return true;
        if (data.getIncomingSource().equals("own business") && data.getRequestedAmount() <= 10) return true;
        return false;
    }

    /**
     * При кредитном рейтинге -1 выдаётся кредит на сумму до 1 млн, при 0 - до 5 млн, при 1 или 2 - до 10 млн
     */
    public boolean validatePossibleAmountDependsOnCreditRating(RqCredit data) {
        if (data.getCreditRating() == -1 && data.getRequestedAmount() <= 1) return true;
        if (data.getCreditRating() == 0 && data.getRequestedAmount() <= 5) return true;
        if (data.getCreditRating() > 0 && data.getRequestedAmount() <= 10) return true;
        return false;
    }

    /**
     * Если годовой платёж (включая проценты) больше половины дохода --> кредит не выдаётся
     */
    public boolean validateIfIncomingForYear(RqCredit data) {
        return new CalcService().calcYearPayment(data) <= (data.getIncomeForLastYear() / 2);
    }


}
