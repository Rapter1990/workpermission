package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.common.model.mapper.BaseMapper;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.response.LeaveRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LeaveRequestToLeaveRequestResponseMapper extends BaseMapper<LeaveRequest, LeaveRequestResponse> {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", expression = "java(leaveRequest.getUser().getFirstName() + \" \" + leaveRequest.getUser().getLastName())")
    LeaveRequestResponse map(LeaveRequest leaveRequest);

    static LeaveRequestToLeaveRequestResponseMapper initialize() {
        return Mappers.getMapper(LeaveRequestToLeaveRequestResponseMapper.class);
    }
}
