package com.cydeo.service.impl;

import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrinciple;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;


    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //get the user from DB and convert to our user spring understand by using UserPrinciple class

        User user = userRepository.findByUserNameAndIsDeleted(username, false); //this is our user

        if (user == null) {
            throw new UsernameNotFoundException(username);    //if user==null spring give us ready exception
        }


        return new UserPrinciple(user); //ger the user from DB and convert to user springs understand
    }
}
