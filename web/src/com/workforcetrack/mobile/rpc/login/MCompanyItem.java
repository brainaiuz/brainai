package com.workforcetrack.mobile.rpc.login;

import com.edatasite.workforce.gwt.backend.client.rpc.CompanyItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/24/11
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "companyListItem")
public class MCompanyItem extends MSelectItem {

    String companyName;

    public MCompanyItem () {

    }

    public MCompanyItem (CompanyItem companyItem) {
        if (companyItem != null) {
            this.companyName = companyItem.getCompanyName();
            this.setObjectID(companyItem.getId());
            this.setName(companyItem.getName());
            this.setDescription(companyItem.getDescription());
        }

    }

    public MCompanyItem (UserCompanyDTO userCompanyDTO) {
        if (userCompanyDTO != null) {
            this.companyName = userCompanyDTO.getCompanyName();
            this.setObjectID(userCompanyDTO.getCompanyID());
            this.setDescription(userCompanyDTO.getCompanyDescription());
        }
    }


    public static boolean convert(CompanyItem companyItem, MCompanyItem mCompanyItem, boolean fromCompanyItem) {

        if (companyItem == null || mCompanyItem == null)
            return false;

        try{
            if (fromCompanyItem) {
                mCompanyItem.setObjectID(companyItem.getId());
                mCompanyItem.setName(companyItem.getName());
                mCompanyItem.setCompanyName(companyItem.getCompanyName());
                mCompanyItem.setDescription(companyItem.getDescription());
            } else {
                companyItem.setId(mCompanyItem.getObjectID());
                companyItem.setName(mCompanyItem.getName());
                companyItem.setCompanyName(mCompanyItem.getCompanyName());
                companyItem.setDescription(mCompanyItem.getDescription());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
