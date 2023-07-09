package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import com.kaua.monitoring.infrastructure.link.inputs.UpdateLinkBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/links")
@Tag(name = "Links")
public interface LinkAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Domain exception validation thrown"),
            @ApiResponse(responseCode = "404", description = "Profile was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> create(@RequestBody CreateLinkBody body);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a link by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link find successfully"),
            @ApiResponse(responseCode = "404", description = "Link was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> getLinkById(@PathVariable String id);

    @GetMapping(
            value = "/list/{profileId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "List a links by profile id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link find successfully"),
            @ApiResponse(responseCode = "404", description = "Profile was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<LinkOutput> getAllLinkByProfileId(
            @PathVariable String profileId,
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "title") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
            );

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a link by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link updated successfully"),
            @ApiResponse(responseCode = "400", description = "Domain exception validation thrown"),
            @ApiResponse(responseCode = "404", description = "Link was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateLinkById(@PathVariable String id, @RequestBody UpdateLinkBody body);

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Delete a link by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Link deleted successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> deleteLinkById(@PathVariable String id);
}
