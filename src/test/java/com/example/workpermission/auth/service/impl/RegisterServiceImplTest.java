package com.example.workpermission.auth.service.impl;

import com.example.workpermission.auth.exception.UserAlreadyExistException;
import com.example.workpermission.auth.model.User;
import com.example.workpermission.auth.model.dto.request.RegisterRequest;
import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.auth.model.enums.UserType;
import com.example.workpermission.auth.model.mapper.RegisterRequestToUserEntityMapper;
import com.example.workpermission.auth.model.mapper.UserEntityToUserMapper;
import com.example.workpermission.auth.repository.UserRepository;
import com.example.workpermission.base.AbstractBaseServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private RegisterServiceImpl registerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final RegisterRequestToUserEntityMapper registerRequestToUserEntityMapper =
            RegisterRequestToUserEntityMapper.initialize();

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();

    @Test
    void givenManagerRegisterRequest_whenRegisterManager_thenReturnAdmin() {

        // Given
        final RegisterRequest request = RegisterRequest.builder()
                .email("usertest@example.com")
                .password("password123")
                .firstName("User FirstName")
                .lastName("User LastName")
                .userType(UserType.MANAGER)
                .phoneNumber("1234567890")
                .build();

        final String encodedPassword = "encodedPassword";

        final UserEntity userEntity = registerRequestToUserEntityMapper.mapForSaving(request);

        final User expected = userEntityToUserMapper.map(userEntity);

        // When
        when(userRepository.existsUserEntityByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Then
        User result = registerService.registerUser(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getLastName(), result.getLastName());

        // Verify
        verify(userRepository).save(any(UserEntity.class));

    }

    @Test
    void givenManagerRegisterRequest_whenEmailAlreadyExists_thenThrowManagerAlreadyExistException() {

        // Given
        final RegisterRequest request = RegisterRequest.builder()
                .email("usertest@example.com")
                .password("password123")
                .firstName("User FirstName")
                .lastName("User LastName")
                .userType(UserType.MANAGER)
                .phoneNumber("1234567890")
                .build();

        // When
        when(userRepository.existsUserEntityByEmail(request.getEmail())).thenReturn(true);

        // Then
        assertThrows(UserAlreadyExistException.class, () -> registerService.registerUser(request));

        // Verify
        verify(userRepository, never()).save(any(UserEntity.class));

    }

}