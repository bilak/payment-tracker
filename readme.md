# Payment Tracker
Application holding history of payments. Each payment consist of currency and amount. Once per minute the balance of each currency is printed to console.

## Requirements
JDK 1.8

## Build
In console execute command  `mvn clean install`
 
## Run 
To run application execute command `mvn spring-boot:run`.

### Run parameters
If you want to include initial data add parameter `-Dbalance_file=<path_to_file>`
If you want to include exchange rates add parameter `-Dexchange_file=<pat_to_file>`

for example: `mvn spring-boot:run -Dbalance_file=./balance.txt -Dexchange_file=./exchanges.txt`

### Files formats
#### Balance File
`CURERNCY AMOUNT` where CURRENCY is ISO 4217 currency code and amount is decimal number
#### Exchange File
`TARGETCURRENCY/SOURCECURRENCY AMOUNT` where TARGETCURRENCY and SOURCECURRENCY are ISO 4217 currency codes, SOURCECURRENCY is currency which whill be converted,
  TARGETCURRENCY is currency to which SOURCECURRENCY will be converted and  AMOUNT is decimal number of exchange rate.
  
  
## Inputs
To add some inputs to application in console write `CURERNCY AMOUNT` where CURRENCY is ISO 4217 currency code and amount is decimal number and then hit enter.
If input has some error (bad currency, bad decimal number, ...) it will be logged to console, but program will continue to work.

### Example
inputs
```
EUR 100
EUR 20
USD 10
EUR -200
GBP 30
USD 20
GBP -30
```
should print
```
EUR -80
USD 30
```
## Program termination
To quit the program type in console quit or press `ctrl+c`