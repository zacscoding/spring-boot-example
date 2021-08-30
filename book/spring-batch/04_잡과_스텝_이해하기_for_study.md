# 잡과 스텝 이해하기

- [잡 구성하기](#잡-구성하기)
  - [잡의 기본 구성](#잡의-기본-구성)
  - [잡 파라미터](#잡-파라미터)
    - [잡 파라미터에 접근하기](#잡-파라미터에-접근하기)
    - [잡 파라미터 유효성 검증하기](#잡-파라미터-유효성-검증하기)
    - [잡 파라미터 증가시키기](#잡-파라미터-증가시키기)
  - [잡 리스너 적용하기](#잡-리스너-적용하기)
  - [ExecutionContext](#ExecutionContext)
    - [ExecutionContext 조작하기](#ExecutionContext-조작하기)
    - [ExecutionContext 저장하기](#ExecutionContext-저장하기)
- [스텝 알아보기](#스텝-알아보기)
  - [스텝 구성](#스텝-구성)
  - [그 밖의 여러 다른 유형의 태스크릿 이해하기](#그 밖의 여러 다른 유형의 태스크릿 이해하기)

---  

# 잡 구성하기  

## 잡의 기본 구성

**1. pom.xml**  

```xml
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <scope>runtime</scope>
    </dependency>
    <!-- lombok -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.batch</groupId>
      <artifactId>spring-batch-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
```  

**2. application.yaml**  

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:53306/spring_batch
    username: root
    password: p@ssw0rd
  batch:
    jdbc:
      initialize-schema: always
```  

**3. HelloWorldJob.java**  

```java
@EnableBatchProcessing
@SpringBootApplication
public class HelloWorldJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("basicJob")
                                     .start(step1())
                                     .build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                                      .tasklet((contribution, chunkContext) -> {
                                          System.out.println("Hello, world!");
                                          return RepeatStatus.FINISHED;
                                      }).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldJob.class, args);
    }
}
```

**4. Maven install**  

```shell
$ ./mvnw clean install -pl Chapter03
$ ls -la ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar                                                                                                                                                                                                                                                                                                      master!?
-rw-r--r--  1 user  staff  18137338 Aug 30 21:21 ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar 
```

**5. Run**  

```shell
$ java -jar ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar foo=bar

...
2021-08-30 21:30:21.173  INFO 72093 --- [           main] o.s.b.c.r.s.JobRepositoryFactoryBean     : No database type set, using meta data indicating: MYSQL
2021-08-30 21:30:21.331  INFO 72093 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2021-08-30 21:30:21.414  INFO 72093 --- [           main] io.spring.batch.HelloWorldJob            : Started HelloWorldJob in 1.868 seconds (JVM running for 2.217)
2021-08-30 21:30:21.416  INFO 72093 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: [foo=bar]
2021-08-30 21:30:21.520  INFO 72093 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=basicJob]] launched with the following parameters: [{foo=bar}]
2021-08-30 21:30:21.582  INFO 72093 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
Hello, world!
2021-08-30 21:30:21.630  INFO 72093 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 48ms
2021-08-30 21:30:21.658  INFO 72093 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=basicJob]] completed with the following parameters: [{foo=bar}] and the following status: [COMPLETED] in 111ms
2021-08-30 21:30:21.661  INFO 72093 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2021-08-30 21:30:21.670  INFO 72093 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

// 같은 JobParameter를 이용하여 다시 실행
$ java -jar ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar foo=bar

... 
2021-08-30 21:30:51.133  INFO 72993 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2021-08-30 21:30:51.222  INFO 72993 --- [           main] io.spring.batch.HelloWorldJob            : Started HelloWorldJob in 1.86 seconds (JVM running for 2.21)
2021-08-30 21:30:51.224  INFO 72993 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: [foo=bar]
2021-08-30 21:30:51.339  INFO 72993 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2021-08-30 21:30:51.357 ERROR 72993 --- [           main] o.s.boot.SpringApplication               : Application run failed

java.lang.IllegalStateException: Failed to execute ApplicationRunner
  ....
Caused by: org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException: A job instance already exists and is complete for parameters={foo=bar}.  If you want to run this job again, change the parameters.
2021-08-30 21:30:51.428  INFO 72993 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2021-08-30 21:30:51.440  INFO 72993 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.


// '-' prefix를 통해 IDENTIFYING 여부를 줄 수 있다. (BATCH_JOB_EXECUTION_PARAMS 테이블에서 확인 가능)
$ java -jar ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar -name=Michael 'executionDate(date)=2020/12/30'
...
2021-08-30 21:37:35.571  INFO 75765 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=basicJob]] completed with the following parameters: [{name=Michael, executionDate=1609254000000}] and the following status: [COMPLETED] in 106ms
...
```  

---  

## 잡 파라미터

### 잡 파라미터에 접근하기  

**1. ChunkContext를 통해 접근하기**

```java
@Bean
public Tasklet helloWorldTasklet() {
    return (contribution, chunkContext) -> {
        String name = (String) chunkContext.getStepContext()
                                           .getJobParameters()
                                           .get("name");

        logger.info("Hello, {}!", name);
        return RepeatStatus.FINISHED;
    };
}
```  

**2. 늦은 바인딩(rate binding)을 이용하여 접근하기**

```java
@StepScope
@Bean
public Tasklet helloWorldTaskletWithLateBind(
        @Value("#{jobParameters['name']") String name) {
    return (contribution, chunkContext) -> {
        logger.info("Hello, {}!", name);
        return RepeatStatus.FINISHED;
    };
}
```  

### 잡 파라미터 유효성 검증하기  

**1. 직접 검증하기**  

```java
public class ParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        final String fileName = parameters.getString("fileName");

        if (!StringUtils.hasText(fileName)) {
            throw new JobParametersInvalidException("fileName parameter is missing");
        }

        if (!StringUtils.endsWithIgnoreCase(fileName, "csv")) {
            throw new JobParametersInvalidException("fileName parameter does not use the csv file extension");
        }
    }
}

// use
@Bean
public Job job() {
    return this.jobBuilderFactory.get("basicJob")
                                 .start(step1())
                                 .validator(new ParameterValidator())
                                 .build();
}
```  

**2. DefaultJobParametersValidator로 검증하기**  


```java
/**
 * {@link DefaultJobParametersValidator}를 이용하여 JobParameters 유효성을 검증한다.
 * - setRequiredKeys: 필수 파라미터
 * - setOptionalKeys: 선택 파라미터
 * - 그 외의 다른 파라미터는 유효성 검증 실패
 */
@Bean
public JobParametersValidator jobParametersValidator() {
    final DefaultJobParametersValidator validator = new DefaultJobParametersValidator();

    validator.setRequiredKeys(new String[] { "fileName" });
    validator.setOptionalKeys(new String[] { "name" });

    return validator;
}
```  

**3. CompositeJobParametersValidator로 검증하기**  

```java
/**
 * {@link CompositeJobParametersValidator}를 이용하여 다중 {@link JobParametersValidator}를 추가한다.
 * 아래의 경우 {@link ParameterValidator} -> {@link DefaultJobParametersValidator} 순으로 유효성을 검증한다.
 */
@Bean
public CompositeJobParametersValidator compositeJobParametersValidator() {
    final CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

    final DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
            new String[] { "fileName" }, new String[] { "name" }
    );
    defaultJobParametersValidator.afterPropertiesSet();

    validator.setValidators(
            Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));

    return validator;
}
```

**확인**  

```shell
// 필수 파라미터 누락
$ java -jar ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar

...
2021-08-30 22:04:56.669 ERROR 77998 --- [           main] o.s.boot.SpringApplication               : Application run failed

java.lang.IllegalStateException: Failed to execute ApplicationRunner
        ...
Caused by: org.springframework.batch.core.JobParametersInvalidException: fileName parameter is missing
        ...

// 정상 호출 
$ java -jar ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar fileName=foo.csv name=Michael

...
2021-08-30 22:06:49.559  INFO 78242 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
Hello, world!
2021-08-30 22:06:49.605  INFO 78242 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 44ms
...    
```  

### 잡 파라미터 증가시키기  

**RunIdIncrementer를 이용하여 증가시키기**

```java
@Bean
public Job job() {
    return this.jobBuilderFactory.get("basicJob")
                                 .start(step1())
                                 .validator(compositeJobParametersValidator())
                                 .incrementer(new RunIdIncrementer()) // 잡 파라미터 증가시키기
                                 .build();
}

...
@Bean
public CompositeJobParametersValidator compositeJobParametersValidator() {
    final CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

    final DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
        new String[] { "fileName" }, new String[] { "name", "run.id" }
    );
    defaultJobParametersValidator.afterPropertiesSet();

    validator.setValidators(
    Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));

    return validator;
}
```  

**3번 실행 후 DB 확인**  

```shell
mysql> SELECT * FROM BATCH_JOB_EXECUTION_PARAMS;
+------------------+---------+----------+------------+----------------------------+----------+------------+-------------+
| JOB_EXECUTION_ID | TYPE_CD | KEY_NAME | STRING_VAL | DATE_VAL                   | LONG_VAL | DOUBLE_VAL | IDENTIFYING |
+------------------+---------+----------+------------+----------------------------+----------+------------+-------------+
|                1 | STRING  | name     | Michael    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                1 | LONG    | run.id   |            | 1970-01-01 09:00:00.000000 |        1 |          0 | Y           |
|                1 | STRING  | fileName | foo.csv    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                2 | STRING  | name     | Michael    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                2 | STRING  | fileName | foo.csv    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                2 | LONG    | run.id   |            | 1970-01-01 09:00:00.000000 |        2 |          0 | Y           |
|                3 | STRING  | name     | Michael    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                3 | STRING  | fileName | foo.csv    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                3 | LONG    | run.id   |            | 1970-01-01 09:00:00.000000 |        3 |          0 | Y           |
+------------------+---------+----------+------------+----------------------------+----------+------------+-------------+
9 rows in set (0.00 sec)
```

**JobParametersIncrementer 구현하기 - Timestamp**

```java
public class DailyJobTimestamper implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder(parameters)
                .addDate("currentDate", new Date())
                .toJobParameters();
    }
}

...
@Bean
public CompositeJobParametersValidator compositeJobParametersValidator() {
    final CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
    final DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
        new String[] { "fileName" }, new String[] { "name", "run.id", "currentDate" }
    );
    defaultJobParametersValidator.afterPropertiesSet();

    validator.setValidators(
        Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));
    
    return validator;
}
```  

**확인하기**  

```shell
mysql> SELECT * FROM BATCH_JOB_EXECUTION_PARAMS;
+------------------+---------+-------------+------------+----------------------------+----------+------------+-------------+
| JOB_EXECUTION_ID | TYPE_CD | KEY_NAME    | STRING_VAL | DATE_VAL                   | LONG_VAL | DOUBLE_VAL | IDENTIFYING |
+------------------+---------+-------------+------------+----------------------------+----------+------------+-------------+
|                1 | STRING  | name        | Michael    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                1 | DATE    | currentDate |            | 2021-08-30 22:20:54.777000 |        0 |          0 | Y           |
|                1 | STRING  | fileName    | foo.csv    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                2 | STRING  | name        | Michael    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                2 | DATE    | currentDate |            | 2021-08-30 22:21:00.031000 |        0 |          0 | Y           |
|                2 | STRING  | fileName    | foo.csv    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                3 | STRING  | name        | Michael    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
|                3 | DATE    | currentDate |            | 2021-08-30 22:21:04.365000 |        0 |          0 | Y           |
|                3 | STRING  | fileName    | foo.csv    | 1970-01-01 09:00:00.000000 |        0 |          0 | Y           |
+------------------+---------+-------------+------------+----------------------------+----------+------------+-------------+
9 rows in set (0.00 sec)
```

---  

## 잡 리스너 적용하기  

**1. JobExecutionListener 구현하기**  

```java
/**
 * {@link Job}이 실행되기 전과 후에 로그를 출력한다.
 */
@Slf4j
public class JobLoggerListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    logger.info("{} is beginning execution", jobExecution.getJobInstance().getJobName());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    logger.info("{} has completed with the status {}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus());
  }
}

@Bean
public Job job() {
  return this.jobBuilderFactory.get("basicJob")
                               .start(step1())
                               .validator(compositeJobParametersValidator())
                               //.incrementer(new RunIdIncrementer()) // 잡 파라미터 증가시키기
                               .incrementer(new DailyJobTimestamper())
                               .listener(new JobLoggerListener())
                               .build();
}
```

**확인하기**

```shell
$ java -jar ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar fileName=foo.csv name=Michael 

...
2021-08-30 22:52:04.485  INFO 85900 --- [           main] io.spring.batch.JobLoggerListener        : basicJob is beginning execution
2021-08-30 22:52:04.519  INFO 85900 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
Hello, world!
2021-08-30 22:52:04.568  INFO 85900 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 49ms
2021-08-30 22:52:04.585  INFO 85900 --- [           main] io.spring.batch.JobLoggerListener        : basicJob has completed with the status COMPLETED
...
```  

**2. 어노테이션 사용하기**  

```java
@Slf4j
public class AnnotationJobLoggerListener {

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        logger.info("{} is beginning execution", jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        logger.info("{} has completed with the status {}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus());
    }
}

...
@Bean
public Job job() {
    return this.jobBuilderFactory.get("basicJob")
                                  .start(step1())
                                  .validator(compositeJobParametersValidator())
                                  .incrementer(new DailyJobTimestamper())
                                  .listener(JobListenerFactoryBean.getListener(new AnnotationJobLoggerListener()))
                                  .build();
}
```  

---  

## ExecutionContext  

### ExecutionContext 조작하기  

- 아래와 같이 StepContext -> StepExecution -> JobExecution -> ExecutionContext 조회
- ExecutionContextPromotionListener 활용
- ItemStream 활용

```java
/**
 * {@link JobParameters}로 전달된 "name" 값을 {@link ExecutionContext}에 {"user.name": name}으로 추가한다.
 */
@Slf4j
public class HelloWorldTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

        final String name = (String) context.getStepContext()
                                            .getJobParameters()
                                            .get("name");

        // context.getStepContext().getJobExecutionContext(); 를 통해 ExecutionContext 조회가 가능하지만,
        // Copy + Immutable Map 이므로 데이터 변경이 불가능하다.
        final ExecutionContext jobContext = context.getStepContext()
                                                   .getStepExecution()
                                                   .getJobExecution()
                                                   .getExecutionContext();
        jobContext.put("user.name", name);

        logger.info("Hello, {}", name);
        return RepeatStatus.FINISHED;
    }
}
```  

### ExecutionContext 저장하기

위의 `HelloWorldTasklet`을 수행 후 `*_EXECUTION_CONTEXT` 테이블을 조회 시 아래와 같이 Context를 확인할 수 있다.

```shell
mysql> SELECT * FROM BATCH_JOB_EXECUTION_CONTEXT;
+------------------+------------------------------------------------------+--------------------+
| JOB_EXECUTION_ID | SHORT_CONTEXT                                        | SERIALIZED_CONTEXT |
+------------------+------------------------------------------------------+--------------------+
|                1 | {"@class":"java.util.HashMap","user.name":"Michael"} | NULL               |
+------------------+------------------------------------------------------+--------------------+
1 row in set (0.00 sec)

mysql> SELECT * FROM BATCH_STEP_EXECUTION_CONTEXT;
+-------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------+
| STEP_EXECUTION_ID | SHORT_CONTEXT                                                                                                                                                     | SERIALIZED_CONTEXT |
+-------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------+
|                 1 | {"@class":"java.util.HashMap","batch.taskletType":"io.spring.batch.HelloWorldTasklet","batch.stepType":"org.springframework.batch.core.step.tasklet.TaskletStep"} | NULL               |
+-------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------+
1 row in set (0.00 sec)
```  

---  

# 스텝 알아보기

## 스텝 구성

### 태스크릿 스텝  

> **org.springframework.batch.core.step.tasklet.Tasklet**

<details>
<summary>Specification</summary>  

```java
/**
 * Strategy for processing in a step.
 *
 * @author Dave Syer
 * @author Mahmoud Ben Hassine
 *
 */
public interface Tasklet {

    /**
     * Given the current context in the form of a step contribution, do whatever
     * is necessary to process this unit inside a transaction. Implementations
     * return {@link RepeatStatus#FINISHED} if finished. If not they return
     * {@link RepeatStatus#CONTINUABLE}. On failure throws an exception.
     *
     * @param contribution mutable state to be passed back to update the current
     * step execution
     * @param chunkContext attributes shared between invocations but not between
     * restarts
     * @return an {@link RepeatStatus} indicating whether processing is
     * continuable. Returning {@code null} is interpreted as {@link RepeatStatus#FINISHED}
     *
     * @throws Exception thrown if error occurs during execution.
     */
    @Nullable
    RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception;

}
```
</details>  

```shell
/*
* Tasklet 인터페이스를 직접 구현할 수 있다.
*/
@Bean
public Step step1() {
    return this.stepBuilderFactory.get("step1")
                                  .tasklet((contribution, chunkContext) -> {
                                      logger.info("Hello, World!");
                                      return RepeatStatus.FINISHED;
                                  }).build();
}
```  

## 그 밖의 여러 다른 유형의 태스크릿 이해하기  

**1. org.springframework.batch.core.step.tasklet.CallableTaskletAdapter**  

```java
@Bean
public Callable<RepeatStatus> callableObject() {
    return () -> {
        logger.info("This was executed in another thread");
        return RepeatStatus.FINISHED;
    };
}

@Bean
public CallableTaskletAdapter callableTaskletAdapter() {
    final CallableTaskletAdapter callableTaskletAdapter = new CallableTaskletAdapter();

    callableTaskletAdapter.setCallable(callableObject());

    return callableTaskletAdapter;
}
```  

**2.org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter**  

```java
/**
 * 만약 TargetObject::Method 호출 결과가 {@link ExitStatus}로 캐스팅 가능한 {@link Object}를 반환하면
 * step의 exist status 값으로 사용한다. 또한 Tasklet은 {@link RepeatStatus.FINISHED}를 반환한다.
 */
@Bean
public MethodInvokingTaskletAdapter methodInvokingTaskletAdapter() {
    final MethodInvokingTaskletAdapter methodInvokingTaskletAdapter = new MethodInvokingTaskletAdapter();

    methodInvokingTaskletAdapter.setTargetObject(customService);
    methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");

    return methodInvokingTaskletAdapter;
}
```

```java
@StepScope
@Bean
public MethodInvokingTaskletAdapter methodInvokingTaskletAdapter2(
        @Value("#{jobParameters['message']}") String message) {
    final MethodInvokingTaskletAdapter methodInvokingTaskletAdapter = new MethodInvokingTaskletAdapter();

    methodInvokingTaskletAdapter.setTargetObject(customService);
    methodInvokingTaskletAdapter.setTargetMethod("service");
    methodInvokingTaskletAdapter.setArguments(new String[] { message });

    return methodInvokingTaskletAdapter;
}
```

**3.org.springframework.batch.core.step.tasklet.SystemCommandTasklet**

```java
@Bean
public SystemCommandTasklet systemCommandTasklet() {
    final SystemCommandTasklet tasklet = new SystemCommandTasklet();

    tasklet.setCommand("touch temp.txt");
    tasklet.setTimeout(5000L);
    tasklet.setInterruptOnCancel(true);

    tasklet.setWorkingDirectory("./.idea"); // 명령을 실행 할 디렉터리 설정
    tasklet.setSystemProcessExitCodeMapper(touchCodeMapper());  // 시스템 반환 코드 <-> ExitStatus 값 매핑
    tasklet.setTerminationCheckInterval(5000L); // 해당 명령 완료 체크 주기
    tasklet.setTaskExecutor(
            new SimpleAsyncTaskExecutor()); // 별도의 TaskExecutor로 수행. 시스템 명령 시 잡에 Lock 걸리는 현상을 막기 위해 동기식X
    tasklet.setEnvironmentParams(new String[] {
            "JAVA_HOME=/java", "USER_HOME=/Users/batch",
            }
    );

    return tasklet;
}

@Bean
public SimpleSystemProcessExitCodeMapper touchCodeMapper() {
    return new SimpleSystemProcessExitCodeMapper();
}
```  

## 청크 기반 스텝

```java
public class ChunkBasedBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkBasedJob() {
        return this.jobBuilderFactory.get("chunkBasedJob")
                                     .start(chunkStep())
                                     .build();
    }

    @Bean
    public Step chunkStep() {
        return this.stepBuilderFactory.get("chunkStep")
                                      // .<String, String>chunk(5)
                                      .<String, String>chunk(completionPolicy())
                                      .reader(itemReader())
                                      .writer(itemWriter())
                                      .build();
    }

    @Bean
    public ListItemReader<String> itemReader() {
        return new ListItemReader<>(
                IntStream.range(0, 1000).boxed().map(i -> UUID.randomUUID().toString()).collect(Collectors.toList())
        );
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> items.forEach(i -> logger.info(">> Current write item: {}", i));
    }

    @Bean
    public CompletionPolicy completionPolicy() {
        final CompositeCompletionPolicy policy = new CompositeCompletionPolicy();

        policy.setPolicies(new CompletionPolicy[] {
                new TimeoutTerminationPolicy(3),
                new SimpleCompletionPolicy(10)
        });

        return policy;
    }
}
```

```shell
mysql> SELECT * FROM BATCH_STEP_EXECUTION;
+-------------------+---------+-----------+------------------+----------------------------+----------------------------+-----------+--------------+------------+--------------+-------------+-----------------+------------------+--------------------+----------------+-----------+--------------+----------------------------+
| STEP_EXECUTION_ID | VERSION | STEP_NAME | JOB_EXECUTION_ID | START_TIME                 | END_TIME                   | STATUS    | COMMIT_COUNT | READ_COUNT | FILTER_COUNT | WRITE_COUNT | READ_SKIP_COUNT | WRITE_SKIP_COUNT | PROCESS_SKIP_COUNT | ROLLBACK_COUNT | EXIT_CODE | EXIT_MESSAGE | LAST_UPDATED               |
+-------------------+---------+-----------+------------------+----------------------------+----------------------------+-----------+--------------+------------+--------------+-------------+-----------------+------------------+--------------------+----------------+-----------+--------------+----------------------------+
|                 1 |     103 | chunkStep |                1 | 2021-08-31 22:04:24.244000 | 2021-08-31 22:04:25.948000 | COMPLETED |          101 |       1000 |            0 |        1000 |               0 |                0 |                  0 |              0 | COMPLETED |              | 2021-08-31 22:04:25.950000 |
+-------------------+---------+-----------+------------------+----------------------------+----------------------------+-----------+--------------+------------+--------------+-------------+-----------------+------------------+--------------------+----------------+-----------+--------------+----------------------------+
1 row in set (0.00 sec)
```  

**org.springframework.batch.repeat.CompletionPolicy** 이해하기  

- `RepeatContext start(RepeatContext parent)`: 청크 시작 시 호출되며 내부 상태를 초기화 해야한다.
- `void update(RepeatContext context)`: 각 아이템이 처리 시 호출되며 내부 상태를 갱신한다.
- `boolean isComplete(RepeatContext context, RepeatStatus result)`, `boolean isComplete(RepeatContext context)`: 청크 완료 여부의 상태를 기반으로 결정 로직을 수행한다.  

<details>
<summary>Specification</summary>  

```java
```
</details>  
/**
* Interface for batch completion policies, to enable batch operations to
* strategise normal completion conditions. Stateful implementations of batch
* iterators should <em>only</em> update state using the update method. If you
* need custom behaviour consider extending an existing implementation or using
* the composite provided.
*
* @author Dave Syer
*
*/
public interface CompletionPolicy {

	/**
	 * Determine whether a batch is complete given the latest result from the
	 * callback. If this method returns true then
	 * {@link #isComplete(RepeatContext)} should also (but not necessarily vice
	 * versa, since the answer here depends on the result).
	 * 
	 * @param context the current batch context.
	 * @param result the result of the latest batch item processing.
	 * 
	 * @return true if the batch should terminate.
	 * 
	 * @see #isComplete(RepeatContext)
	 */
	boolean isComplete(RepeatContext context, RepeatStatus result);

	/**
	 * Allow policy to signal completion according to internal state, without
	 * having to wait for the callback to complete.
	 * 
	 * @param context the current batch context.
	 * 
	 * @return true if the batch should terminate.
	 */
	boolean isComplete(RepeatContext context);

	/**
	 * Create a new context for the execution of a batch. N.B. implementations
	 * should <em>not</em> return the parent from this method - they must
	 * create a new context to meet the specific needs of the policy. The best
	 * way to do this might be to override an existing implementation and use
	 * the {@link RepeatContext} to store state in its attributes.
	 * 
	 * @param parent the current context if one is already in progress.
	 * @return a context object that can be used by the implementation to store
	 * internal state for a batch.
	 */
	RepeatContext start(RepeatContext parent);

	/**
	 * Give implementations the opportunity to update the state of the current
	 * batch. Will be called <em>once</em> per callback, after it has been
	 * launched, but not necessarily after it completes (if the batch is
	 * asynchronous).
	 * 
	 * @param context the value returned by start.
	 */
	void update(RepeatContext context);

}

**org.springframework.batch.repeat.CompletionPolicy** 구현하기  

```java
@Slf4j
public static class RandomChunkSizePolicy implements CompletionPolicy {

    private int chunkSize;
    private int totalProcessed;
    private Random random = new Random();

    @Override
    public boolean isComplete(RepeatContext context, RepeatStatus result) {
        if (RepeatStatus.FINISHED == result) {
            return true;
        }
        return isComplete(context);
    }

    @Override
    public boolean isComplete(RepeatContext context) {
        return totalProcessed >= chunkSize;
    }

    @Override
    public RepeatContext start(RepeatContext parent) {
        chunkSize = random.nextInt(20);
        totalProcessed = 0;
        logger.info("The chunk size has been set to {}", chunkSize);
        return parent;
    }

    @Override
    public void update(RepeatContext context) {
        this.totalProcessed++;
    }
}

// java -jar -Dspring.profiles.active=step-example-chunk-policy ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar
```  

### 스탭 리스너



---

java -jar -Dspring.profiles.active=step-example-chunk ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar

__잡 소개하기
____잡의 생명주기 따라 가보기
__잡 구성하기
____잡의 기본 구성
____잡 파라미터
____잡 리스너 적용하기
____ExecutionContext
____ExecutionContext 조작하기
__스텝 알아보기
____태스크릿 처리와 청크 처리 비교
____스텝 구성
____그 밖의 여러 다른 유형의 태스크릿 이해하기
____스텝 플로우
__요약