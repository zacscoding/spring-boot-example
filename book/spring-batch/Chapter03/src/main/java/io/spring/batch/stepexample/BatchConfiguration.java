package io.spring.batch.stepexample;

import java.util.concurrent.Callable;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.core.step.tasklet.SimpleSystemProcessExitCodeMapper;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("step-example")
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomService customService;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("job")
                                     .start(step1())
                                     .incrementer(new RunIdIncrementer()) // 잡 파라미터 증가시키기
                                     .build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                                      .tasklet((contribution, chunkContext) -> {
                                          logger.info("Hello, World!");
                                          return RepeatStatus.FINISHED;
                                      })
                                      .tasklet(callableTaskletAdapter())
                                      .tasklet(methodInvokingTaskletAdapter())
                                      .tasklet(methodInvokingTaskletAdapter2(null))
                                      .build();
    }

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

    /**
     * 만약 TargetObject::Method 호출 결과가 {@link ExitStatus}로 캐스팅 가능한 {@link Object}를 반환하면
     * step의 exist status 값으로 사용한다(기본 값: {@link ExitStatus#COMPLETED}. 또한 Tasklet은 {@link RepeatStatus#FINISHED}를 반환한다.
     */
    @Bean
    public MethodInvokingTaskletAdapter methodInvokingTaskletAdapter() {
        final MethodInvokingTaskletAdapter methodInvokingTaskletAdapter = new MethodInvokingTaskletAdapter();

        methodInvokingTaskletAdapter.setTargetObject(customService);
        methodInvokingTaskletAdapter.setTargetMethod("service");

        return methodInvokingTaskletAdapter;
    }

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
}
