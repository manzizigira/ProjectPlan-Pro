//package com.ProjectPro.ProjectPro.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        userDetailsManager.setUsersByUsernameQuery("SELECT email, password, TRUE FROM user WHERE email = ?");
//        userDetailsManager.setAuthoritiesByUsernameQuery(
//                "SELECT u.email, CONCAT('ROLE_', r.role_name) " +
//                        "FROM user u " +
//                        "JOIN user_role ur ON u.id = ur.user_id " +
//                        "JOIN role r ON ur.role_id = r.id " +
//                        "WHERE u.email = ?"
//        );
//        return userDetailsManager;
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/images/**", "/accessD.css", "/employeeStyle.css", "/loginStyle.css", "/directorateStyle.css", "/report.css").permitAll()
//                                .requestMatchers("/employee-dashboard").hasRole("EMPLOYEE")
//                                .requestMatchers("/employeePage").hasRole("SUPERVISOR")
//                                .requestMatchers("/objectivesPage").hasRole("SUPERVISOR")
//                                .requestMatchers("/priority-levels").hasRole("SUPERVISOR")
//                                .requestMatchers("/projectPage").hasRole("SUPERVISOR")
//                                .requestMatchers("/reports-page").hasRole("SUPERVISOR")
//                                .requestMatchers("/viewRolePage").hasRole("SUPERVISOR")
//                                .requestMatchers("/subObjectivePage").hasRole("SUPERVISOR")
//                                .requestMatchers("/task/taskPage").permitAll()
//                                .requestMatchers("/saveUser").permitAll()
//                                .requestMatchers("/updateTaskStatus").permitAll()
//                                .requestMatchers("/api/task-details/{taskId}").permitAll()
//                                .requestMatchers("/api/completed-tasks").permitAll()
//                                .requestMatchers("/api/in-progress-tasks").permitAll()
//                                .requestMatchers("/api/directorates").permitAll()
//                                .requestMatchers("/application/**").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/view")
//                                .loginProcessingUrl("/perform_login")
//                                .successHandler(customAuthSuccessHandler())
//                                .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/")
//                        .permitAll()
//
//                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling
//                                .accessDeniedPage("/accessDeniedPage")
//                );
//        return http.build();
//    }
//
//    public AuthenticationSuccessHandler customAuthSuccessHandler(){
//        return new CustomAuthenticationSuccessHandler();
//    }
//
//}
//
