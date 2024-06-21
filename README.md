# microservices-test

1. Environment
- Java version 21.0.1
- Docker version 20.10.7

2. Start up applications:
```sh
cd microservices-test

cd registry-service
    mvn spring-boot:run
    
cd ../gateway-service
    mvn spring-boot:run
    
cd ../client-microservice
    mvn spring-boot:run
    
cd ../account-microservice
    mvn spring-boot:run
```

2. Client genders
- MALE
- FEMALE
- OTHERS

3. Account types
- SAVINGS
- CHECKING

3 Account status
- ACTIVE
- HOLD

4. Transaction types
- DEPOSIT
- WITHDRAWAL

2. The postman collection is in the docs directory.


3. GitHub repository:
- https://github.com/gveramendi/microservices-test
