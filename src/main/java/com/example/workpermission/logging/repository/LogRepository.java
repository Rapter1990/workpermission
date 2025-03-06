package com.example.workpermission.logging.repository;

import com.example.workpermission.logging.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity,String> {

}
