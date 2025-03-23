# LendFlow - Java Lending Application

LendFlow is a loan management system designed to handle various loan operations, including product management, fee configurations, loan lifecycle tracking, and notifications.

## API Endpoints

### Loan Product Management

#### Get Tenure Duration Types
**GET**: `http://localhost:8080/v1/product/duration-types`

**Sample Response:**
```json
[
    { "id": "1", "name": "Day" },
    { "id": "2", "name": "Month" },
    { "id": "3", "name": "Year" }
]
```

#### Create a Loan Product
**POST**: `http://localhost:8080/v1/product`

**Sample Request:**
```json
{
  "name": "Personal Loan",
  "tenureDuration": 24,
  "tenureDurationTypeID": 2,
  "daysAfterDueForFeeApplication": 5,
  "disbursementType": "LUMP_SUM",
  "disbursementIntervalInDays": 30,
  "billingCycle": "MONTHLY",
  "fees": [
    { "feeTypeId": 1, "amount": 100.00, "rate": 0.0 },
    { "feeTypeId": 2, "amount": 0.00, "rate": 0.3 }
  ]
}
```

**Sample Response:**
```json
{
    "productId": 1,
    "name": "Personal Loan",
    "tenure": "24 Months",
    "daysAfterDueForFeeApplication": 5,
    "disbursementType": "LUMP_SUM",
    "disbursementIntervalInDays": 0,
    "billingCycle": "MONTHLY",
    "fees": [
        {
            "rate": 0.0,
            "feeType": "PERCENTAGE_ON_AMOUNT",
            "feeTypeName": "SERVICE_FEE",
            "whenToCharge": "BEFORE"
        },
        {
            "amount": 0.0,
            "feeType": "FIXED_AMOUNT",
            "feeTypeName": "SERVICE_FEE",
            "whenToCharge": "BEFORE"
        }
    ]
}
```

#### Get Loan Product by ID
**GET**: `http://localhost:8080/v1/product/1`

**Sample Response:**
```json
{
    "productId": 1,
    "name": "Construction Loan",
    "tenure": "12 Months",
    "daysAfterDueForFeeApplication": 5,
    "disbursementType": "LUMP_SUM",
    "disbursementIntervalInDays": 0,
    "billingCycle": "MONTHLY",
    "fees": [
        {
            "rate": 0.0,
            "feeType": "PERCENTAGE_ON_AMOUNT",
            "feeTypeName": "SERVICE_FEE",
            "whenToCharge": "BEFORE"
        },
        {
            "amount": 0.0,
            "feeType": "FIXED_AMOUNT",
            "feeTypeName": "SERVICE_FEE",
            "whenToCharge": "BEFORE"
        }
    ]
}
```

### Profile Management

#### Create Profile
**POST**: `http://localhost:8080/v1/profile`

**Sample Request:**
```json
{
    "msisdn": "254712345678",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "address": "123 Main St",
    "pinHash": "hashed_pin",
    "deviceId": "ezRTGHJ6789076545678",
    "preferredNotificationChannel": "SMS",
    "creditScore": 750.0
}
```

**Sample Response:**
```json
{
    "profileId": 1,
    "msisdn": "254712345678",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "address": "123 Main St",
    "pinStatus": 1,
    "creditScore": 750.0
}
```

#### Get Profile by MSISDN
**GET**: `http://localhost:8080/v1/profile/254712345678`

**Sample Response:**
```json
{
    "profileId": 1,
    "msisdn": "254712345678",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "address": "123 Main St",
    "pinStatus": 1,
    "creditScore": 750.00
}
```

#### Update Credit Score
**PUT**: `http://localhost:8080/v1/profile/254712345678/credit-score?newCreditScore=459`

**Sample Response:**
```json
{
    "profileId": 1,
    "msisdn": "254712345678",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "address": "123 Main St",
    "pinStatus": 1,
    "creditScore": 459
}
```

### Loan Management

#### Request a Loan
**POST**: `http://localhost:8080/v1/loan/1/request`

**Sample Request:**
```json
{
    "requestedAmount": 400000,
    "gracePeriodInDays": 180,
    "disbursementInstallments": 12,
    "coolingOffPeriodInDays": 30
}
```

**Sample Response:**
```json
{
    "loanName": "Personal Loan",
    "principal": 400000,
    "principalDisbursed": 400000.0,
    "gracePeriodInDays": 0,
    "loanState": "OPEN",
    "fullyDisbursed": false
}
```

#### Cancel a Loan
**PUT**: `http://localhost:8080/v1/loan/1/cancel`

**Sample Response:**
```
Loan cancelled successfully.
```

### Notifications

#### Get Notifications by MSISDN
**GET**: `http://localhost:8080/v1/notification/254712345678`

**Sample Response:**
```json
[
    {
        "message": "Your loan of 400000 has been received.",
        "channel": "SMS",
        "dateSend": null,
        "delivered": false,
        "eventType": "LOAN_CREATED"
    }
]
```

