package com.example.workpermission.builder;

import com.example.workpermission.auth.model.dto.request.RegisterRequest;
import com.example.workpermission.auth.model.enums.UserType;

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
                .withPhoneNumber("0987654321")
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
                .withPhoneNumber("1234567890")
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
}
