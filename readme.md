# MVP cервиса для расчета выдачи кредита

## Как запустить
Открыть через intellij idea, дождаться установки всех зависимостей и запустить класс CreditServiceApplication.
После этого сервер будет развернут на `http://localhost:8080/`

Для запуска юнит-тестов можно использовать команду `mvn test`

## Как отправить запрос
Надо отправить POST запрос на эндпоинт `check-info-about-credit`

### Пример запроса
    {
    "age": 38, // [0, ...]
    "sex": "male", // [male, female]
    "incomingSource": "own business", //[passive income, employee, own business, unemployed]
    "incomeForLastYear": 10, 
    "creditRating": -1, //  [-2, -1, 0, 1, 2]
    "requestedAmount": 6, // [0.1 ... 10]
    "loanRepaymentTime": 4, // [1...20]
    "goal": "mortgage" // [mortgage, business development, car loan, personal loan]
    }

### Схема
        {
        "$schema": "http://json-schema.org/draft-04/schema#",
        "type": "object",
        "properties": {
        "age": {
        "type": "integer"
        },
        "sex": {
        "type": "string"
        },
        "incomingSource": {
        "type": "string"
        },
        "incomeForLastYear": {
        "type": "integer"
        },
        "creditRating": {
        "type": "integer"
        },
        "requestedAmount": {
        "type": "integer"
        },
        "loanRepaymentTime": {
        "type": "integer"
        },
        "goal": {
        "type": "string"
        }
        },
        "required": [
        "age",
        "sex",
        "incomingSource",
        "incomeForLastYear",
        "creditRating",
        "requestedAmount",
        "loanRepaymentTime",
        "goal"
        ]
        }

### Пример ответа
#### Кредит не одобрен
    {
    "isCreditApproved": false
    }
#### Кредит одобрен
    {
    "isCreditApproved": true,
    "yearPayment": 1.93
    }

Отправляйте свои данные и проверяйте свои шансы на получения кредита.