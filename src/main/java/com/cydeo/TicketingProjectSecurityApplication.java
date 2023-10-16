package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication  //this includes @Configuration
public class TicketingProjectSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectSecurityApplication.class, args);
    }

    // Write a method which return the obj that you trying to add in the container
    //                 annotate this method with @Bean
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }  //if we want to encode any password we can use this. ıt came with spring security


}
