package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.entity.LeaveRequestEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListLeaveRequestEntityToListLeaveRequestMapperTest {

    private final ListLeaveRequestEntityToListLeaveRequestMapper listLeaveRequestEntityToListLeaveRequestMapper =
            ListLeaveRequestEntityToListLeaveRequestMapper.initialize();

    @Test
    void testToLeaveRequestList_WhenLeaveRequestEntityListIsNull() {

        List<LeaveRequestEntity> leaveRequestEntityList = null;

        List<LeaveRequest> result = listLeaveRequestEntityToListLeaveRequestMapper.toLeaveRequestList(leaveRequestEntityList);

        // Assert that the result is null when leaveRequestEntityList is null
        assertNull(result);

    }

}