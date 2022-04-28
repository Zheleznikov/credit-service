package ru.praktikum.task.creditservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.praktikum.task.creditservice.dto.RqCredit;
import ru.praktikum.task.creditservice.dto.RsCredit;
import ru.praktikum.task.creditservice.service.CalcService;
import ru.praktikum.task.creditservice.service.ValidationService;

@RestController
public class CreditController {

    @Autowired
    ValidationService validation;

    @Autowired
    CalcService calc;

    @PostMapping(value = "/check-info-about-credit")
    public RsCredit getInfoAboutCredit(@RequestBody RqCredit data) {
        System.out.println(data);

        return validation.validateRq(data) ?
                new RsCredit().setYearPayment(calc.calcYearPayment(data)).setIsCreditApproved(true) :
                new RsCredit().setIsCreditApproved(false);
    }


}
