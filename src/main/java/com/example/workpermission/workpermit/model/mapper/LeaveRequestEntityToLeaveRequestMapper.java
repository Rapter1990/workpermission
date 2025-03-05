package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.auth.model.mapper.UserEntityToUserMapper;
import com.example.workpermission.common.model.mapper.BaseMapper;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.entity.LeaveRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LeaveRequestEntityToLeaveRequestMapper extends BaseMapper<LeaveRequestEntity, LeaveRequest> {

    @Mapping(target = "user", source = "user")
    LeaveRequest map(LeaveRequestEntity entity);

    static LeaveRequestEntityToLeaveRequestMapper initialize() {
        return Mappers.getMapper(LeaveRequestEntityToLeaveRequestMapper.class);
    }

}
