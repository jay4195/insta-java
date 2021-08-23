# Instagram Backend with Spring Boot

### 1.Updates

#### [version 0.2.1]

- Change Password

#### [version 0.2.0]

- Kafka Message Queue
- Eureka Service Registry Center

#### [version 0.1.3]

- Direct Message Controller Add

#### [version 0.1.2]

- fix profile follow bugs

#### [version 0.1.1] 

- token with 24 hours
- fix followers bug at profile page
- add comment captions and hashtags

### 2.Configs

```yaml
instagram:
  server:
    serverDirPath: /home/wangyan8880/java-workspace/instagram/
    imgSrcPath: http://localhost:8080/upload/

server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/instagram?serverTimezone=Asia/Shanghai
    username: root
    password: xxxxxx
    driver-class-name: com.mysql.cj.jdbc.Driver
  jdbc:
    template:
      query-timeout: 3
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

mybatis:
  mapper-locations: classpath:mybatis/*.xml
  configuration:
    map-underscore-to-camel-case: true
```



```shell
docker pull mysql:latest
docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=ufo950327 mysql
```

