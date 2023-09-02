package com.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.csrf().disable(); // csrf 토큰 비활성화

        http.authorizeHttpRequests()
                .antMatchers("/list/**").authenticated() // 이 주소로 시작되면 인증 필요
                .anyRequest().permitAll() // 그게 아닌 모든 주소는 인증 필요 X
                .and()
                .formLogin()
                .loginPage("/login") // 인증 필요한 주소로 접속하면 이 주소로 이동
                .loginProcessingUrl("/login") // 스프링 시큐리티가 로그인 자동 진행하면 POST 방식으로 진행
                .defaultSuccessUrl("/list"); // 로그인이 정상적이면 이동

    }
}
