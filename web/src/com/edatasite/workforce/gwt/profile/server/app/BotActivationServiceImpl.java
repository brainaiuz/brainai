package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.core.domain.EdsBotActivation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.UUID;
import com.edatasite.workforce.gwt.core.client.rpc.BotActivationService;
import com.edatasite.workforce.gwt.core.server.db.BotActivationManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("botActivationService")
public class BotActivationServiceImpl implements BotActivationService, BotActivationServiceLocal {

    @Autowired
    private BotActivationManager botActivationManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Transactional
    @Override
    public String updateActivationKeyForUser() {
        EdsBotActivation activation = botActivationManager.getByEmail(currentUserEmail());
        if (activation == null) {
            activation = new EdsBotActivation();
            activation.setUsername(currentUserEmail());
            botActivationManager.create(activation);
        }
        activation.setKey(UUID.uuid(20));
        activation.setSessionId(ServerSecurityContext.getInstance().getSessionId());
        activation.setCompanyId(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        botActivationManager.update(activation);
        return activation.getKeyEncoded();
    }

    @Override
    public String getActivationKey() {
        EdsBotActivation activation = botActivationManager.getByEmail(currentUserEmail());
        return activation != null ? activation.getKeyEncoded() : updateActivationKeyForUser();
    }

    private String currentUserEmail() {
        int companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        int userID = ((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID();
        return globalAuthJdbcSpringManager.getUsername(companyID, userID);
    }
}
