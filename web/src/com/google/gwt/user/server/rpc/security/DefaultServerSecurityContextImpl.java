package com.google.gwt.user.server.rpc.security;

import com.edatasite.shared.log.KpiLog;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 26, 2007
 * Time: 2:13:24 PM
 * To change this template use File | Settings | File Templates.
 */

public class DefaultServerSecurityContextImpl extends ServerSecurityContext {

    private String sessionId;
    private Boolean superUser;
    private String companyId;

    @Override
    public String getDatabase() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDatabase(String database) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Object getUser() {
        return new DefaultUserImpl(sessionId);
    }

    @Override
    public boolean isLoggedIn() {
        return sessionId != null;
    }

    @Override
    public String getSessionId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSuperUser(Boolean superUser) {
       this.superUser =superUser;
    }

    @Override
    public Boolean isSuperUser() {
        return this.superUser;
    }

    @Override
    public Integer getStaticUserID() {
        return null;
    }

    @Override
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public void removeCompanyId() {
        setCompanyId((String) null);
    }

    @Override
    public void setStaticUserID(Integer staticUserID) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCompanyId(Integer companyId) {
        if (companyId != null) {
            setCompanyId(companyId.toString());
        }
    }

    @Override
    public String getCompanyId() {
        return null;
    }

    @Override
    public void setDummySessionId(String session) {
        this.sessionId = session;
    }

    @Override
    public void setStartDate(long startDate) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getStartDate() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setUniqueID(String uniqueID) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUniqueID() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Locale getUserLocale() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setUserLocale(Locale userLocale) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public KpiLog getKpiLog() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public void setVersion(String version) {

	}

    @Override
    public String getSource() {
        return "";
    }

    @Override
    public void setSource(String source) {

    }

    @Override
    public Integer getUserId() {
        return null;
    }
}
