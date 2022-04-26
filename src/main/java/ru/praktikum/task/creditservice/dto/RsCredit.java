package ru.praktikum.task.creditservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RsCredit {
    Boolean isCreditApproved;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Double yearPayment;
}
