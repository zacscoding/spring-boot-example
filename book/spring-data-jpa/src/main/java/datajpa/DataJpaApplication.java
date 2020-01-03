package datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import datajpa.repository.MemberRepository;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = { "datajpa" })
public class DataJpaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DataJpaApplication.class, args);
        MemberRepository bean = ctx.getBean(MemberRepository.class);
        System.out.println(bean.getClass().getName());

    }
}
