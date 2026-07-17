package com.workforcetrack.mobile.rpc.login;

import com.edatasite.workforce.gwt.backend.client.rpc.CompanyItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/24/11
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */

public class MCompanyList {


    private List<MCompanyItem> companyListItems;
    //Integer totalCount;


    public MCompanyList() {
    }

    public MCompanyList(CompanyItem[] companyItems) {

        if (companyItems != null) {
            this.companyListItems = new ArrayList<>();
            for (CompanyItem companyItem : companyItems) {
                this.companyListItems.add(new MCompanyItem(companyItem));
            }
        }
    }

/*    public MCompanyList(List<CompanyItem> companyListItems) {

        if (companyListItems != null) {
            this.companyListItems = new ArrayList<MCompanyItem>();
            for (CompanyItem companyItem : companyListItems) {
                this.companyListItems.add(new MCompanyItem(companyItem));
            }
        }
    }*/

    public MCompanyList(UserCompanyDTO[] userCompanyDTOs) {
        if (userCompanyDTOs != null) {
            this.companyListItems = new ArrayList<>();
            for (UserCompanyDTO userCompanyDTO : userCompanyDTOs) {
                this.companyListItems.add(new MCompanyItem(userCompanyDTO));
            }
        }
    }

     public MCompanyList(List<UserCompanyDTO> userCompanyDTOs) {
        if (userCompanyDTOs != null) {
            this.companyListItems = new ArrayList<>();
            for (UserCompanyDTO userCompanyDTO : userCompanyDTOs) {
                this.companyListItems.add(new MCompanyItem(userCompanyDTO));
            }
        }
    }

    @XmlElement(name = "companies")
    public List<MCompanyItem> getCompanyListItems() {
        return companyListItems;
    }

    public void setCompanyListItems(List<MCompanyItem> companyListItems) {
        this.companyListItems = companyListItems;
    }
}
