package com.example.workpermission.workpermit.controller;

import com.example.workpermission.common.model.CustomPage;
import com.example.workpermission.common.model.dto.request.CustomPagingRequest;
import com.example.workpermission.common.model.dto.response.CustomPagingResponse;
import com.example.workpermission.common.model.dto.response.CustomResponse;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.request.CreateLeaveRequest;
import com.example.workpermission.workpermit.model.dto.request.LeaveRequestPagingRequest;
import com.example.workpermission.workpermit.model.dto.response.LeaveRequestResponse;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import com.example.workpermission.workpermit.model.mapper.CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper;
import com.example.workpermission.workpermit.model.mapper.LeaveRequestToLeaveRequestResponseMapper;
import com.example.workpermission.workpermit.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.hibernate.validator.constraints.UUID;

@RestController
@RequestMapping("/api/v1/leaverequests")
@RequiredArgsConstructor
@Validated
@Tag(name = "Leave Requests", description = "Handles operations for user leave requests.")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final LeaveRequestToLeaveRequestResponseMapper leaveRequestToLeaveRequestResponseMapper =
            LeaveRequestToLeaveRequestResponseMapper.initialize();

    private final CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper =
            CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.initialize();

    /**
     * Creates a new leave request.
     *
     * @param request the leave request details
     * @return the created leave request in response format
     */
    @PreAuthorize("hasAnyAuthority('MANAGER','EMPLOYEE')")
    @PostMapping
    public CustomResponse<LeaveRequestResponse> createLeaveRequest(@RequestBody final CreateLeaveRequest request) {
        final LeaveRequest leaveRequest = leaveRequestService.createLeaveRequest(request);
        final LeaveRequestResponse response = leaveRequestToLeaveRequestResponseMapper.map(leaveRequest);
        return CustomResponse.successOf(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','EMPLOYEE')")
    public CustomResponse<LeaveRequestResponse> getLeaveRequestById(@PathVariable @Valid @UUID final String id){
        final LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(id);

        final LeaveRequestResponse response = leaveRequestToLeaveRequestResponseMapper.map(leaveRequest);

        return CustomResponse.successOf(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','EMPLOYEE')")
    @GetMapping("/users/{userId}")
    public CustomResponse<CustomPagingResponse<LeaveRequestResponse>> getLeaveRequestsByUser(
            @PathVariable final String userId,
            @RequestBody @Valid final LeaveRequestPagingRequest leaveRequestPagingRequest) {

        CustomPage<LeaveRequest> customPage = leaveRequestService.getLeaveRequestsByUser(userId, leaveRequestPagingRequest);

        CustomPagingResponse<LeaveRequestResponse> pagingResponse =
                customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.toPagingResponse(customPage);

        return CustomResponse.successOf(pagingResponse);

    }

    /**
     * Updates the status of an existing leave request.
     *
     * @param leaveRequestId the ID of the leave request to update
     * @param status         the new leave status
     * @return the updated leave request in response format
     */
    @PreAuthorize("hasAuthority('MANAGER')")
    @PatchMapping("/{leaveRequestId}/status")
    public CustomResponse<LeaveRequestResponse> updateLeaveStatus(
            @PathVariable final String leaveRequestId,
            @RequestParam final LeaveStatus status) {

        final LeaveRequest updated = leaveRequestService.updateLeaveStatus(leaveRequestId, status);

        final LeaveRequestResponse response = leaveRequestToLeaveRequestResponseMapper.map(updated);

        return CustomResponse.successOf(response);
    }

    /**
     * Retrieves the number of remaining leave days available for the specified employee.
     *
     * @param userId the unique identifier of the user
     * @return a {@link ResponseEntity} containing the number of remaining leave days
     */
    @PreAuthorize("hasAnyAuthority('MANAGER','EMPLOYEE')")
    @GetMapping("/remaining/{userId}")
    public CustomResponse<Long> getRemainingLeaveDays(@PathVariable @Valid @UUID final String userId) {
        final Long remainingDays = leaveRequestService.getRemainingLeaveDays(userId);

        return CustomResponse.successOf(remainingDays);
    }

}
