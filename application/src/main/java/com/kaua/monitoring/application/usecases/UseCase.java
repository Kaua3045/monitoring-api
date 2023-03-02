package com.kaua.monitoring.application.usecases;

public abstract class UseCase<OUT, IN> {

    public abstract OUT execute(IN aCommand);
}
