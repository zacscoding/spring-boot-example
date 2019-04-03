package org.batch.config;

import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.batch.domain.Person;
import org.batch.process.JobCompletionNotificationListener;
import org.batch.process.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("console")
@Configuration
@EnableBatchProcessing
public class BatchSimpleConfiguration {

    final AtomicLong readCounter = new AtomicLong(0);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<Person> randomPersonReader() {
        return new ItemReader<Person>() {
            @Override
            public Person read()
                throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

                Person result = null;
                long readCount = readCounter.getAndIncrement();
                if (readCount % 2 == 0) {
                    result = null;
                } else {
                    String uuid = UUID.randomUUID().toString();
                    StringTokenizer tokenizer = new StringTokenizer(uuid, "-");
                    return new Person(tokenizer.nextToken(), tokenizer.nextToken());
                }

                logger.info("[READER] read person : {}", result);
                return result;
            }
        };
    }

    @Bean
    public ItemWriter<Person> consoleOutputWriter() {
        return new ItemWriter<Person>() {
            @Override
            public void write(List<? extends Person> list) throws Exception {
                System.out.println("[WRITER] persons : " + list.size());
                for (Person person : list) {
                    System.out.println("[WRITER] : " + person.toString());
                }
            }
        };
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    // == tag :: jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
            .flow(step1)
            .end().build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<Person, Person>chunk(1)
            .reader(randomPersonReader())
            .processor(processor())
            //.writer(consoleOutputWriter())
            .build();
    }
    // == end :: job step[]


}
