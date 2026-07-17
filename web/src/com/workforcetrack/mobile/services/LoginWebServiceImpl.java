package com.workforcetrack.mobile.services;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.rest.base.exception.UserNotActivatedException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.mobile.rpc.login.MCompanyList;
import com.workforcetrack.mobile.rpc.login.MUserCompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/24/11
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("loginWebService")
public class LoginWebServiceImpl implements LoginWebService, Constants {

    @Autowired
    @Qualifier("loginService")
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private UserSessionManager userSessionManager;

    @Override
    public MCompanyList getUserCompanies(String userName, String password, String userAgent, String hostUrl) throws UserNotFoundException, IncorrectPasswordException {

        List<UserCompanyDTO> companyDTOList;
        if (userName == null || password == null || "".equals(userName.trim()) || "".equals(password.trim())) {
            throw new UserNotFoundException("Username or Password is empty");
        } else {
            ServerSecurityContext.getInstance().setSessionId(null);
            companyDTOList = filterUserCompanyDTOListAsMap(globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(hostUrl != null && !"".equals(hostUrl) ? hostUrl : HOST_LIVE, userName, password));

            if (companyDTOList.size() == 0) {
                throw new IncorrectPasswordException("Incorrect user name or password");
            }
        }
        return new MCompanyList(companyDTOList);

        /*//3 false
        CompanyItem[] companyItems = loginService.getUserCompanies(userName, password, userAgent, false);
        MCompanyList companyList = new MCompanyList(companyItems);
        return companyList;*/
    }

    @Override
    public MCompanyList getLoginedUserCompanies(String userName, String hostUrl) throws UserNotFoundException, IncorrectPasswordException {

        List<UserCompanyDTO> companyDTOList;
        if (userName == null) {
            throw new UserNotFoundException("Username is empty");
        } else {
//            ServerSecurityContext.getInstance().setSessionId(null);
            companyDTOList = filterUserCompanyDTOListAsMap(globalAuthJdbcSpringManager.getAuthInfoByUsername(hostUrl != null && !"".equals(hostUrl) ? hostUrl : HOST_LIVE, userName));

            if (companyDTOList.size() == 0) {
                throw new IncorrectPasswordException("Incorrect user name or password");
            }
        }
        return new MCompanyList(companyDTOList);

        /*//3 false
        CompanyItem[] companyItems = loginService.getUserCompanies(userName, password, userAgent, false);
        MCompanyList companyList = new MCompanyList(companyItems);
        return companyList;*/
    }

    @Override
    public MUserCompanyDTO lightLogin(String userName, String password, String userAgent, Integer companyID, String hostUrl) throws UserNotFoundException, IncorrectPasswordException, UserNotActivatedException {
        UserCompanyDTO userCompanyDTO = login(userName, password, userAgent, companyID, hostUrl);
        MUserCompanyDTO mUserCompanyDTO = new MUserCompanyDTO(userCompanyDTO);
        mUserCompanyDTO = loginServiceLocal.getUserInfo(mUserCompanyDTO, false);
        return mUserCompanyDTO;
    }

    @Override
    public MUserCompanyDTO mobileLogin(String userName, String password, String userAgent, Integer companyID, String hostUrl) throws UserNotFoundException, IncorrectPasswordException, UserNotActivatedException {
        UserCompanyDTO userCompanyDTO = login(userName, password, userAgent, companyID, hostUrl);
        MUserCompanyDTO mUserCompanyDTO = new MUserCompanyDTO(userCompanyDTO);
        mUserCompanyDTO = loginServiceLocal.getMobileUserSettings(mUserCompanyDTO);

        return mUserCompanyDTO;
    }

    @Override
    @Transactional
    public MUserCompanyDTO mobileLogin(String userName, String password, String userAgent, Integer companyID, String deviceToken, String deviceType, String hostUrl) throws UserNotFoundException, IncorrectPasswordException, UserNotActivatedException {
        UserCompanyDTO userCompanyDTO = login(userName, password, userAgent, companyID, hostUrl);
        MUserCompanyDTO mUserCompanyDTO = new MUserCompanyDTO(userCompanyDTO);
        mUserCompanyDTO = loginServiceLocal.getMobileUserSettings(mUserCompanyDTO);
        setUserDeviceTypeAndToken(mUserCompanyDTO.getUserID(), deviceType, deviceToken);
        return mUserCompanyDTO;
    }

    @Override
    public String getSessionID(Integer companyID) {
        String dataBaseName = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID);
        String sessionID = dataBaseName + "$" + companyID.toString();
        sessionID = sessionID.replace("%24", "$");

        return sessionID;
    }

    @Override
    @Transactional
    public MUserCompanyDTO getMainParams() {
        MUserCompanyDTO userCompanyDTO = new MUserCompanyDTO();
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        userCompanyDTO.setUserID(user.getObjectID());
        userCompanyDTO.setCompanyID(company.getObjectID());
        userCompanyDTO.setCompanyName(company.getName());
        userCompanyDTO.setCompanyDescription(company.getDescription());
        String logo = loginServiceLocal.getCompanyLogoURL(company);
        if (logo == null) {
            logo = "/no-logo.gif";
        }
        userCompanyDTO.setLogo(logo);
        userCompanyDTO.setActive(company.getActive() != null ? company.getActive() : Boolean.TRUE);
        userCompanyDTO = loginServiceLocal.getMobileUserSettings(userCompanyDTO);
        return userCompanyDTO;
    }

    @Override
    @Transactional
    public MUserCompanyDTO switchCompany(Integer companyId, String hostUrl, String userAgent) {
        EdsUser user = userManager.getUser();
        loginServiceLocal.logout();
        UserCompanyDTO userCompanyDTO = filterUserCompanyDTO(globalAuthJdbcSpringManager.getUserInfoByUserNameAndCompany(hostUrl != null && !"".equals(hostUrl) ? hostUrl : HOST_LIVE, user.getUserName(), companyId), userAgent);
        MUserCompanyDTO mUserCompanyDTO = new MUserCompanyDTO(userCompanyDTO);
        mUserCompanyDTO = loginServiceLocal.getUserInfo(mUserCompanyDTO, false);
        return mUserCompanyDTO;
    }

    @Override
    @Transactional
    public void setUserDeviceTypeAndToken(Integer userID, String deviceType, String deviceToken) {
        if (userID != null && userID > 0 && !StringUtil.isEmpty(deviceType) && !StringUtil.isEmpty(deviceToken)) {
            EdsUser edsUser = userManager.get(userID);
            edsUser.setMobileDeviceType(DeviceTypeEnum.valueOf(deviceType));
            edsUser.setDeviceToken(deviceToken);
            userManager.update(edsUser);
        }
    }

    @Override
    @Transactional
    public void updateUserLastAccessTime() {
        if (StringUtil.isEmpty(ServerSecurityContext.getInstance().getSessionId())) {
            return;
        }
        EdsUserSession edsUserSession = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId());
        if (edsUserSession != null) {
            edsUserSession.setExpired(false);
            edsUserSession.setLastAccessTime(new Date());
            userSessionManager.update(edsUserSession);
        }
    }


    private UserCompanyDTO login(String userName, String password, String userAgent, Integer companyID, String hostUrl) throws UserNotFoundException,
            IncorrectPasswordException, UserNotActivatedException {

        // validation
        if (userName == null || password == null || "".equals(userName.trim()) || "".equals(password.trim())) {
            throw new UserNotFoundException("User name or password is empty");
        } else {
            ServerSecurityContext.getInstance().setSessionId(null);
            List<UserCompanyDTO> companyDTOList = filterUserCompanyDTOListAsMap(globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(hostUrl != null && !"".equals(hostUrl) ? hostUrl : HOST_LIVE, userName, password));

            if (companyID == null && companyDTOList != null && companyDTOList.size() == 1) {
                companyID = companyDTOList.get(0).getCompanyID();
            } else if (companyDTOList != null && companyDTOList.size() == 0) {
                throw new IncorrectPasswordException("Incorrect user name or password");
            }

            if (companyID != null) {
                for (UserCompanyDTO userCompanyDTO : companyDTOList) {
                    if (companyID.equals(userCompanyDTO.getCompanyID())) {
                        ServerSecurityContext.getInstance().setCompanyId(companyID.toString());
                        ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());

                        AuthDetails authDetails = new AuthDetails();
                        authDetails.setCompanyID(userCompanyDTO.getCompanyID());
                        authDetails.setDatabase(userCompanyDTO.getClusterDbName());
                        authDetails.setUserID(userCompanyDTO.getUserID());
                        authDetails.setUserAgent(userAgent);

                        SessionService sessionService = ApplicationContextProvider.applicationContext.getBean(SessionService.class);

                        String sessionID = sessionService.obtainSession(authDetails);
                        userCompanyDTO.setSessionID(sessionID);
                        return userCompanyDTO;
                    }
                }
            }

            return new UserCompanyDTO();
        }
    }

    public List<UserCompanyDTO> filterUserCompanyDTOListAsMap(List<UserCompanyDTO> urlList) {
        List<UserCompanyDTO> companyDTOList = new ArrayList<>();
        for (UserCompanyDTO userCompanyDTO : urlList) {
            ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());

            EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());
            EdsUser user = null;
            if (company != null) {
                try {
                    user = userManager.get(userCompanyDTO.getUserID());
                } catch (Exception e) {
                    //schema dosnt exists
                }
            }
            if (company != null && user != null && !user.getDeleted() && EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {
                if (Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) { // is survey respondent
                    continue;
                }

                if (user instanceof EdsClientContact) {
                    if (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess())) {
                        //logger.debug("Client contact access is false for user " + user.getObjectID());
                        continue;
                    }
                }
                userCompanyDTO.setCompanyName(company.getName());
            }
            companyDTOList.add(userCompanyDTO);

        }
        return companyDTOList;
    }

    private UserCompanyDTO filterUserCompanyDTO(UserCompanyDTO userCompanyDTO, String userAgent) {
        ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());

        EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());
        EdsUser user = null;
        if (company != null) {
            try {
                user = userManager.get(userCompanyDTO.getUserID());
            } catch (Exception e) {
                //schema dosnt exists
            }
        }
        if (company != null && user != null && !user.getDeleted() && EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {

            if (user instanceof EdsClientContact) {
                if (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess())) {
                    return null;
                }
            }
            userCompanyDTO.setCompanyName(company.getName());
        }

        AuthDetails authDetails = new AuthDetails();
        authDetails.setCompanyID(userCompanyDTO.getCompanyID());
        authDetails.setDatabase(userCompanyDTO.getClusterDbName());
        authDetails.setUserID(userCompanyDTO.getUserID());
        authDetails.setUserAgent(userAgent);

        SessionService sessionService = ApplicationContextProvider.applicationContext.getBean(SessionService.class);

        String sessionID = sessionService.obtainSession(authDetails);
        userCompanyDTO.setSessionID(sessionID);


        return userCompanyDTO;
    }
}
