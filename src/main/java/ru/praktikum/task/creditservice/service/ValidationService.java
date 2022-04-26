package ru.praktikum.task.creditservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.praktikum.task.creditservice.dto.RqCredit;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public boolean validateRq(RqCredit data) {
        return validateRetirementAge(data)
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
    public boolean validateRetirementAge(RqCredit data) {
        if (data.getAge() <= 0) return false;
        if (!data.getSex().equals("female") && !data.getSex().equals("male")) return false;
        return !((data.getAge() >= 65 && data.getSex().equals("male")) || (data.getAge() >= 60 && data.getSex().equals("female")));
    }

    /**
     * Если результат деления запрошенной суммы на срок погашения в годах более трети годового дохода --> кредит не выдаётся
     */
    public boolean validateIsPossibleToLendRequiredAmount(RqCredit data) {
        return (data.getRequestedAmount() / data.getLoanRepaymentTime()) <= (data.getIncomeForLastYear() / 3);
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
        if (data.getIncomingSource().equals("passive income") && data.getRequestedAmount() < 1) return true;
        if (data.getIncomingSource().equals("employee") && data.getRequestedAmount() < 5) return true;
        if (data.getIncomingSource().equals("own business") && data.getRequestedAmount() < 10) return true;
        return false;
    }

    /**
     * При кредитном рейтинге -1 выдаётся кредит на сумму до 1 млн, при 0 - до 5 млн, при 1 или 2 - до 10 млн
     */
    public boolean validatePossibleAmountDependsOnCreditRating(RqCredit data) {
        if (data.getCreditRating() == -1 && data.getRequestedAmount() < 1) return true;
        if (data.getCreditRating() == 0 && data.getRequestedAmount() < 5) return true;
        if (data.getCreditRating() > 0 && data.getRequestedAmount() < 10) return true;
        return false;
    }

    /**
     * Если годовой платёж (включая проценты) больше половины дохода --> кредит не выдаётся
     */
    public boolean validateIfIncomingForYear(RqCredit data) {
        return new CalcService().calcYearPayment(data) <= (data.getIncomeForLastYear() / 2);
    }


}
