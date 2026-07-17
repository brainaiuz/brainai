package com.workforcetrack.mobile.rpc.signup;

import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */

public class MNewCompany {

    private String adminFName;
    private String adminLName;
    private String adminEmail;
    private Integer countryID;
    private String phone;
    private String name;
    private boolean active = true;

    private String companySignedUpFrom;
	private String host;

    public MNewCompany() {
    }

    public MNewCompany(NewCompany newCompany) {
        if (newCompany != null) {
            this.adminFName = newCompany.getAdminFName();
            this.adminLName = newCompany.getAdminLName();
            this.adminEmail = newCompany.getAdminEmail();
            this.countryID = newCompany.getCountryID();
            this.phone = newCompany.getPhone();
            this.name = newCompany.getName();
            this.companySignedUpFrom = newCompany.getCompanySignedUpFrom();
            this.active = newCompany.isActive();
        }
    }

    public static Boolean convert(MNewCompany mNewCompany, NewCompany newCompany, boolean toNewCompany) {
        if (newCompany == null || mNewCompany == null)
            return null;

        try {
            if (toNewCompany) {
                newCompany.setAdminFName(mNewCompany.getAdminFName());
                newCompany.setAdminLName(mNewCompany.getAdminLName());
                newCompany.setAdminEmail(mNewCompany.getAdminEmail());
                newCompany.setCountryID(mNewCompany.getCountryID());
                newCompany.setPhone(mNewCompany.getPhone());
                newCompany.setName(mNewCompany.getName());
                newCompany.setCompanySignedUpFrom(mNewCompany.getCompanySignedUpFrom());

                newCompany.setActive(mNewCompany.isActive());
            } else {
                mNewCompany.setAdminFName(newCompany.getAdminFName());
                mNewCompany.setAdminLName(newCompany.getAdminLName());
                mNewCompany.setAdminEmail(newCompany.getAdminEmail());
                mNewCompany.setCountryID(newCompany.getCountryID());
                mNewCompany.setPhone(newCompany.getPhone());
                mNewCompany.setName(newCompany.getName());
                mNewCompany.setCompanySignedUpFrom(newCompany.getCompanySignedUpFrom());

                mNewCompany.setActive(newCompany.isActive());

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCompanySignedUpFrom() {
        return companySignedUpFrom;
    }

    public void setCompanySignedUpFrom(String companySignedUpFrom) {
        this.companySignedUpFrom = companySignedUpFrom;
    }

    public String getAdminFName() {
        return adminFName;
    }

    public void setAdminFName(String adminFName) {
        this.adminFName = adminFName;
    }

    public String getAdminLName() {
        return adminLName;
    }

    public void setAdminLName(String adminLName) {
        this.adminLName = adminLName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}

