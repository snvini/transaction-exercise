# Transaction exchange - REST API

A rest api using Java 17 + Spring boot + Postgres + Docker-Compose for study.

### What it does ?

This api saves a simple transaction and can consult on [Treasury Reporting Rates of Exchange API](https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange) an exchange rate based on currency.

## Getting Started

You will just need [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/install/) to run this application !

To run the application, just do the following command at the root directory
```
docker-compose pull && docker-compose up -d (for linux)
docker-compose pull ; docker-compose up -d (for windows)
```
**Attention, be sure that your machine can run "docker-compose" commands !**


## API Endpoints and Usage

The server will listen at **http://localhost:8081/planets**

You can also use the swagger-ui to access the documentation and try the endpoins at **http://localhost:8081/swagger-ui/index.html#/**

| function             	| HTTP Verb 	| Endoint          	| Request Param             	| Body                          	|
|----------------------	|-----------	|------------------	|---------------------------	|-------------------------------	|
| List transactions    	| GET       	| /transactions    	|                           	|                               	|
| Search by id         	| GET       	| /transactions/id 	|                           	|                               	|
| Save new Transaction 	| POST      	| /transactions    	|                           	| Look at transacion json below 	|
| Get exchange         	| GET       	| /exchange        	| currency, id(transaction) 	|                               	|

### Transaction JSON document

Transaction needs to have the following attributes:
- description (cannot be null and cannot be more than 50 characters)
- transactionDate ("YYYY-mm-dd")
- amount (cannot be null)

Transaction JSON Example:
```
{
 "description": "Books",
 "transactionDate": "2023-10-12",
 "amount": 230
}
```

## License
[MIT](https://choosealicense.com/licenses/mit/)