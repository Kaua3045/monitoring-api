package com.kaua.monitoring.application.usecases;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN aCommand);
}
