package com.kaua.monitoring.domain.checking;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.links.Link;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class LinkResponse extends Aggregate<LinkResponseID> {

    private String responseMessage;
    private int statusCode;
    private Instant verifiedDate;
    private Link link;

    public LinkResponse(
            final LinkResponseID aLinkResponseID,
            final String aResponseMessage,
            final int aStatusCode,
            final Instant aVerifiedDate,
            final Link aLink
    ) {
        super(aLinkResponseID);
        this.responseMessage = aResponseMessage;
        this.statusCode = aStatusCode;
        this.verifiedDate = aVerifiedDate;
        this.link = aLink;
    }

    public static LinkResponse newLinkResponse(
            final String aResponseMessage,
            final int aStatusCode,
            final Instant aVerifiedDate,
            final Link aLink
    ) {
        return new LinkResponse(
                LinkResponseID.unique(),
                aResponseMessage,
                aStatusCode,
                aVerifiedDate,
                aLink
        );
    }

    @Override
    public List<Error> validate() {
        return null;
    }
}
