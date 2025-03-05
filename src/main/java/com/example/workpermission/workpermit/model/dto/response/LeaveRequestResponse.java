package com.example.workpermission.workpermit.model.dto.response;

import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestResponse {
    private String id;
    private String userId;
    private String userFullName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;
}
