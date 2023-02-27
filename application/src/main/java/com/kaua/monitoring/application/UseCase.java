package com.kaua.monitoring.application;

public abstract class UseCase<IN, OUT> {

    public abstract IN execute(OUT aCommand);
}
