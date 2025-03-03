package com.example.workpermission.auth.service;

import com.example.workpermission.auth.model.dto.request.TokenInvalidateRequest;

public interface LogoutService {

    void logout(final TokenInvalidateRequest tokenInvalidateRequest);

}
