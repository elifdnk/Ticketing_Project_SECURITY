package com.cydeo.entity;

import com.cydeo.entity.common.UserPrinciple;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener { //This is listening mechanism


    @PrePersist
    private void onPrePersist(BaseEntity baseEntity){ //this method executed whenever create the user

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //we get all information who log in the system

        baseEntity.setInsertDateTime(LocalDateTime.now()); //listen your entity and set time and reflected to DB
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());//listen your entity and set time and reflected to DB
        if(authentication != null && !authentication.getName().equals("anonymousUser")){ //valid authentication and authentication name is not anonymousUser(valid user)
            Object principal = authentication.getPrincipal();
            baseEntity.setInsertUserId(((UserPrinciple) principal).getId()); //what is that user?our Id coming DB, UserPrinciple doesnt have any ıd because of this we add user principal ID field.
            baseEntity.setLastUpdateUserId( ((UserPrinciple) principal).getId());

        }
    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity){ //who is doing updating?

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if(authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.setLastUpdateUserId( ((UserPrinciple) principal).getId());
        }
    }


}
