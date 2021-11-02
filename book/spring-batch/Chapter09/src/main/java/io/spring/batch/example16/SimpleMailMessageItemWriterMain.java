package io.spring.batch.example16;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.mail.SimpleMailMessageItemWriter;
import org.springframework.batch.item.mail.builder.SimpleMailMessageItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import io.spring.batch.domain.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class SimpleMailMessageItemWriterMain {

    public static void main(String[] args) {
        args = new String[] {
                "customerFile=input/customer.csv"
        };
        SpringApplication.run(SimpleMailMessageItemWriterMain.class, args);
    }

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public SimpleMailMessageItemWriter emailItemWriter(MailSender mailSender) {
        return new SimpleMailMessageItemWriterBuilder()
                .mailSender(mailSender)
                .build();
    }

    @Bean
    public Step emailStep() {
        return stepBuilderFactory.get("emailStep")
                                 .<Customer, SimpleMailMessage>chunk(3)
                                 .reader(null)
                                 .processor((ItemProcessor<Customer, SimpleMailMessage>) customer -> {
                                     SimpleMailMessage mail = new SimpleMailMessage();
                                     mail.setFrom("prospringbatch@gmail.com");
                                     mail.setTo(customer.getEmail());
                                     mail.setSubject("Welcome!");
                                     mail.setText(String.format("Welcome %s %s", customer.getFirstName(),
                                                                customer.getLastName()));
                                     return mail;
                                 })
                                 .writer(emailItemWriter(null))
                                 .build();
    }
}
