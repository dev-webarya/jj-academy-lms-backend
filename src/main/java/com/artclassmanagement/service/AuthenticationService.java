package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.LoginRequest;
import com.artclassmanagement.dto.request.RefreshTokenRequest;
import com.artclassmanagement.dto.request.RegisterRequest;
import com.artclassmanagement.dto.response.AuthenticationResponse;
import com.artclassmanagement.dto.response.RegistrationResponse;

public interface AuthenticationService {

    RegistrationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
