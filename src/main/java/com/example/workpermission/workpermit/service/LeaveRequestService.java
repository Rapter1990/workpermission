package com.example.workpermission.workpermit.service;

import com.example.workpermission.common.model.CustomPage;
import com.example.workpermission.common.model.dto.request.CustomPagingRequest;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.request.CreateLeaveRequest;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;

public interface LeaveRequestService {

    LeaveRequest createLeaveRequest(CreateLeaveRequest request);

    LeaveRequest updateLeaveStatus(String leaveRequestId, LeaveStatus newStatus);

    Long getRemainingLeaveDays(String userId);

    LeaveRequest getLeaveRequestById(String id);

    CustomPage<LeaveRequest> getLeaveRequestsByUser(String userId, CustomPagingRequest customPagingRequest);

}
