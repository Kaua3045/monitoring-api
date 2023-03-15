package com.kaua.monitoring.application.usecases.checking.retrieve;

import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.domain.pagination.Pagination;

public abstract class ListLinkResponseByUrlIdUseCase extends UseCase<
        Pagination<LinkResponseOutput>, ListLinkResponseByUrlIdCommand> {
}
