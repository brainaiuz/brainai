package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRecurrenceHistory;
import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 29.03.11
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */

public interface RecurrenceHistoryManager extends Manager<EdsRecurrenceHistory>{

    EdsRecurrenceHistory getRecurrenceHistory(String jobName, Date fireTime);

    Long getLateRecurrencesInThisSeries(EdsServerHistory serverHistory);

    List<EdsRecurrenceHistory> getLateRecurrences();

    List<EdsRecurrenceHistory> list(ListingFilterParameter filterParametrs);

    EdsRecurrenceHistory getRecurrenceHistory(Integer recurrenceID, String date);
}
