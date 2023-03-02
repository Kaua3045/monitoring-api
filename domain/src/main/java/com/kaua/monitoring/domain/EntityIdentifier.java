package com.kaua.monitoring.domain;

import java.util.Objects;

public abstract class EntityIdentifier<ID extends Identifier> {

    protected final String id;


    protected EntityIdentifier(String id) {
        this.id = Objects.requireNonNull(id, "'id' should not be null");
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EntityIdentifier<?> that = (EntityIdentifier<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
