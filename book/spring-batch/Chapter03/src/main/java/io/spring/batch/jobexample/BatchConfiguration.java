package io.spring.batch.jobexample;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("job-example")
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("basicJob")
                                     .start(step1())
                                     .validator(compositeJobParametersValidator())
                                     //.incrementer(new RunIdIncrementer()) // 잡 파라미터 증가시키기
                                     .incrementer(new DailyJobTimestamper())
                                     //.listener(new JobLoggerListener())
                                     .listener(JobListenerFactoryBean.getListener(new AnnotationJobLoggerListener()))
                                     .build();
    }

    @Bean
    public Step step1() {
//        return this.stepBuilderFactory.get("step1")
//                                      .tasklet((contribution, chunkContext) -> {
//                                          System.out.println("Hello, world!");
//                                          return RepeatStatus.FINISHED;
//                                      }).build();
        return this.stepBuilderFactory.get("step1")
                                      .tasklet(new HelloWorldTasklet())
                                      .build();
    }

    // ------------------------------------------------------------------
    // JobParameters 추출하기
    // ------------------------------------------------------------------

    /**
     * {@link ChunkContext}에서 JobParameter를 추출하는 {@link Tasklet}
     */
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

    /**
     * {@link StepScope}를 이용하여 늦은 바인딩(실제 실행전까지 빈 생성 지연) 후 JobParameters 중 'name' 파라미터를 사용하는 {@link Tasklet}
     */
    @StepScope
    @Bean
    public Tasklet helloWorldTaskletWithLateBind(
            @Value("#{jobParameters['name']") String name) {
        return (contribution, chunkContext) -> {
            logger.info("Hello, {}!", name);
            return RepeatStatus.FINISHED;
        };
    }

    // ------------------------------------------------------------------
    // JobParameters 유효성 검증하기
    // ------------------------------------------------------------------

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

    /**
     * {@link CompositeJobParametersValidator}를 이용하여 다중 {@link JobParametersValidator}를 추가한다.
     * 아래의 경우 {@link ParameterValidator} -> {@link DefaultJobParametersValidator} 순으로 유효성을 검증한다.
     */
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
}
