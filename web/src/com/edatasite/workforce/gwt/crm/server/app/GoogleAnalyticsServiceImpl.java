package com.edatasite.workforce.gwt.crm.server.app;

import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleAnalyticsManager;
import com.edatasite.workforce.gwt.crm.client.rpc.GoogleAnalyticsService;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 11, 2011
 * Time: 10:59:18 PM
 */
@Transactional
@Service("googleAnalyticsService")
public class GoogleAnalyticsServiceImpl implements GoogleAnalyticsService {

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GoogleAnalyticsManager googleAnalyticsManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateCurrentUser() {
        return googleAnalyticsManager.validateUser(googleAnalyticsManager.getUser());
    }

    public void saveToken(String token) throws Exception {
        try {
            googleAnalyticsManager.createAnalyticsDetails(token);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new GeneralSecurityException(ex.getMessage());
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new AuthenticationException(ex.getMessage());
        } catch (ServiceException ex) {
            ex.printStackTrace();
            throw new ServiceException(ex);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getUserTeamName() {
        return googleAnalyticsManager.getUser().getEmployee().getEmployeeTeam().getTeam().getName();
    }

    public void deleteGoogleAnalyticsToken() {
        googleAnalyticsManager.delete(googleAnalyticsManager.getGoogleAnalytics(employeeManager.getUser()));
    }
}