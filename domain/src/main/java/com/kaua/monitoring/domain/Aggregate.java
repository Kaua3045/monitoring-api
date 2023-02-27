package com.kaua.monitoring.domain;

import com.kaua.monitoring.domain.exceptions.Error;

import java.util.List;

public abstract class Aggregate<ID extends Identifier> extends EntityIdentifier<ID> {

    protected Aggregate(ID id) {
        super(id);
    }

    public abstract List<Error> validate();
}
