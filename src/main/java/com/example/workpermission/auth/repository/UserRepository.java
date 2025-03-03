package com.example.workpermission.auth.repository;

import com.example.workpermission.auth.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsUserEntityByEmail(final String email);

    Optional<UserEntity> findUserEntityByEmail(final String email);

}
