package com.kaua.monitoring.domain;

public abstract class Aggregate<ID extends Identifier> extends EntityIdentifier<ID> {

    protected Aggregate(String id) {
        super(id);
    }
}
