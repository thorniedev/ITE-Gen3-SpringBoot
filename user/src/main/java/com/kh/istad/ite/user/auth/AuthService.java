package com.kh.istad.ite.user.auth;

import com.kh.istad.ite.user.auth.dto.RegisterRequest;
import com.kh.istad.ite.user.auth.dto.RegisterResponse;

public interface AuthService
{
    RegisterResponse register(RegisterRequest registerRequest);
}
