-- HW#4
-- Section 03
-- Student ID: 012232922
-- Song, Jimin
## _Console output version_

Requirments

- Java 8 or above
- Java SE development kid (JDK)
- Docker and DB2

Tested on

- JDK 17
- Intelijj Idea Ultimate
- Windows 10 64 Bit
- Docker 4.2.0

## Features

- Create New customer
- Login to Customer
- Login to Administrator
- Admin can view report of specific customer
- Admin can view summary of all customer and accounts
- Admin can view summary with Min and Max age Limit
- Customer can Open Account
- Customer can close account
- Customer can Deposit into account
- Customer can withdraw from his/her account
- Customer can transfer from his/her account to another account or his/her account
- Customer can view summary of his account/s

> GUI version is also available

## Run

Run in CMD or Terminal

```sh
docker run -itd --name mydb2 --privileged=true -p 50000:50000 -e LICENSE=accept -e DB2INST1_PASSWORD=kenward -e DBNAME=testdb -v luw:/database ibmcom/db2

docker exec -ti mydb2 bash -c "su - db2inst1"

db2 "connect to sample"

docker cp ./p1_create.sql <Containder ID here>:./database/config/db2inst1/

db2 -tvf p1_create.sql

Javac P1.Java

Java P1
```
