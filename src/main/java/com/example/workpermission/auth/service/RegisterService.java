package com.example.workpermission.auth.service;

import com.example.workpermission.auth.model.User;
import com.example.workpermission.auth.model.dto.request.RegisterRequest;

public interface RegisterService {

    User registerUser(final RegisterRequest registerRequest);
}
