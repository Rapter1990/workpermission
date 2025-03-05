package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.common.model.CustomPage;
import com.example.workpermission.common.model.dto.response.CustomPagingResponse;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.response.LeaveRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper {

    LeaveRequestToLeaveRequestResponseMapper leaveRequestToLeaveRequestResponseMapper =
            Mappers.getMapper(LeaveRequestToLeaveRequestResponseMapper.class);

    default CustomPagingResponse<LeaveRequestResponse> toPagingResponse(CustomPage<LeaveRequest> leaveRequestPage) {

        if (leaveRequestPage == null) {
            return null;
        }

        return CustomPagingResponse.<LeaveRequestResponse>builder()
                .content(toLeaveRequestResponseList(leaveRequestPage.getContent()))
                .totalElementCount(leaveRequestPage.getTotalElementCount())
                .totalPageCount(leaveRequestPage.getTotalPageCount())
                .pageNumber(leaveRequestPage.getPageNumber())
                .pageSize(leaveRequestPage.getPageSize())
                .build();

    }

    default List<LeaveRequestResponse> toLeaveRequestResponseList(List<LeaveRequest> leaveRequests) {

        if (leaveRequests == null) {
            return null;
        }

        return leaveRequests.stream()
                .map(leaveRequestToLeaveRequestResponseMapper::map)
                .collect(Collectors.toList());

    }

    static CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper initialize() {
        return Mappers.getMapper(CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.class);
    }

}
