package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.gateways.EncrypterGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptService implements EncrypterGateway {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BcryptService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public String encrypt(String value) {
        return bCryptPasswordEncoder.encode(value);
    }

    @Override
    public boolean isMatch(String value, String encryptedValue) {
        return bCryptPasswordEncoder.matches(value, encryptedValue);
    }
}
