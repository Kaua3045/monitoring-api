package com.kaua.monitoring.application.usecases.checking.retrieve;

import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.pagination.Pagination;

public abstract class ListLinkResponseByUrlIdUseCase extends UseCase<
        Pagination<LinkResponse>, ListLinkResponseByUrlIdCommand> {
}
