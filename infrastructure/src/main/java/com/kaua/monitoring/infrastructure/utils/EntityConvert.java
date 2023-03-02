package com.kaua.monitoring.infrastructure.utils;

public abstract class EntityConvert<ENTITY, DOMAIN> {

    public abstract ENTITY toEntity(final DOMAIN aDomain);

    public abstract DOMAIN toDomain(final ENTITY aEntity);
}
