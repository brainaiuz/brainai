package com.edatasite.workforce.gwt.backend.server.app;

public interface BannedDomainServiceLocal {
    boolean areEmailAndDomainBanned(String email);
}
