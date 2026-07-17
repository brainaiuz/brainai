package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/22/11
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoreMenuUpdateItem implements IsSerializable {

    private String companyName;
    private SelectItem[] moreMenuItems;
	private boolean enableWFTMoreMenuForMEM = true;
	private boolean enableWFTMoreMenuForADMIN = true;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public SelectItem[] getMoreMenuItems() {
        return moreMenuItems;
    }

    public void setMoreMenuItems(SelectItem[] moreMenuItems) {
        this.moreMenuItems = moreMenuItems;
    }

	public boolean isEnableWFTMoreMenuForMEM() {
		return enableWFTMoreMenuForMEM;
	}

	public void setEnableWFTMoreMenuForMEM(boolean enableWFTMoreMenuForMEM) {
		this.enableWFTMoreMenuForMEM = enableWFTMoreMenuForMEM;
	}

	public boolean isEnableWFTMoreMenuForADMIN() {
		return enableWFTMoreMenuForADMIN;
	}

	public void setEnableWFTMoreMenuForADMIN(boolean enableWFTMoreMenuForADMIN) {
		this.enableWFTMoreMenuForADMIN = enableWFTMoreMenuForADMIN;
	}
}