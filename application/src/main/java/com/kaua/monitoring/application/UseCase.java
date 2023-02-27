package com.kaua.monitoring.application;

public abstract class UseCase<OUT, IN> {

    public abstract OUT execute(IN aCommand);
}
