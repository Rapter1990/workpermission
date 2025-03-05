package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.entity.LeaveRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ListLeaveRequestEntityToListLeaveRequestMapper {

    LeaveRequestEntityToLeaveRequestMapper leaveRequestEntityToLeaveRequestMapper =
            Mappers.getMapper(LeaveRequestEntityToLeaveRequestMapper.class);

    default List<LeaveRequest> toLeaveRequestList(List<LeaveRequestEntity> leaveRequestEntityList) {

        if (leaveRequestEntityList == null) {
            return null;
        }

        return leaveRequestEntityList.stream()
                .map(leaveRequestEntityToLeaveRequestMapper::map)
                .collect(Collectors.toList());

    }

    static ListLeaveRequestEntityToListLeaveRequestMapper initialize() {
        return Mappers.getMapper(ListLeaveRequestEntityToListLeaveRequestMapper.class);
    }

}
