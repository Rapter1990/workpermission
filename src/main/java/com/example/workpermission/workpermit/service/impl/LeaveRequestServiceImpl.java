package com.example.workpermission.workpermit.service.impl;

import com.example.workpermission.auth.exception.UserNotFoundException;
import com.example.workpermission.auth.model.UserIdentity;
import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.auth.repository.UserRepository;
import com.example.workpermission.common.model.CustomPage;
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
import com.example.workpermission.workpermit.service.LeaveRequestService;
import com.example.workpermission.workpermit.strategy.AnnualLeaveCalculator;
import com.example.workpermission.workpermit.utils.HolidayUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    private final UserIdentity userIdentity;

    private final LeaveRequestEntityToLeaveRequestMapper leaveRequestEntityToLeaveRequestMapper =
            LeaveRequestEntityToLeaveRequestMapper.initialize();

    private final ListLeaveRequestEntityToListLeaveRequestMapper listLeaveRequestEntityToListLeaveRequestMapper =
            ListLeaveRequestEntityToListLeaveRequestMapper.initialize();

    /**
     * Creates a new leave request.
     *
     * @param request the leave request details
     * @return the created leave request mapped to the domain model
     * @throws UserNotFoundException if the user is not found
     * @throws InvalidLeavePeriodException if no working days are found in the leave period
     * @throws InsufficientLeaveDaysException if the user does not have enough remaining leave days
     */
    @Override
    @Transactional
    public LeaveRequest createLeaveRequest(CreateLeaveRequest request) {

        this.checkCurrentUser(request.getUserId());

        // 1. Retrieve the user by ID (Manager or Employee).
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if (user.getJoiningDate() == null) {
            throw new IllegalStateException("User joining date is not set.");
        }

        // 2. Use the user's joining date
        LocalDate joiningDate = user.getJoiningDate();
        long yearsOfService = ChronoUnit.YEARS.between(joiningDate, LocalDate.now());
        long annualLeaveDays = AnnualLeaveCalculator.calculateAnnualLeaveDays(yearsOfService);

        // 3. Calculate the total requested working days (excluding weekends and holidays).
        long totalRequestedDays = HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(
                request.getStartDate(), request.getEndDate());
        if (totalRequestedDays <= 0) {
            throw new InvalidLeavePeriodException();
        }

        // 4. Retrieve existing leave requests for this user and calculate used leave days for the current year.
        List<LeaveRequestEntity> leaveRequests = leaveRequestRepository.findByUserId(user.getId());
        long usedDaysThisYear = leaveRequests.stream()
                .filter(lr -> lr.getStatus() == LeaveStatus.APPROVED)
                .filter(lr -> lr.getStartDate().getYear() == LocalDate.now().getYear())
                .mapToLong(lr -> HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(lr.getStartDate(), lr.getEndDate()))
                .sum();

        long remainingDays = annualLeaveDays - usedDaysThisYear;
        if (remainingDays < totalRequestedDays) {
            throw new InsufficientLeaveDaysException("Remaining: " + remainingDays);
        }

        // 5. Create a new LeaveRequestEntity, assign the user, dates, and set default status to PENDING.
        LeaveRequestEntity entity = LeaveRequestEntity.builder()
                .user(user)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(LeaveStatus.PENDING)
                .build();

        // 6. Save the entity.
        LeaveRequestEntity savedLeaveRequestEntity = leaveRequestRepository.save(entity);

        return leaveRequestEntityToLeaveRequestMapper.map(savedLeaveRequestEntity);
    }

    /**
     * Updates the status of an existing leave request.
     *
     * @param leaveRequestId the ID of the leave request to update
     * @param newStatus      the new leave status
     * @return the updated leave request mapped to the domain model
     * @throws LeaveRequestNotFoundException if the leave request is not found
     */
    @Override
    @Transactional
    public LeaveRequest updateLeaveStatus(String leaveRequestId, LeaveStatus newStatus) {
        LeaveRequestEntity entity = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(LeaveRequestNotFoundException::new);
        entity.setStatus(newStatus);
        LeaveRequestEntity savedLeaveRequestEntity = leaveRequestRepository.save(entity);
        return leaveRequestEntityToLeaveRequestMapper.map(savedLeaveRequestEntity);
    }

    /**
     * Retrieves the remaining leave days for a specified user.
     *
     * @param userId the ID of the user
     * @return the number of remaining leave days
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public Long getRemainingLeaveDays(String userId) {

        this.checkCurrentUser(userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        LocalDate joiningDate = user.getJoiningDate(); // Ensure that UserEntity defines getJoiningDate()
        long yearsOfService = ChronoUnit.YEARS.between(joiningDate, LocalDate.now());
        long annualLeaveDays = AnnualLeaveCalculator.calculateAnnualLeaveDays(yearsOfService);

        List<LeaveRequestEntity> leaveRequests = leaveRequestRepository.findByUserId(user.getId());
        long usedDaysThisYear = leaveRequests.stream()
                .filter(lr -> lr.getStatus() == LeaveStatus.APPROVED)
                .filter(lr -> lr.getStartDate().getYear() == LocalDate.now().getYear())
                .mapToLong(lr -> HolidayUtils.calculateWorkingDaysExcludingWeekendsAndHolidays(lr.getStartDate(), lr.getEndDate()))
                .sum();

        return annualLeaveDays - usedDaysThisYear;
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequest getLeaveRequestById(String id) {
        LeaveRequestEntity leaveRequestEntity = leaveRequestRepository.findById(id)
                .orElseThrow(()->new LeaveRequestNotFoundException("Leave Request given id can't found " + id));

        return leaveRequestEntityToLeaveRequestMapper.map(leaveRequestEntity);
    }

    /**
     * Retrieves a paginated list of leave requests for the specified user.
     *
     * @param userId             the ID of the user
     * @param customPagingRequest the paging parameters
     * @return a CustomPage of LeaveRequest domain models
     */
    @Override
    @Transactional(readOnly = true)
    public CustomPage<LeaveRequest> getLeaveRequestsByUser(String userId, CustomPagingRequest customPagingRequest) {

        Page<LeaveRequestEntity> leaveRequestPageEntities = leaveRequestRepository.findByUserId(userId, customPagingRequest.toPageable());

        if (leaveRequestPageEntities.getContent().isEmpty()) {
            throw new LeaveRequestNotFoundException("Couldn't find any airport");
        }

        final List<LeaveRequest> productDomainModels = listLeaveRequestEntityToListLeaveRequestMapper
                .toLeaveRequestList(leaveRequestPageEntities.getContent());

        return CustomPage.of(productDomainModels, leaveRequestPageEntities);

    }

    private void checkCurrentUser(String userId) {
        // Get the current user's ID from UserIdentity.
        String currentUserId = userIdentity.getUserId();
        if (!userId.equals(currentUserId)) {
            throw new AccessDeniedException("You are not authorized to create a leave request for another user.");
        }
    }

}
