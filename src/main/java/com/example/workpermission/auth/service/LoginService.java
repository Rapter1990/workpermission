package com.example.workpermission.auth.service;

import com.example.workpermission.auth.model.Token;
import com.example.workpermission.auth.model.dto.request.LoginRequest;

public interface LoginService {

    Token login(final LoginRequest loginRequest);
}
