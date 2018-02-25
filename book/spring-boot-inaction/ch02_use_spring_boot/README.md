# ch02 스프링 부트 사용하기

## index
- <a href="#2.1">2.1 스프링 부트 사용하기</a>
- <a href="#2.2">2.2 스타터 의존성 사용하기</a>


<div id="2.1"></div>

## 2.1 스프링 부트 사용하기

> Spring cli  

```
$spring init -d=web,thymeleaf, data-jpa,h2 --groupId=com.manning \
--artifactId=readinglist --name="Reading List" --package-name=readinglist \
--description="Reading List Demo" --build gradle readinglist  
```

### 2.1.1 갓 초기화한 스프링 부트 프로젝트 살펴보기

![project init](./pics/[2-1]project.png)    

- build.gradle : 그레이들 빌드 명세
- gradlew : 그레이들 레퍼
- ReadingListApplication.java : 어플리케이션의 부트스트랩 클래스이자  
주 스프링 구성 클래스  
- application.properties : APP과 스프링 부트 프로퍼티를 구성하는 파일  
- ReadingListApplicationTest.java : 기본 통합 테스트 클래스

**스프링 부트스트래핑**  

> ReadlingListApplication.java  

```
package readinglist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 컴포넌트 검색과 자동 구성 활성화
public class ReadingListApplication {

	public static void main(String[] args) {
    // 애플리케이션 부트스트랩
		SpringApplication.run(ReadingListApplication.class, args);
	}
}
```  

1. @SpringBootApplication  
; 스프링 컴포넌트 검색 + 스프링 부트 자동 구성을 활성화  
아래 기능을 묶은 것

- 스프링의 @Configuration  
: 스프링의 자바 기반 구성 클래스로 지정.
- 스프링의 @ComponentScan  
: 컴포넌트 검색 기능을 활성화해서 웹 컨트롤러 클래스나 다른 컴포넌트  
클래스들을 자동으로 검색하여 빈으로 등록
- 스프링부트의 @EnabledAutoConfiguration  
: 이 구성 한 줄로 스프링 부트의 자동 구성이 일어나 수많은 구성 코드를 대체  

> 애플리케이션 빌드 및 실행  

```$ gradle bootRun  ```  

```
$gradle build
$java -jar build/libs/readinglist-0.0.1-SNAPSHOT.jar
```   

**스프링 부트 애플리케이션 테스트**  
**애플리케이션 프로퍼티 구성**  

### 2.1.2 스프링 부트 프로젝트 빌드 파헤치기  

> 스프링 부트 그레이들 플러그인 사용  

```
buildscript {
	ext {
		springBootVersion = '2.0.0.RC2'
	}
	repositories {
		mavenCentral()
		maven { url "https://repo.spring.io/snapshot" }
		maven { url "https://repo.spring.io/milestone" }
	}
	dependencies {
    // 스프링 부트 플러그인 의존성
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
	}
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot' // 스프링 부트 플러그인 적용
apply plugin: 'io.spring.dependency-management'

group = 'com.manning'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8
targetCompatibility = 1.8

repositories {
	mavenCentral()
	maven { url "https://repo.spring.io/snapshot" }
	maven { url "https://repo.spring.io/milestone" }
}


dependencies {
  // 스타터 의존성
	compile('org.springframework.boot:spring-boot-starter-data-jpa')
	compile('org.springframework.boot:spring-boot-starter-thymeleaf')
	compile('org.springframework.boot:spring-boot-starter-web')
	runtime('org.springframework.boot:spring-boot-devtools')
	runtime('com.h2database:h2')
	compileOnly('org.projectlombok:lombok')
	testCompile('org.springframework.boot:spring-boot-starter-test')
}

```  

> pom.xml  

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.manning</groupId>
	<artifactId>readingList</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>demo</name>
	<description>Reading List Demo</description>

	<parent> <!-- 부모 스타터에서 버전 상속 -->
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.10.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```  

=> spring-boot-starter-parent를 부모로 지정  
=> 메이븐의 의존성 관리 기능으로 자주 사용하는 라이브러들의 의존성 버전을  
상속받을 수 있어서 의존성을 선언할 때 버전을 명시X  

---  

<div id="2.2"></div>

## 2.2 스타터 의존성 사용하기  












<br><br><br><br><br><br><br><br><br><br><br><br>

---
