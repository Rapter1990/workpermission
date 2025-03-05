package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.common.model.CustomPage;
import com.example.workpermission.common.model.dto.response.CustomPagingResponse;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.response.LeaveRequestResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapperTest {

    private final CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper  customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper=
            CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.initialize();

    @Test
    void testToPagingResponse_WhenLeaveRequestPageIsNull() {
        // Test case where leaveRequestPage is null
        CustomPage<LeaveRequest> leaveRequestPage = null;

        CustomPagingResponse<LeaveRequestResponse> result = customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.toPagingResponse(leaveRequestPage);

        // Assert that the result is null when leaveRequestPage is null
        assertNull(result);
    }

    @Test
    void testToLeaveRequestResponseList_WhenLeaveRequestsIsNull() {
        // Test case where leaveRequests list is null
        List<LeaveRequest> leaveRequests = null;

        List<LeaveRequestResponse> result = customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.toLeaveRequestResponseList(leaveRequests);

        // Assert that the result is null when leaveRequests is null
        assertNull(result);
    }


}