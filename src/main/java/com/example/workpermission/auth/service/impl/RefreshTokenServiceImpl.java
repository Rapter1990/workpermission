package com.example.workpermission.auth.service.impl;

import com.example.workpermission.auth.exception.UserNotFoundException;
import com.example.workpermission.auth.exception.UserStatusNotValidException;
import com.example.workpermission.auth.model.Token;
import com.example.workpermission.auth.model.dto.request.TokenRefreshRequest;
import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.auth.model.enums.TokenClaims;
import com.example.workpermission.auth.model.enums.UserStatus;
import com.example.workpermission.auth.repository.UserRepository;
import com.example.workpermission.auth.service.RefreshTokenService;
import com.example.workpermission.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepository userRepository;
    private final TokenService tokenService;


    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {

        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final String adminId = tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final UserEntity userEntityFromDB = userRepository
                .findById(adminId)
                .orElseThrow(UserNotFoundException::new);

        this.validateAdminStatus(userEntityFromDB);

        return tokenService.generateToken(
                userEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        );
    }

    private void validateAdminStatus(final UserEntity userEntity) {
        if (!(UserStatus.ACTIVE.equals(userEntity.getUserStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + userEntity.getUserStatus());
        }
    }

}
