package com.kaua.monitoring.application.gateways;

public interface EncrypterGateway {

    String encrypt(String value);

    boolean isMatch(String value, String encryptedValue);
}
