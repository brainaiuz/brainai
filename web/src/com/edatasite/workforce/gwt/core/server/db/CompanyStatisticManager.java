package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompanyStatistic;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;

import java.util.Date;
import java.util.List;

public interface CompanyStatisticManager extends Manager<EdsCompanyStatistic> {

    List<EdsCompanyStatistic> getAllCompanyStatistic();

    List<Object[]> getCompanyStatistic();

    void clearTable();

    List<Object[]> getCompanyStatistics(ListingFilterParameter searchKey, Boolean isCount, Integer viewAsFilter, Integer backendUsersId);

    List<EdsCompanyStatistic> searchCompanies(String searchKey, ListLoadConfig config);

    Object getOverallStatistics();

    Long getSignUpsCountByDateLimit(Date sdate, Date edate);

    Long getActivatedSignupersCountByDateLimit(Date sdate, Date edate);

    Long getSystemUsedUsersCountByDateLimit(Date sdate, Date edate);

    Long getBouncedUsersCountByDateLimit(Date sdate, Date edate);

    Long getInactiveSignupersCountByDateLimit(Date sdate, Date edate);

    Long getLastUpdationCount(Date sdate, Date edate);

    List<Object[]> getCountriesByDate(Date sdate, Date edate);

    List<Object[]> getIndustriesByDate(Date sdate, Date edate);

    Object getSignuppersRate(Date sdate, Date edate);

    void deleteByCompanyID(Integer companyID);

    EdsCompanyStatistic getStatisticByCompanyID(Integer companyID);

    List<Object[]> getCompanyStatistics(Integer companyID);

    List<CompanyListItem> getWeeklySubscriptions(Date start, Date end);

    List<Object[]> getLimitedSubscriptions(ListingFilterParameter fp, Date start, Date end, Integer limit);

    Object[] getCompany(Integer companyID);

    ListResult<CompanyListItem> getCompanyStatisticList(ListingFilterParameter fp);

    List<Object[]> getnotLoggedCompaniesSinceMonthYear();


}
