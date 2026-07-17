package com.edatasite.workforce.gwt.core.server.db.hmrc;

import com.edatasite.workforce.core.domain.hmrc.EdsHmrcUserCredentials;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Optional;

public interface HmrcUserCredentialsManager extends Manager<EdsHmrcUserCredentials> {

    Optional<EdsHmrcUserCredentials> findFirst();
}
