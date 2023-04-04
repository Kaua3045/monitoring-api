package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(value = "/links-responses")
@Tag(name = "Links Responses")
public interface LinkResponseAPI {

    @GetMapping(
            value = "{urlId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "List a link responses by link id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link responses find successfully"),
            @ApiResponse(responseCode = "404", description = "Link was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    List<LinkResponseOutput> getAllLinkResponseByUrlId(
            @PathVariable String urlId,
            @RequestParam(name = "start", required = false, defaultValue = " ") final String startTimestamp,
            @RequestParam(name = "end", required = false, defaultValue = " ") final String endTimestamp
    );
}
