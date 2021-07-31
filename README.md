# Instagram Backend with Spring Boot

1. application.yaml

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
    password: ufo950327
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

2. mysql docker

```shell
docker pull mysql:latest
docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=ufo950327 mysql
```

