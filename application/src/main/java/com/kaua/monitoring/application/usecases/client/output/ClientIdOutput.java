package com.kaua.monitoring.application.usecases.client.output;

public record ClientIdOutput(String id) {

    public static ClientIdOutput from(final String anId) {
        return new ClientIdOutput(anId);
    }
}
