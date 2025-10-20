package kr.co.greengarden.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers(h -> h.frameOptions(fo -> fo.sameOrigin())); // iframe same-origin 허용

        // 로그인 설정
        http.formLogin(form -> form
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                //.defaultSuccessUrl("/", true)
                .successHandler((req, res, auth) -> {
                    boolean isAdmin = auth.getAuthorities()
                            .stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    String ctx = req.getContextPath();
                    res.sendRedirect(ctx + (isAdmin ? "/admin/" : "/"));
                })
                .failureUrl("/member/login?error=true")
                .usernameParameter("memId")
                .passwordParameter("password")
        );

        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/member/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID",  "remember-me")
                .logoutSuccessUrl("/member/login?logout=true"));

        // 자동 로그인 설정
        http.rememberMe(remember -> remember
                .key("greengarden-remember-key")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .alwaysRemember(false)
        );

        // 인가 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/member/**", "/css/**", "/js/**", "/images/**", "/favicon.ico")
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        //http.csrf(CsrfConfigurer::disable);

        return http.build();
    }

    // password 암호화 방식
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

