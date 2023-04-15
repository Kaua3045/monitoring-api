package com.kaua.monitoring.application.usecases.security;

public record CreateAuthenticateCommand(String email, String password) {
}
