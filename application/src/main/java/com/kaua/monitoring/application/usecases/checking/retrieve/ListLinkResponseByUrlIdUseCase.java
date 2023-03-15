package com.kaua.monitoring.application.usecases.checking.retrieve;

import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;

import java.util.List;

public abstract class ListLinkResponseByUrlIdUseCase extends UseCase<
        List<LinkResponseOutput>, ListLinkResponseByUrlIdCommand> {
}
