package com.workforcetrack.mobile.rpc.login;

import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/9/11
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class MUserCompanyDTOList {

    List<MUserCompanyDTO> companyListItem;

    public MUserCompanyDTOList(){}

    public MUserCompanyDTOList(UserCompanyDTO[] userCompanyDTOs){
        if (userCompanyDTOs != null) {
            this.companyListItem = new ArrayList<>();
            for (UserCompanyDTO userCompanyDTO : userCompanyDTOs) {
                this.companyListItem.add(new MUserCompanyDTO(userCompanyDTO));
            }
        }
    }

    public List<MUserCompanyDTO> getCompanyListItem() {
        return companyListItem;
    }

    public void setCompanyListItem(List<MUserCompanyDTO> companyListItem) {
        this.companyListItem = companyListItem;
    }
}
