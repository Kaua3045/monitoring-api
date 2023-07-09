package com.kaua.monitoring.domain.links;

public enum LinkExecutions {

    NO_REPEAT("Não repetir"),
    ON_SPECIFIC_DAY("No dia específico e na hora, todo mês"),
    EVERY_DAYS("Todos os dias na mesma hora"),
    TWO_TIMES_A_MONTH("Duas vezes ao mês"),
    EVERY_FIVE_HOURS("A cada 5 horas");

    private String name;

    LinkExecutions(final String aName) {
        this.name = aName;
    }

    public String getName() {
        return name;
    }
}
