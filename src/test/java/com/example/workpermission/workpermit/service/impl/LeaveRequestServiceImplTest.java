package com.example.workpermission.workpermit.service.impl;

import com.example.workpermission.auth.model.UserIdentity;
import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.auth.repository.UserRepository;
import com.example.workpermission.base.AbstractBaseServiceTest;
import com.example.workpermission.common.model.CustomPage;
import com.example.workpermission.common.model.CustomPaging;
import com.example.workpermission.common.model.dto.request.CustomPagingRequest;
import com.example.workpermission.workpermit.exception.InsufficientLeaveDaysException;
import com.example.workpermission.workpermit.exception.InvalidLeavePeriodException;
import com.example.workpermission.workpermit.exception.LeaveRequestNotFoundException;
import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.dto.request.CreateLeaveRequest;
import com.example.workpermission.workpermit.model.entity.LeaveRequestEntity;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import com.example.workpermission.workpermit.model.mapper.LeaveRequestEntityToLeaveRequestMapper;
import com.example.workpermission.workpermit.model.mapper.ListLeaveRequestEntityToListLeaveRequestMapper;
import com.example.workpermission.workpermit.repository.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LeaveRequestServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LeaveRequestServiceImpl leaveRequestServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private UserIdentity userIdentity;

    private final LeaveRequestEntityToLeaveRequestMapper leaveRequestEntityToLeaveRequestMapper =
            LeaveRequestEntityToLeaveRequestMapper.initialize();

    private final ListLeaveRequestEntityToListLeaveRequestMapper listLeaveRequestEntityToListLeaveRequestMapper =
            ListLeaveRequestEntityToListLeaveRequestMapper.initialize();

    private final String currentUserId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        // Given the current user identity always returns the same user id.
        when(userIdentity.getUserId()).thenReturn(currentUserId);
    }

    @Test
    void testCreateLeaveRequest_valid() {

        // Given
        final CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(currentUserId)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();

        final UserEntity userEntity = UserEntity.builder()
                .id(currentUserId)
                .joiningDate(LocalDate.now().minusYears(2))
                .build();

        final LeaveRequestEntity savedEntity = LeaveRequestEntity.builder()
                .id("leave-uuid")
                .user(userEntity)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(LeaveStatus.PENDING)
                .build();

        long years = ChronoUnit.YEARS.between(userEntity.getJoiningDate(), LocalDate.now());

        LeaveRequest expected = leaveRequestEntityToLeaveRequestMapper.map(savedEntity);

        // When
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userEntity));
        when(leaveRequestRepository.findByUserId(currentUserId)).thenReturn(List.of());
        when(leaveRequestRepository.save(any(LeaveRequestEntity.class))).thenReturn(savedEntity);

        // Then
        LeaveRequest result = leaveRequestServiceImpl.createLeaveRequest(request);

        assertEquals(2, years, "Years of service should be 2, thus annual leave days expected is 15 from the strategy");
        assertNotNull(result, "Result should not be null");
        assertEquals(expected.getStartDate(), result.getStartDate(), "Start dates should match");
        assertEquals(expected.getEndDate(), result.getEndDate(), "End dates should match");
        assertEquals(expected.getStatus(), result.getStatus(), "Leave status should match");

        // Verify
        verify(userIdentity).getUserId();
        verify(userRepository).findById(currentUserId);
        verify(leaveRequestRepository).findByUserId(currentUserId);
        verify(leaveRequestRepository).save(any(LeaveRequestEntity.class));

    }

    @Test
    void testCreateLeaveRequest_invalidLeavePeriod() {
        // Given: A valid request using the current user's ID,
        // but with startDate and endDate on a weekend (so no working days).
        // For example, assume 8th February 2025 is a Saturday.
        LocalDate weekendDate = LocalDate.of(2025, 2, 8);
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(currentUserId)
                .startDate(weekendDate)
                .endDate(weekendDate)
                .build();

        // And: A UserEntity exists with a valid joining date.
        UserEntity userEntity = UserEntity.builder()
                .id(currentUserId)
                .joiningDate(LocalDate.now().minusYears(2))
                .build();
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userEntity));

        // And: The leave repository returns an empty list (no used leave days)
        when(leaveRequestRepository.findByUserId(currentUserId)).thenReturn(List.of());

        // When & Then: Expect InvalidLeavePeriodException because no working day is in the range.
        assertThrows(InvalidLeavePeriodException.class, () ->
                        leaveRequestServiceImpl.createLeaveRequest(request),
                "An InvalidLeavePeriodException should be thrown when no working days exist in the leave period."
        );

        verify(userIdentity).getUserId();
        verify(userRepository).findById(currentUserId);

    }

    @Test
    void testCreateLeaveRequest_insufficientLeaveDays() {

        // Given: a CreateLeaveRequest for the current user with a leave period that requires 3 working days.
        // For example, new request: startDate = 2025-04-04 (Friday) and endDate = 2025-04-08 (Tuesday).
        // Working days: Friday, Monday, Tuesday = 3.
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(currentUserId)
                .startDate(LocalDate.of(2025, 4, 4))
                .endDate(LocalDate.of(2025, 4, 8))
                .build();

        // And: a UserEntity exists with a joining date 2 years ago (annual leave = 15 days).
        // For stability, we set joiningDate to 2023-04-01 so that as of 2025, years of service = 2.
        UserEntity userEntity = UserEntity.builder()
                .id(currentUserId)
                .joiningDate(LocalDate.of(2023, 4, 1))
                .build();

        // And: simulate an already approved leave request that used 13 working days.
        // We'll set approvedEntity's period from 2025-04-10 to 2025-04-29.
        // Based on our HolidayUtils, this period yields 13 working days.
        LeaveRequestEntity approvedEntity = LeaveRequestEntity.builder()
                .id(UUID.randomUUID().toString())
                .user(userEntity)
                .startDate(LocalDate.of(2025, 4, 10))
                .endDate(LocalDate.of(2025, 4, 29))
                .status(LeaveStatus.APPROVED)
                .build();

        // When
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userEntity));
        when(leaveRequestRepository.findByUserId(currentUserId)).thenReturn(List.of(approvedEntity));

        // Then
        Exception exception = assertThrows(InsufficientLeaveDaysException.class, () -> {
            leaveRequestServiceImpl.createLeaveRequest(request);
        });

        assertTrue(exception.getMessage().contains("Remaining:"), "Expected exception message to contain remaining leave info");

        // Verify that the proper methods were called.
        verify(userIdentity).getUserId();
        verify(userRepository).findById(currentUserId);
        verify(leaveRequestRepository).findByUserId(currentUserId);

    }

    @Test
    void testCreateLeaveRequest_userIdMismatch() {

        // Given
        final CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();

        // When/Then: Expect AccessDeniedException because checkCurrentUser should fail.
        assertThrows(AccessDeniedException.class, () ->
                leaveRequestServiceImpl.createLeaveRequest(request)
        );

        // Verify
        verify(userIdentity).getUserId();
        verifyNoInteractions(userRepository);
        verifyNoInteractions(leaveRequestRepository);

    }

    @Test
    void testCreateLeaveRequest_nullJoiningDateThrowsIllegalStateException() {
        // Given: a CreateLeaveRequest for the current user.
        CreateLeaveRequest request = CreateLeaveRequest.builder()
                .userId(currentUserId)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();

        // And: a UserEntity exists with a null joiningDate.
        UserEntity userEntity = UserEntity.builder()
                .id(currentUserId)
                .joiningDate(null)
                .build();
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userEntity));

        // When & Then: Expect an IllegalStateException with the expected message.
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                leaveRequestServiceImpl.createLeaveRequest(request)
        );
        assertEquals("User joining date is not set.", exception.getMessage());

        // Verify that the appropriate methods were called.
        verify(userIdentity).getUserId();
        verify(userRepository).findById(currentUserId);
    }

    @Test
    void testUpdateLeaveStatus_valid() {

        // Given
        final String leaveRequestId = UUID.randomUUID().toString();
        final LeaveStatus newStatus = LeaveStatus.APPROVED;

        final UserEntity userEntity = UserEntity.builder()
                .id(currentUserId)
                .build();

        LeaveRequestEntity entity = LeaveRequestEntity.builder()
                .id(leaveRequestId)
                .user(userEntity)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(LeaveStatus.PENDING)
                .build();

        entity.setStatus(newStatus);

        LeaveRequest expected = leaveRequestEntityToLeaveRequestMapper.map(entity);

        // When
        when(leaveRequestRepository.findById(leaveRequestId)).thenReturn(Optional.of(entity));
        when(leaveRequestRepository.save(entity)).thenReturn(entity);

        // Then
        LeaveRequest result = leaveRequestServiceImpl.updateLeaveStatus(leaveRequestId, newStatus);

        assertNotNull(result, "Updated leave request should not be null");
        assertEquals(expected.getId(), result.getId(), "Leave request id should match");
        assertEquals(expected.getStatus(), result.getStatus(), "Leave status should be updated to APPROVED");
        assertEquals(expected.getUser().getId(), result.getUser().getId(), "User id should match");
        assertEquals(expected.getStartDate(), result.getStartDate(), "Start date should match");
        assertEquals(expected.getEndDate(), result.getEndDate(), "End date should match");

        // Verify
        verify(leaveRequestRepository).findById(leaveRequestId);
        verify(leaveRequestRepository).save(entity);

    }

    @Test
    void testGetRemainingLeaveDays_valid() {

        // Given
        final UserEntity userEntity = UserEntity.builder()
                .id(currentUserId)
                .joiningDate(LocalDate.now().minusYears(2))
                .build();

        // When
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(userEntity));
        when(leaveRequestRepository.findByUserId(currentUserId)).thenReturn(List.of());

        // Then
        Long remainingDays = leaveRequestServiceImpl.getRemainingLeaveDays(currentUserId);

        assertEquals(15L, remainingDays, "Remaining leave days should be 15 when no leave is used.");

        // Verify
        verify(userRepository).findById(currentUserId);
        verify(leaveRequestRepository).findByUserId(currentUserId);

    }

    @Test
    void testGetLeaveRequestById_valid() {

        // Given
        final String leaveRequestId = UUID.randomUUID().toString();

        final UserEntity userEntity = UserEntity.builder()
                .id(currentUserId)
                .build();

        final LeaveRequestEntity entity = LeaveRequestEntity.builder()
                .id(leaveRequestId)
                .user(userEntity)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(LeaveStatus.PENDING)
                .build();

        final LeaveRequest expected = leaveRequestEntityToLeaveRequestMapper.map(entity);

        // When
        when(leaveRequestRepository.findById(leaveRequestId)).thenReturn(Optional.of(entity));

        // Then
        LeaveRequest result = leaveRequestServiceImpl.getLeaveRequestById(leaveRequestId);

        assertNotNull(result, "Result should not be null");
        assertEquals(expected.getId(), result.getId(), "Leave request ID should match");
        assertEquals(expected.getStartDate(), result.getStartDate(), "Start date should match");
        assertEquals(expected.getEndDate(), result.getEndDate(), "End date should match");
        assertEquals(expected.getStatus(), result.getStatus(), "Leave status should match");
        assertEquals(expected.getUser().getId(), result.getUser().getId(), "User ID should match");

        // Verify
        verify(leaveRequestRepository).findById(leaveRequestId);

    }

    @Test
    void testGetLeaveRequestsByUser_valid() {

        // Given
        final CustomPagingRequest pagingRequest = CustomPagingRequest.builder()
                .pagination(CustomPaging.builder()
                        .pageSize(10)
                        .pageNumber(1)
                        .build())
                .build();


        final LeaveRequestEntity entity = LeaveRequestEntity.builder()
                .id(UUID.randomUUID().toString())
                .user(UserEntity.builder()
                        .id(currentUserId)
                        .firstName("First")
                        .lastName("Last")
                        .build())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(LeaveStatus.PENDING)
                .build();

        final Pageable pageable = PageRequest.of(0, 10);
        final Page<LeaveRequestEntity> pageEntities = new PageImpl<>(List.of(entity), pageable, 1);

        final List<LeaveRequest> domainList = listLeaveRequestEntityToListLeaveRequestMapper.toLeaveRequestList(pageEntities.getContent());

        final CustomPage<LeaveRequest> expected = CustomPage.of(domainList, pageEntities);

        // When
        when(leaveRequestRepository.findByUserId(eq(currentUserId), any(Pageable.class))).thenReturn(pageEntities);

        // Then
        CustomPage<LeaveRequest> result = leaveRequestServiceImpl.getLeaveRequestsByUser(currentUserId, pagingRequest);

        assertNotNull(result, "CustomPage should not be null");
        assertFalse(result.getContent().isEmpty(), "Content should not be empty");
        assertEquals(expected.getPageNumber(), result.getPageNumber(), "Page number should match");
        assertEquals(expected.getPageSize(), result.getPageSize(), "Page size should match");
        assertEquals(expected.getTotalElementCount(), result.getTotalElementCount(), "Total element count should match");
        assertEquals(expected.getTotalPageCount(), result.getTotalPageCount(), "Total page count should match");

        // Verify
        verify(leaveRequestRepository, times(1)).findByUserId(eq(currentUserId), any(Pageable.class));

    }

    @Test
    void testGetLeaveRequestsByUser_emptyThrowsException() {
        // Given: a CustomPagingRequest built with pageNumber = 1 and pageSize = 10.
        CustomPagingRequest customPagingRequest = CustomPagingRequest.builder()
                .pagination(CustomPaging.builder()
                        .pageNumber(1)
                        .pageSize(10)
                        .build())
                .build();

        // Prepare an empty Page of LeaveRequestEntity.
        Pageable pageable = PageRequest.of(customPagingRequest.getPagination().getPageNumber(), customPagingRequest.getPagination().getPageSize());
        Page<LeaveRequestEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        // When: repository returns an empty page.
        when(leaveRequestRepository.findByUserId(eq(currentUserId), any(Pageable.class))).thenReturn(emptyPage);

        // Then: Expect LeaveRequestNotFoundException to be thrown.
        Exception exception = assertThrows(LeaveRequestNotFoundException.class, () -> {
            leaveRequestServiceImpl.getLeaveRequestsByUser(currentUserId, customPagingRequest);
        });

        assertTrue(exception.getMessage().contains("Couldn't find any airport"),
                "Exception message should indicate that no leave requests were found.");

        verify(leaveRequestRepository).findByUserId(eq(currentUserId), any(Pageable.class));

    }


}