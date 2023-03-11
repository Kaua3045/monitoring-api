package com.kaua.monitoring.domain.checking;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.links.Link;
import lombok.Getter;

import java.util.List;

@Getter
public class LinkResponse extends Aggregate<LinkResponseID> {

    private String responseMessage;
    private int statusCode;
    private Link link;

    public LinkResponse(
            final LinkResponseID aLinkResponseID,
            final String aResponseMessage,
            final int aStatusCode,
            final Link aLink
    ) {
        super(aLinkResponseID);
        this.responseMessage = aResponseMessage;
        this.statusCode = aStatusCode;
        this.link = aLink;
    }

    public static LinkResponse newLinkResponse(
            final String aResponseMessage,
            final int aStatusCode,
            final Link aLink
    ) {
        return new LinkResponse(
                LinkResponseID.unique(),
                aResponseMessage,
                aStatusCode,
                aLink
        );
    }

    @Override
    public List<Error> validate() {
        return null;
    }
}
