package com.project.mobility.repository.login;

import com.project.mobility.model.login.provider.AuthProvider;

public interface LoginRepo {
    boolean login(AuthProvider authProvider);

    boolean register(AuthProvider authProvider);

    boolean logout(AuthProvider authProvider);
}
