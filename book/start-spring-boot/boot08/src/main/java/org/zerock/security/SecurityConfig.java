package org.zerock.security;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


import javax.sql.DataSource;

/**
 * @author zacconding
 * @Date 2017-12-24
 * @GitHub : https://github.com/zacscoding
 */
@Log
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // Method Secured
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Autowired
    ZerockUsersService zerockUsersService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("# security config..");
        http.authorizeRequests().antMatchers("/guest/**").permitAll().antMatchers("/manager/**").hasRole("MANAGER").antMatchers("/admin/**").hasRole("ADMIN")
            .and().formLogin().loginPage("/login").and().exceptionHandling().accessDeniedPage("/accessDenied").and().logout().logoutUrl("/logout")
            .invalidateHttpSession(true).and().rememberMe().key("zerock").userDetailsService(zerockUsersService).tokenRepository(getJDBCRepository())
            .tokenValiditySeconds(60 * 60 * 24);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(zerockUsersService).passwordEncoder(passwordEncoder());
    }


    private PersistentTokenRepository getJDBCRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }


    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("## build auth global");

        String query1 = "SELECT uid username, CONCAT('{noop}',upw) password, true enabled FROM tbl_members where uid = ?";
        String query2 = "SELECT member uid, role_name role FROM tbl_member_roles WHERE member = ?";

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(query1)
                .rolePrefix("ROLE_")
                .authoritiesByUsernameQuery(query2);
    }
    */

    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("## build auth global");

        auth.inMemoryAuthentication()
                .withUser("manager")
                .password("{noop}1111")
                .roles("MANAGER");
    }
    */
}
