package com.example.workpermission.builder;

import com.example.workpermission.auth.model.dto.request.RegisterRequest;
import com.example.workpermission.auth.model.enums.UserType;

import java.time.LocalDate;

public class RegisterRequestBuilder extends BaseBuilder<RegisterRequest> {

    public RegisterRequestBuilder() {
        super(RegisterRequest.class);
    }

    /**
     * Pre-populates the builder with valid Manager fields.
     */
    public RegisterRequestBuilder withManagerValidFields() {
        return this
                .withEmail("manager@example.com")
                .withPassword("managerpassword")
                .withFirstName("ManagerFirstName")
                .withLastName("ManagerLastName")
                .withPhoneNumber("10987654321")
                .withUserType(UserType.MANAGER);
    }

    /**
     * Pre-populates the builder with valid Employee fields.
     */
    public RegisterRequestBuilder withEmployeeValidFields() {
        return this
                .withEmail("employee@example.com")
                .withPassword("employeepassword")
                .withFirstName("EmployeeFirstName")
                .withLastName("EmployeeLastName")
                .withPhoneNumber("12345678910")
                .withUserType(UserType.EMPLOYEE);
    }

    public RegisterRequestBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public RegisterRequestBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public RegisterRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public RegisterRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public RegisterRequestBuilder withPhoneNumber(String phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public RegisterRequestBuilder withUserType(UserType userType) {
        data.setUserType(userType);
        return this;
    }

    public RegisterRequestBuilder withJoiningDate(LocalDate joiningDate) {
        data.setJoiningDate(joiningDate);
        return this;
    }

}
