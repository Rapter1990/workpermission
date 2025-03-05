package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.auth.model.User;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.response.LeaveRequestResponse;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LeaveRequestToLeaveRequestResponseMapperImplTest {

    private final LeaveRequestToLeaveRequestResponseMapper leaveRequestToLeaveRequestResponseMapper =
            Mappers.getMapper(LeaveRequestToLeaveRequestResponseMapper.class);

    @Test
    void testMap_NullLeaveRequest_ReturnsNull() {
        LeaveRequest leaveRequest = null;
        LeaveRequestResponse response = leaveRequestToLeaveRequestResponseMapper.map(leaveRequest);
        assertNull(response, "Mapping null LeaveRequest should return null.");
    }

    @Test
    void testMap_NullCollection_ReturnsNull() {
        List<LeaveRequest> leaveRequests = null;
        List<LeaveRequestResponse> responses = leaveRequestToLeaveRequestResponseMapper.map(leaveRequests);
        assertNull(responses, "Mapping null collection should return null.");
    }

    @Test
    void testMap_ValidLeaveRequest() {

        // Given
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("User First Name")
                        .lastName("User Last Name")
                        .build())
                .startDate(LocalDate.of(2025, 4, 1))
                .endDate(LocalDate.of(2025, 4, 10))
                .status(LeaveStatus.APPROVED)
                .build();

        // Map to response object
        LeaveRequestResponse response = leaveRequestToLeaveRequestResponseMapper.map(leaveRequest);

        // Verify the mapping
        assertNotNull(response);
        assertEquals(leaveRequest.getId(), response.getId());
        assertEquals(leaveRequest.getUser().getId(), response.getUserId());
        assertEquals(leaveRequest.getUser().getFirstName() + " " + leaveRequest.getUser().getLastName(),
                response.getUserFullName());
        assertEquals(leaveRequest.getStartDate(), response.getStartDate());
        assertEquals(leaveRequest.getEndDate(), response.getEndDate());
        assertEquals(leaveRequest.getStatus(), response.getStatus());

    }

    @Test
    void testMap_CollectionOfLeaveRequests() {

        // Given
        LeaveRequest lr1 = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("User First Name")
                        .lastName("User Last Name")
                        .build())
                .startDate(LocalDate.of(2025, 5, 1))
                .endDate(LocalDate.of(2025, 5, 5))
                .status(LeaveStatus.PENDING)
                .build();

        LeaveRequest lr2 = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("User First Name")
                        .lastName("User Last Name")
                        .build())
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 3))
                .status(LeaveStatus.APPROVED)
                .build();

        List<LeaveRequest> leaveRequests = Arrays.asList(lr1, lr2);
        List<LeaveRequestResponse> responses = leaveRequestToLeaveRequestResponseMapper.map(leaveRequests);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        // Verify first mapped object
        LeaveRequestResponse response1 = responses.get(0);
        assertEquals(leaveRequests.get(0).getId(), response1.getId());
        assertEquals(leaveRequests.get(0).getUser().getId(), response1.getUserId());
        assertEquals(leaveRequests.get(0).getUser().getFirstName() + " " + leaveRequests.get(0).getUser().getLastName(),
                response1.getUserFullName());
        assertEquals(leaveRequests.get(0).getStartDate(), response1.getStartDate());
        assertEquals(leaveRequests.get(0).getEndDate(), response1.getEndDate());
        assertEquals(leaveRequests.get(0).getStatus(), response1.getStatus());

        // Verify second mapped object
        LeaveRequestResponse response2 = responses.get(1);
        assertEquals(leaveRequests.get(1).getId(), response2.getId());
        assertEquals(leaveRequests.get(1).getUser().getId(), response2.getUserId());
        assertEquals(leaveRequests.get(1).getUser().getFirstName() + " " + leaveRequests.get(1).getUser().getLastName(), response2.getUserFullName());
        assertEquals(leaveRequests.get(1).getStartDate(), response2.getStartDate());
        assertEquals(leaveRequests.get(1).getEndDate(), response2.getEndDate());
        assertEquals(leaveRequests.get(1).getStatus(), response2.getStatus());
    }

    @Test
    void testMap_NullUserInLeaveRequest_ThrowsNullPointerException() {

        // Given
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(null)
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 10))
                .status(LeaveStatus.PENDING)
                .build();

        // Expect a NullPointerException when mapping due to accessing user fields
        assertThrows(NullPointerException.class, () -> leaveRequestToLeaveRequestResponseMapper.map(leaveRequest),
                "Mapping a LeaveRequest with null user should throw NullPointerException.");

    }

    @Test
    void testLeaveRequestUserId_WithNullLeaveRequest() throws Exception {
        String result = invokeLeaveRequestUserId(null);
        assertNull(result, "Expected null result when LeaveRequest is null.");
    }

    @Test
    void testLeaveRequestUserId_WithNullUser() throws Exception {
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(null)
                .build();
        String result = invokeLeaveRequestUserId(leaveRequest);
        assertNull(result, "Expected null result when LeaveRequest user is null.");
    }

    @Test
    void testLeaveRequestUserId_WithNullUserId() throws Exception {

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(null)
                        .firstName("User First Name")
                        .lastName("User Last Name")
                        .build())
                .build();
        String result = invokeLeaveRequestUserId(leaveRequest);
        assertNull(result, "Expected null result when User id is null.");
    }

    @Test
    void testLeaveRequestUserId_WithValidUser() throws Exception {

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("User First Name")
                        .lastName("User Last Name")
                        .build())
                .build();

        String result = invokeLeaveRequestUserId(leaveRequest);
        assertEquals(leaveRequest.getUser().getId(), result, "Expected the valid user id to be returned.");
    }

    private String invokeLeaveRequestUserId(LeaveRequest leaveRequest) throws Exception {
        Method method = leaveRequestToLeaveRequestResponseMapper.getClass().getDeclaredMethod("leaveRequestUserId", LeaveRequest.class);
        method.setAccessible(true);
        return (String) method.invoke(leaveRequestToLeaveRequestResponseMapper, leaveRequest);
    }


}