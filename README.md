## Note
The job recommendation in task 6 was built using the **Full text search** feature from posgresql.

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
**Open terminal**
```
git clone .....
cd ....
```
**Start docker compose**

postgres will run in port `5430` and rabbitmq will run in port `5672` and `15672` 
```
docker compose up -d
```

**Remember to use JDK 17 or higher**

****

```
./mvnw spring-boot:run
```

