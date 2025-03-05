package com.example.workpermission.workpermit.repository;

import com.example.workpermission.workpermit.model.entity.LeaveRequestEntity;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, String> {

    List<LeaveRequestEntity> findByUserId(String userId);

    Page<LeaveRequestEntity> findByUserId(String userId, Pageable pageable);

}
