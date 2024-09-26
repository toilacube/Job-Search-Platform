When update email, user have to login again?

## Requirements
- JDK 17 or higher

## Usage
If you want to test the RabbitMQ email service, go to `application.yml` and replace your email in order to receive the notificaiton:
```
test:
    email: youremail@gmail.com
```
**User account:** 
Email: `user@gmail.com` Password: `string`

**Admin account:** Email: `admin@gmail.com` Password: `string`
## How to run
```
git clone .....
cd ....
```
### Start docker compose
postgres will run in port `5430` and rabbitmq will run in port `5672` and `15672` 
```
docker compose up -d
```

**If you are using Intellij or VScode, just select required JDK version and run**

**For terminal only:**

Make sure to have [mvn](https://maven.apache.org/install.html) installed and Java version 17 or higher


```
mvn spring:build
```

