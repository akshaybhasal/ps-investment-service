### ps-investment-service

---------------------------

The service is responsible to save investor details along with associated funds and holdings

#### Below are the functionalities available in the service
* Ability to create an investor along with associated funds and holdings
* Ability to retrieve total market value for any given fund
* Ability to retrieve total market value for all the funds associated to an investor
* Ability to exclude specific holding or list of holdings while calculating market value

### Swagger UI Link
http://localhost:8091/swagger-ui/index.html#/

### Rest Endpoints

1. Create an investor with associated funds and holdings

Sample Endpoint - http://{{host}}:{{port}}/api/v1/investments/investors

Sample Request
```commandline
{
    "name": "Investor1",
    "funds": [
        {
            "name": "Fund1",
            "holdings": [
                {
                    "name": "Fund1-Holding1",
                    "marketValue": 10,
                    "quantity": 100
                },
                {
                    "name": "Fund1-Holding2",
                    "marketValue": 20,
                    "quantity": 100
                }
            ]
        },
        {
            "name": "Fund2",
            "holdings": [
                {
                    "name": "Fund2-Holding1",
                    "marketValue": 15,
                    "quantity": 100
                },
                {
                    "name": "Fund2-Holding2",
                    "marketValue": 20,
                    "quantity": 100
                }
            ]
        }
    ]
}
```

Sample Response

```commandline
{
    "name": "Investor1",
    "externalInvestorId": "29aa8e24-f8ef-42e0-b80a-d474e144bcc9",
    "funds": [
        {
            "name": "Fund1",
            "externalFundId": "a99f53ac-2707-4c40-95a7-6bc295184eff"
        },
        {
            "name": "Fund2",
            "externalFundId": "7566a40e-26f8-4810-9b92-1a2979d90fa9"
        }
    ]
}
```

2. Retrieve total market value for any given fund excluding specific or list of holdings

Sample Endpoint - http://{{host}}:{{port}}/api/v1/investments/funds/{externalFundId}/market-value?excludeHoldings=Fund1-Holding1

Sample Response

```commandline
{
    "totalMarketValue": 2000
}
```

3. Retrieve total market value for any given investor for all the associated fundings excluding specific or list of holdings

Sample Endpoint - http://{{host}}:{{port}}/api/v1/investments/investors/{externalInvestorId}/market-value?excludeHoldings=Fund1-Holding1

Sample Response

```commandline
{
    "totalMarketValue": 5500
}
```