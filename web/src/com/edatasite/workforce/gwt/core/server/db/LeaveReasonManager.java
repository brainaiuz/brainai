package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hurshid on 12/15/2018
 */
public interface LeaveReasonManager extends Manager<EdsLeaveReason> {

    ArrayList<EdsLeaveReason> listLeaveReasons(ListingFilterParameter fp);

    int countReasons(ListingFilterParameter filterParameter);

    EdsLeaveReason getReasonByName(String name, String code);

    EdsLeaveReason findByCode(String code);

    EdsLeaveReason getReasonByShortName(String shortName);

    List<EdsLeaveReason> listActiveReasons();

    List<EdsLeaveReason> listActiveReasonsForAR();

    List<EdsLeaveReason> listActiveReasonsByGender(String gender);
    List<EdsLeaveReason> listActiveReasonsByLimit(String gender, String searchText,Integer limit);

    List<EdsLeaveReason> listActiveReasonsByGenderWithoutDrafts(String gender);

    SelectItem[] getAttendanceLRReasons(boolean b);

    List<EdsLeaveReason> getLRHolidayIncludedReasons();
}
