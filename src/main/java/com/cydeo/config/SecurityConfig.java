package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;

    public SecurityConfig(SecurityService securityService) {
        this.securityService = securityService;
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//
//        List<UserDetails> userList = new ArrayList<>();
//
//        userList.add(new User("mike", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//                //this user is Spring Security's User. we can not empty. at least we must write username,password and role. now we assume we dont have any DB
//        userList.add(new User("ozzy", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//        return new InMemoryUserDetailsManager(userList);  //this is not something common(InMemoryUserDetailsManager) but we use now learning the structure.
//                                                               // these users ve saves in the memory now.
//    }
    //comment this because this is hardcoded add user. we will use in our data users.



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()  //whenever we run our security we need to authorize each page
//                .antMatchers("/user/**").hasRole("ADMIN") //whatever inside the user(our inside user controller) should be accessible by ADMIN
                .antMatchers("/user/**").hasAuthority("Admin")// admin only able to see related with the user pages
//                .antMatchers("/project/**").hasRole("MANAGER")// manager can acess to /project
                .antMatchers("/project/**").hasAuthority("Manager")//manager only able to see project related pages
//                .antMatchers("/task/employee/**").hasRole("EMPLOYEE") //employee role can access to task/employee
                .antMatchers("/task/employee/**").hasAuthority("Employee")
//                .antMatchers("/task/**").hasRole("MANAGER") //manager role can access to task
                .antMatchers("/task/**").hasAuthority("Manager")
              //  .antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN") //more than one role for teh pages
              //  .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE") //if we are using hasAuthority we must write this way. Spring accepts this way.(we use this manual creation)
                .antMatchers(         //some pages everyone should able to see
                        "/",
                        "/login",              //we put this 5pages because we want everyone can see this pages.
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                ).permitAll()  //make it available for everyone.
                .anyRequest().authenticated()//any other request must be authenticated
                .and()
              //  .httpBasic()  //whenever we open default,spring give us sign in page.  (one pop up box)
                .formLogin() //I am gonna login with form which is mine
                .loginPage("/login") // our login page
                .defaultSuccessUrl("/welcome")//whenever we successfully login, where we are gonna landed? now we want to land welcome page.
                .failureUrl("/login?error=true")//whenever our credentials wrong it says this.
                .permitAll()// login page (now everyone go to login page)  it needs to be accessible for all
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  //can you give me where is this logout link in the UI?
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                .tokenValiditySeconds(120) //how long you want to activate yourself?
                .key("cydeo") //based on key-value (remember value is kkeeping in this key. it can be any name.)
                .userDetailsService(securityService) //remember who. who information coming from security service.
                .and().build();

    }





}
