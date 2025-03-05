package com.example.workpermission.workpermit.model;

import com.example.workpermission.auth.model.User;
import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.common.model.BaseDomainModel;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LeaveRequest extends BaseDomainModel {

    private String id;
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;

}
