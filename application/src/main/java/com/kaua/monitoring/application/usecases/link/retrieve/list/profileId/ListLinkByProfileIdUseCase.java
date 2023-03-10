package com.kaua.monitoring.application.usecases.link.retrieve.list.profileId;

import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.domain.pagination.Pagination;

import java.util.List;

public abstract class ListLinkByProfileIdUseCase extends UseCase<Pagination<LinkOutput>, ListLinkByProfileIdCommand> {
}
