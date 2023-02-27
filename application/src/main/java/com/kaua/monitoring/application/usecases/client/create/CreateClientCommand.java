package com.kaua.monitoring.application.usecases.client.create;

public record CreateClientCommand(
        String name,
        String email,
        String password
) {
}
