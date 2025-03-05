package com.example.workpermission.workpermit.controller;

import com.example.workpermission.auth.model.User;
import com.example.workpermission.base.AbstractRestControllerTest;
import com.example.workpermission.builder.CreateLeaveRequestBuilder;
import com.example.workpermission.common.model.CustomPage;
import com.example.workpermission.common.model.CustomPaging;
import com.example.workpermission.common.model.dto.response.CustomPagingResponse;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.request.CreateLeaveRequest;
import com.example.workpermission.workpermit.model.dto.request.LeaveRequestPagingRequest;
import com.example.workpermission.workpermit.model.dto.response.LeaveRequestResponse;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import com.example.workpermission.workpermit.model.mapper.CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper;
import com.example.workpermission.workpermit.model.mapper.LeaveRequestToLeaveRequestResponseMapper;
import com.example.workpermission.workpermit.service.LeaveRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LeaveRequestControllerTest extends AbstractRestControllerTest {

    @MockitoBean
    LeaveRequestService leaveRequestService;

    private final LeaveRequestToLeaveRequestResponseMapper leaveRequestToLeaveRequestResponseMapper =
            LeaveRequestToLeaveRequestResponseMapper.initialize();

    private final CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper =
            CustomPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.initialize();

    @Test
    void testCreateLeaveRequestForManager() throws Exception {

        // Given
        final CreateLeaveRequest createRequest = new CreateLeaveRequestBuilder()
                .withValidFields();

        final LeaveRequest sampleLeaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("Manager First Name")
                        .lastName("Manager Last Name")
                        .build())
                .startDate(createRequest.getStartDate())
                .endDate(createRequest.getEndDate())
                .status(LeaveStatus.PENDING)
                .build();

        final LeaveRequestResponse expected = leaveRequestToLeaveRequestResponseMapper.map(sampleLeaveRequest);

        // When
        when(leaveRequestService.createLeaveRequest(any(CreateLeaveRequest.class)))
                .thenReturn(sampleLeaveRequest);

        // Then
        mockMvc.perform(post("/api/v1/leaverequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + mockManagerToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(expected.getId()))
                .andExpect(jsonPath("$.response.userId").value(expected.getUserId()))
                .andExpect(jsonPath("$.response.userFullName").value(expected.getUserFullName()))
                .andExpect(jsonPath("$.response.startDate").value(String.valueOf(expected.getStartDate())))
                .andExpect(jsonPath("$.response.endDate").value(String.valueOf(expected.getEndDate())))
                .andExpect(jsonPath("$.response.status").value(expected.getStatus().toString()));

        // Verify
        verify(leaveRequestService, times(1)).createLeaveRequest(any(CreateLeaveRequest.class));

    }

    @Test
    void testCreateLeaveRequestForEmployee() throws Exception {

        // Given
        final CreateLeaveRequest createRequest = new CreateLeaveRequestBuilder()
                .withValidFields();

        final LeaveRequest sampleLeaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("Employee First Name")
                        .lastName("Employee Last Name")
                        .build())
                .startDate(createRequest.getStartDate())
                .endDate(createRequest.getEndDate())
                .status(LeaveStatus.PENDING)
                .build();

        final LeaveRequestResponse expected = leaveRequestToLeaveRequestResponseMapper.map(sampleLeaveRequest);

        // When
        when(leaveRequestService.createLeaveRequest(any(CreateLeaveRequest.class)))
                .thenReturn(sampleLeaveRequest);

        // Then
        mockMvc.perform(post("/api/v1/leaverequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + mockEmployeeToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(expected.getId()))
                .andExpect(jsonPath("$.response.userId").value(expected.getUserId()))
                .andExpect(jsonPath("$.response.userFullName").value(expected.getUserFullName()))
                .andExpect(jsonPath("$.response.startDate").value(String.valueOf(expected.getStartDate())))
                .andExpect(jsonPath("$.response.endDate").value(String.valueOf(expected.getEndDate())))
                .andExpect(jsonPath("$.response.status").value(expected.getStatus().toString()));

        // Verify
        verify(leaveRequestService, times(1)).createLeaveRequest(any(CreateLeaveRequest.class));

    }

    @Test
    void testGetLeaveRequestByIdForManager() throws Exception {

        // Given
        final String leaveRequestId = UUID.randomUUID().toString();

        final LeaveRequest sampleLeaveRequest = LeaveRequest.builder()
                .id(leaveRequestId)
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("Manager First Name")
                        .lastName("Manager Last Name")
                        .build())
                .startDate(LocalDate.of(2023, 10, 1))
                .endDate(LocalDate.of(2023, 10, 5))
                .status(LeaveStatus.APPROVED)
                .build();

        final LeaveRequestResponse expected = leaveRequestToLeaveRequestResponseMapper.map(sampleLeaveRequest);

        // When
        when(leaveRequestService.getLeaveRequestById(leaveRequestId))
                .thenReturn(sampleLeaveRequest);

        // Then
        mockMvc.perform(get("/api/v1/leaverequests/{id}", leaveRequestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockManagerToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(expected.getId()))
                .andExpect(jsonPath("$.response.userId").value(expected.getUserId()))
                .andExpect(jsonPath("$.response.userFullName").value(expected.getUserFullName()))
                .andExpect(jsonPath("$.response.startDate").value(String.valueOf(expected.getStartDate())))
                .andExpect(jsonPath("$.response.endDate").value(String.valueOf(expected.getEndDate())))
                .andExpect(jsonPath("$.response.status").value(expected.getStatus().toString()));

        // Verify
        verify(leaveRequestService, times(1)).getLeaveRequestById(leaveRequestId);

    }

    @Test
    void testGetLeaveRequestByIdForEmployee() throws Exception {

        // Given
        final String leaveRequestId = UUID.randomUUID().toString();

        final LeaveRequest sampleLeaveRequest = LeaveRequest.builder()
                .id(leaveRequestId)
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("Employee First Name")
                        .lastName("Employee Last Name")
                        .build())
                .startDate(LocalDate.of(2023, 10, 10))
                .endDate(LocalDate.of(2023, 10, 15))
                .status(LeaveStatus.PENDING)
                .build();

        final LeaveRequestResponse expected = leaveRequestToLeaveRequestResponseMapper.map(sampleLeaveRequest);

        // When
        when(leaveRequestService.getLeaveRequestById(leaveRequestId))
                .thenReturn(sampleLeaveRequest);

        // Then
        mockMvc.perform(get("/api/v1/leaverequests/{id}", leaveRequestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockEmployeeToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(expected.getId()))
                .andExpect(jsonPath("$.response.userId").value(expected.getUserId()))
                .andExpect(jsonPath("$.response.userFullName").value(expected.getUserFullName()))
                .andExpect(jsonPath("$.response.startDate").value(String.valueOf(expected.getStartDate())))
                .andExpect(jsonPath("$.response.endDate").value(String.valueOf(expected.getEndDate())))
                .andExpect(jsonPath("$.response.status").value(expected.getStatus().toString()));

        // Verify
        verify(leaveRequestService, times(1)).getLeaveRequestById(leaveRequestId);

    }

    @Test
    void testGetLeaveRequestsByUserForManager() throws Exception {

        // Given
        final String userId = UUID.randomUUID().toString();

        final LeaveRequestPagingRequest pagingRequest = LeaveRequestPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageNumber(1)  // raw value; effective page will be 0 if subtracted internally
                                .pageSize(10)
                                .build()
                ).build();

        final LeaveRequest sampleLeaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(userId)
                        .firstName("Manager First Name")
                        .lastName("Manager Last Name")
                        .build())
                .startDate(LocalDate.of(2023, 10, 1))
                .endDate(LocalDate.of(2023, 10, 5))
                .status(LeaveStatus.APPROVED)
                .build();

        final List<LeaveRequest> leaveRequests = List.of(sampleLeaveRequest);

        final Page<LeaveRequest> leaveRequestPage =
                new PageImpl<>(leaveRequests, PageRequest.of(1, 10), leaveRequests.size());

        final CustomPage<LeaveRequest> customPage = CustomPage.of(leaveRequests, leaveRequestPage);

        final CustomPagingResponse<LeaveRequestResponse> expectedResponse =
                customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.toPagingResponse(customPage);

        // When
        when(leaveRequestService.getLeaveRequestsByUser(eq(userId), any(LeaveRequestPagingRequest.class)))
                .thenReturn(customPage);

        // Then
        mockMvc.perform(get("/api/v1/leaverequests/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockManagerToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.content", hasSize(expectedResponse.getContent().size())))
                .andExpect(jsonPath("$.response.totalElementCount").value(expectedResponse.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expectedResponse.getTotalPageCount()))
                .andExpect(jsonPath("$.response.pageNumber").value(expectedResponse.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expectedResponse.getPageSize()));

        // Verify
        verify(leaveRequestService, times(1))
                .getLeaveRequestsByUser(eq(userId), any(LeaveRequestPagingRequest.class));

    }

    @Test
    void testGetLeaveRequestsByUserForEmployee() throws Exception {

        // Given
        final String userId = UUID.randomUUID().toString();

        final LeaveRequestPagingRequest pagingRequest = LeaveRequestPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageNumber(2)  // raw value; effective page will be (2 - 1) if subtracted internally
                                .pageSize(5)
                                .build()
                ).build();

        final LeaveRequest sampleLeaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(userId)
                        .firstName("Employee First Name")
                        .lastName("Employee Last Name")
                        .build())
                .startDate(LocalDate.of(2023, 11, 1))
                .endDate(LocalDate.of(2023, 11, 3))
                .status(LeaveStatus.PENDING)
                .build();

        final List<LeaveRequest> leaveRequests = List.of(sampleLeaveRequest);

        final Page<LeaveRequest> leaveRequestPage =
                new PageImpl<>(leaveRequests, PageRequest.of(2, 5), leaveRequests.size());

        final CustomPage<LeaveRequest> customPage = CustomPage.of(leaveRequests, leaveRequestPage);

        final CustomPagingResponse<LeaveRequestResponse> expectedResponse =
                customPageLeaveRequestToCustomPagingLeaveRequestResponseMapper.toPagingResponse(customPage);

        // When
        when(leaveRequestService.getLeaveRequestsByUser(eq(userId), any(LeaveRequestPagingRequest.class)))
                .thenReturn(customPage);

        // Then
        mockMvc.perform(get("/api/v1/leaverequests/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockEmployeeToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.content", hasSize(expectedResponse.getContent().size())))
                .andExpect(jsonPath("$.response.totalElementCount").value(expectedResponse.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expectedResponse.getTotalPageCount()))
                .andExpect(jsonPath("$.response.pageNumber").value(expectedResponse.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expectedResponse.getPageSize()));

        // Verify
        verify(leaveRequestService, times(1))
                .getLeaveRequestsByUser(eq(userId), any(LeaveRequestPagingRequest.class));

    }

    @Test
    void testUpdateLeaveStatusForManager() throws Exception {

        // Given
        final String leaveRequestId = UUID.randomUUID().toString();
        final LeaveStatus newStatus = LeaveStatus.APPROVED;

        final LeaveRequest updatedLeaveRequest = LeaveRequest.builder()
                .id(leaveRequestId)
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("Manager First Name")
                        .lastName("Manager Last Name")
                        .build())
                .startDate(LocalDate.of(2023, 10, 1))
                .endDate(LocalDate.of(2023, 10, 5))
                .status(newStatus)
                .build();

        final LeaveRequestResponse expectedResponse = leaveRequestToLeaveRequestResponseMapper.map(updatedLeaveRequest);

        // When
        when(leaveRequestService.updateLeaveStatus(leaveRequestId, newStatus))
                .thenReturn(updatedLeaveRequest);

        // Then
        mockMvc.perform(patch("/api/v1/leaverequests/{leaveRequestId}/status", leaveRequestId)
                        .param("status", newStatus.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockManagerToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.response.userId").value(expectedResponse.getUserId()))
                .andExpect(jsonPath("$.response.userFullName").value(expectedResponse.getUserFullName()))
                .andExpect(jsonPath("$.response.startDate").value(String.valueOf(expectedResponse.getStartDate())))
                .andExpect(jsonPath("$.response.endDate").value(String.valueOf(expectedResponse.getEndDate())))
                .andExpect(jsonPath("$.response.status").value(expectedResponse.getStatus().toString()));

        // Verify
        verify(leaveRequestService, times(1)).updateLeaveStatus(leaveRequestId, newStatus);

    }

    @Test
    void testUpdateLeaveStatusForEmployee() throws Exception {

        // Given
        final String leaveRequestId = UUID.randomUUID().toString();
        final LeaveStatus newStatus = LeaveStatus.APPROVED;

        // When & Then: Employee should be forbidden from accessing this endpoint
        mockMvc.perform(patch("/api/v1/leaverequests/{leaveRequestId}/status", leaveRequestId)
                        .param("status", newStatus.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockEmployeeToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Verify
        verify(leaveRequestService, times(0)).updateLeaveStatus(anyString(), any());

    }

    @Test
    void testGetRemainingLeaveDaysForManager() throws Exception {

        // Given
        final String userId = UUID.randomUUID().toString();
        final Long remainingDays = 12L;

        // When
        when(leaveRequestService.getRemainingLeaveDays(userId)).thenReturn(remainingDays);

        // Then
        mockMvc.perform(get("/api/v1/leaverequests/remaining/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockManagerToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response").value(remainingDays));

        // Verify
        verify(leaveRequestService, times(1)).getRemainingLeaveDays(userId);

    }

    @Test
    void testGetRemainingLeaveDaysForEmployee() throws Exception {

        // Given
        final String userId = UUID.randomUUID().toString();
        final Long remainingDays = 8L;

        // When
        when(leaveRequestService.getRemainingLeaveDays(userId)).thenReturn(remainingDays);

        // Then
        mockMvc.perform(get("/api/v1/leaverequests/remaining/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockEmployeeToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response").value(remainingDays));

        // Verify
        verify(leaveRequestService, times(1)).getRemainingLeaveDays(userId);

    }


}