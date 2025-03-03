package com.example.workpermission.builder;

import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.auth.model.enums.UserStatus;
import com.example.workpermission.auth.model.enums.UserType;

import java.util.UUID;

public class ManagerUserBuilder extends BaseBuilder<UserEntity> {


    public ManagerUserBuilder() {
        super(UserEntity.class);
    }

    public ManagerUserBuilder withValidFields() {
        return this
                .withId(UUID.randomUUID().toString())
                .withEmail("adminexample@example.com")
                .withPassword("adminpassword")
                .withFirstName("Admin First Name")
                .withLastName("Admin Last Name")
                .withPhoneNumber("1234567890")
                .withUserType(UserType.MANAGER)
                .withUserStatus(UserStatus.ACTIVE);
    }

    public ManagerUserBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public ManagerUserBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public ManagerUserBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public ManagerUserBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public ManagerUserBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public ManagerUserBuilder withPhoneNumber(String phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public ManagerUserBuilder withUserType(UserType userType) {
        data.setUserType(userType);
        return this;
    }

    public ManagerUserBuilder withUserStatus(UserStatus userStatus) {
        data.setUserStatus(userStatus);
        return this;
    }

}
