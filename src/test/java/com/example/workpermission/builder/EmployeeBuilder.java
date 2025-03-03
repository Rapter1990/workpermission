package com.example.workpermission.builder;

import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.auth.model.enums.UserStatus;
import com.example.workpermission.auth.model.enums.UserType;

import java.util.UUID;

public class EmployeeBuilder extends BaseBuilder<UserEntity> {

    public EmployeeBuilder() {
        super(UserEntity.class);
    }

    public EmployeeBuilder withValidFields() {
        return this
                .withId(UUID.randomUUID().toString())
                .withEmail("userexample@example.com")
                .withPassword("userpassword")
                .withFirstName("User FirstName")
                .withLastName("User LastName")
                .withPhoneNumber("1234567890")
                .withUserType(UserType.EMPLOYEE)
                .withUserStatus(UserStatus.ACTIVE);
    }

    public EmployeeBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public EmployeeBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public EmployeeBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public EmployeeBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public EmployeeBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public EmployeeBuilder withPhoneNumber(String phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public EmployeeBuilder withUserType(UserType userType) {
        data.setUserType(userType);
        return this;
    }

    public EmployeeBuilder withUserStatus(UserStatus userStatus) {
        data.setUserStatus(userStatus);
        return this;
    }

}
