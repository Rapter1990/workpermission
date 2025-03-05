package com.example.workpermission.builder;

import com.example.workpermission.workpermit.model.dto.request.CreateLeaveRequest;

import java.time.LocalDate;
import java.util.UUID;

public class CreateLeaveRequestBuilder extends BaseBuilder<CreateLeaveRequest> {

    public CreateLeaveRequestBuilder() {
        super(CreateLeaveRequest.class);
    }

    public CreateLeaveRequest withValidFields() {
        return this
                .withUserId(UUID.randomUUID().toString())
                .withStartDate(LocalDate.now().plusDays(1))
                .withEndDate(LocalDate.now().plusDays(5))
                .build();
    }

    public CreateLeaveRequestBuilder withUserId(String userId) {
        data.setUserId(userId);
        return this;
    }

    public CreateLeaveRequestBuilder withStartDate(LocalDate startDate) {
        data.setStartDate(startDate);
        return this;
    }

    public CreateLeaveRequestBuilder withEndDate(LocalDate endDate) {
        data.setEndDate(endDate);
        return this;
    }

}
