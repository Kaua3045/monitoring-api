package com.kaua.monitoring.infrastructure.link.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateLinkBody(
        @JsonProperty("title") String title,
        @JsonProperty("url") String url,
        @JsonProperty("execute_date") String executeDate,
        @JsonProperty("link_execution") String linkExecution
) {
}
