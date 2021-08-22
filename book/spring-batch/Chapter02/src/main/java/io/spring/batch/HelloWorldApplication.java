package io.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class HelloWorldApplication {

    /** 잡을 생성하는 빌더 **/
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** 스텝을 생성하는 빌더 **/
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step() {
        return this.stepBuilderFactory.get("step1")
                                      .tasklet(new Tasklet() {
                                          @Override
                                          public RepeatStatus execute(StepContribution stepContribution,
                                                                      ChunkContext chunkContext) throws Exception {
                                              logger.info("Hello, World");
                                              return RepeatStatus.FINISHED;
                                          }
                                      }).build();
    }

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("job")
                                     .start(step())
                                     .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

    // Output
    //2021-08-22 21:46:53.133  INFO 29649 --- [           main] o.s.b.c.r.s.JobRepositoryFactoryBean     : No database type set, using meta data indicating: H2
    //2021-08-22 21:46:53.211  INFO 29649 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
    //2021-08-22 21:46:53.272  INFO 29649 --- [           main] io.spring.batch.HelloWorldApplication    : Started HelloWorldApplication in 1.205 seconds (JVM running for 1.608)
    //2021-08-22 21:46:53.274  INFO 29649 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
    //2021-08-22 21:46:53.319  INFO 29649 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job]] launched with the following parameters: [{}]
    //2021-08-22 21:46:53.342  INFO 29649 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
    //2021-08-22 21:46:53.348  INFO 29649 --- [           main] io.spring.batch.HelloWorldApplication    : Hello, World
    //2021-08-22 21:46:53.352  INFO 29649 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 10ms
    //2021-08-22 21:46:53.355  INFO 29649 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 22ms
    //2021-08-22 21:46:53.359  INFO 29649 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
    //2021-08-22 21:46:53.360  INFO 29649 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
}
