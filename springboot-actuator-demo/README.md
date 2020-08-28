# Spring boot with actuator  

- [Health](#Health)  

---  

# Health  

3 components are [`db`, `redis`, `custom indicator`]  

> Getting started  

```bash
// start mysql and redis
$ docker-compose -f tools/compose/mysql-compose.yaml up
$ docker-compose -f tools/compose/redis-compose.yaml up


// start application
$ ./gradlew boot:Run

// request health
$ curl -XGET http://localhost:3000/manage/health | jq .
{
    "status": "UP",
    "components": {
        "custom": {
            "status": "UP",
            "details": {
                "type": "custom indicator"
            }
        },
        "db": {
            "status": "UP",
            "details": {
                "database": "MySQL",
                "validationQuery": "isValid()"
            }
        },
        "redis": {
            "status": "UP",
            "details": {
                "version": "5.0.9"
            }
        }
    }
}
```  



> application.yaml  

```yaml
management:
  health:
    diskspace:
      enabled: false
    datasource:
      enabled: true
    redis:
      enabled: true
    ping:
      enabled: false
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      # default value is "/actuator"
      base-path: /manage
```  






---  

# References  

- [Actuator api docs](https://docs.spring.io/spring-boot/docs/current/actuator-api/html/)  
- [Actuator reference docs](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html)

---

## TODO  

- final status is "UP" if running database only. i.e redis health indicator is just observer  

