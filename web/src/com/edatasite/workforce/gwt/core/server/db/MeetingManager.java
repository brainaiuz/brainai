package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: developer
 * Date: 4/20/12
 * Time: 7:11 PM
 */
public interface MeetingManager extends Manager<EdsMeetingMinutes> {
    List<EdsMeetingMinutes> getMeetingMinutesList(ListingFilterParameter filterParameter);

    List<EdsMeetingMinutes> getMeetingMinutesById(Integer projectId);

    List<EdsMeetingMinutes> getMeetingMinutesList();

    Integer getProductLastIntNumber();

    boolean isProductNumberExists(String number, Integer productID);
}