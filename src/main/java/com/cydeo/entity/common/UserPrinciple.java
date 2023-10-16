package com.cydeo.entity.common;

import com.cydeo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {

    private final User user;

    public UserPrinciple(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();  // GrantedAuthority what spring understand the roles always we use this.
        GrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole().getDescription()); //SimpleGrantedAuthority says give me the role
        authorityList.add(authority);
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.user.getPassWord();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {  //we dont have any field look like this. we must say true.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;     //we dont have any field look like this. we must say true.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;    //we dont have any field look like this. we must say true.
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public Long getId(){
        return this.user.getId();
    }
}
