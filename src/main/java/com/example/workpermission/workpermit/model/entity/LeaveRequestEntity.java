package com.example.workpermission.workpermit.model.entity;

import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.common.model.entity.BaseEntity;
import com.example.workpermission.workpermit.model.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "LEAVE_REQUESTS")
public class LeaveRequestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "LEAVE_START_DATE")
    private LocalDate startDate;

    @Column(name = "LEAVE_END_DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "LEAVE_STATUS")
    @Builder.Default
    private LeaveStatus status = LeaveStatus.PENDING;

}
