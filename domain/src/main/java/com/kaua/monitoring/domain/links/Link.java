package com.kaua.monitoring.domain.links;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.profile.Profile;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Link extends Aggregate<LinkID> {

    private String title;
    private String url;
    private Instant executeDate;
    private boolean repeat;
    private Profile profile;

    public Link(
            final LinkID aLinkID,
            final String aTitle,
            final String aUrl,
            final Instant aExecuteDate,
            final boolean aRepeat,
            final Profile aProfile
    ) {
        super(aLinkID);
        this.title = aTitle;
        this.url = aUrl;
        this.executeDate = aExecuteDate;
        this.repeat = aRepeat;
        this.profile = aProfile;
    }

    public static Link newLink(
            final String aTitle,
            final String aUrl,
            final Instant aExecuteDate,
            final boolean aRepeat,
            final Profile aProfile
    ) {
        return new Link(
                LinkID.unique(),
                aTitle,
                aUrl,
                aExecuteDate,
                aRepeat,
                aProfile
        );
    }

    public Link update(
            final String aTitle,
            final String aUrl,
            final Instant aExecuteDate,
            final boolean aRepeat
    ) {
        this.title = aTitle;
        this.url = aUrl;
        this.executeDate = aExecuteDate;
        this.repeat = aRepeat;
        return this;
    }

    @Override
    public List<Error> validate() {
        final var errors = new ArrayList<Error>();

        if (title == null || title.isBlank()) {
            errors.add(new Error("'title' should not be null or empty"));
        }

        if (url == null || url.isBlank() || !isUrlValid(url)) {
            errors.add(new Error("'url' you must provide a valid url"));
        }

        if (executeDate == null) {
            errors.add(new Error("'executeDate' should not be null"));
        }

        if (executeDate != null && executeDate.isBefore(Instant.now())) {
            errors.add(new Error("'executeDate' cannot be a date that has already passed"));
        }

        if (profile == null) {
            errors.add(new Error("'profile' should not be null"));
        }

        return errors;
    }

    private boolean isUrlValid(final String aUrl) {
        try {
            final var a = new URL(aUrl).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
