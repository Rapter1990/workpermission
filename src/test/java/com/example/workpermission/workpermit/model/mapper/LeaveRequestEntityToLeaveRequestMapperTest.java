package com.example.workpermission.workpermit.model.mapper;

import com.example.workpermission.workpermit.model.LeaveRequest;
import com.example.workpermission.workpermit.model.entity.LeaveRequestEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaveRequestEntityToLeaveRequestMapperTest {

    private final LeaveRequestEntityToLeaveRequestMapper leaveRequestEntityToLeaveRequestMapper =
            LeaveRequestEntityToLeaveRequestMapper.initialize();

    @Test
    void testMapCollection_NullSources_ReturnsNull() {
        Collection<LeaveRequestEntity> sources = null;
        List<LeaveRequest> result = leaveRequestEntityToLeaveRequestMapper.map(sources);
        assertNull(result, "Mapping a null collection should return null.");
    }

    @Test
    void testMap_NullEntity_ReturnsNull() {
        LeaveRequest result = leaveRequestEntityToLeaveRequestMapper.map((LeaveRequestEntity) null);
        assertNull(result, "Mapping a null LeaveRequestEntity should return null.");
    }

}