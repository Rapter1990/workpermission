package com.example.workpermission.auth.service;

import com.example.workpermission.auth.model.Token;
import com.example.workpermission.auth.model.dto.request.TokenRefreshRequest;

public interface RefreshTokenService {

    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);

}
