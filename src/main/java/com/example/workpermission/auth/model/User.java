package com.example.workpermission.auth.model;

import com.example.workpermission.auth.model.enums.UserStatus;
import com.example.workpermission.auth.model.enums.UserType;
import com.example.workpermission.common.model.BaseDomainModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDomainModel {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserType userType;
    private UserStatus userStatus;

}
