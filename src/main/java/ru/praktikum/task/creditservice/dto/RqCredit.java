package ru.praktikum.task.creditservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RqCredit {
    int age;
    String sex;
    String incomingSource; //[пассивный доход, наёмный работник, собственный бизнес, безработный] [passive income, employee, own business, unemployed]
    double incomeForLastYear; // доход за последний год
    int creditRating; // [-2, -1, 0, 1, 2]
    double requestedAmount; // запрашиваемая сумма
    int loanRepaymentTime; // [1.. 20] срок погашения
    String goal; // [ипотека, развитие бизнеса, автокредит, потребительский] [mortgage, business development, car loan, personal loan]
}
