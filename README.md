Upnid Challenge
===========

Overview
--------

The challenge is to develop a solution that is able to assess whether the people who are making purchases in e-commerce are customers that we can trust, preventing purchases from fraudsters from being approved. The API will have to respond, whether a purchase should be approved or declined.


# Solution

As i don't have much experience with Golang, my choice of technology was Java because it is statically typed and compiled like this with Go and because I have a good experience with it.

As a scope of the application, to focus efforts on building the algorithm that responds to the problem, I assumed the following context for the API:

- It would be a micro service, which would run on a private network, and which would have other measures to guarantee its exclusive access to the owner, thus being able to ignore factors such as tokens and request origins.
- It would receive the data previously handled by another service, which would deliver the transaction data already validated.
- The format of the data he would receive would have the following format:

```yaml
{
    "customer": {
        "name": "Geralt of Rivia",
        "email": "opa@gmail.com",
        "cpf": "00000000000",
        "phone": "99999999999",
        "createdAt": "2019-12-03 00:"
    },
    "card": {
        "number": "0000-0000-0000-0000",
        "holder": "Geralt of Rivia",
        "cvc": "123"
    },
    "shipping": {
        "address": "Rua das ruas",
        "country": "Brazil",
        "zip": "12345-678",
        "number": 1,
        "aditional": "Logo ali",
        "price": 10
    },
    "details": {
        "ip": "177.37.155.124", // address from user
        "totalValue": 10000 // value in cents
    }
}
```

- There is a metrics service that supports the decision of the algorithm based on statistics and AI on the transaction database. (This service is referred to in the application as MetricsService and for simplification it returns constant values)

The algorithm responds based on a set of tests that are done with the transaction data, judging whether it fits in the most common cases of fraud, which are here:

- Many transactions / very high transactions from a new customer (common to do this before the card is blocked)
- International purchases (when the address is not registered or the shipping cost is higher/much higher than the price of the product).
- Many transactions in a small period that is well above the average for that user, given that he is already known on the platform.
- Many transactions using different cards, sending to the same adress.

Other points that I would like to have implemented but due to the time available it was not possible to compare the zone of the ip address with the delivery address when possible and also the situation of multiple accounts using the same credit card.

A request that does not fit into any of these suspect cases is approved.

However many features that i would like to have delivered but the small time i got to work did not allow can be listed following:

## Building from source

1. Ensure you have 

   ```java``` installed - goto https://www.oracle.com/java/technologies/javase-downloads.html to download installer for your OS.    
   ```maven``` installed - goto https://maven.apache.org/download.cgi to download latest version of maven.

1. Clone this repository to your local filesystem (default branch is 'master')

1. To download the dependencies
   ```
    mvn install
   ```

1. To run the application, run the following command on the project root folder

   ```
    mvn spring-boot:run
   ```

## Running maven tasks


### Build

For production you need to provide to enviroment variables:

* `DATABASE_URL`: Url for ProstgreSQL Database

With this you need just run the following commands:

* `mvn install`

* `java -jar target/*.jar`

and the aplication will start in port 8080. Opitionaly you can use a specific port:

* `java -jar target/*.jar --server.port=$PORT`

### Docker

Additionally, you can use docker container to automatise backend deployment using the image available in docker hub 

``` 
    docker pull gmonteiro/upnid-challenge:latest
```

ou building from source following next steps o backend bolder:

```
    docker build . && docker-compose build && docker-compose run springbootapp
```


## Running the tests

You just need to run the following command:

`mvn test`


## Contribution guidelines

Not yet