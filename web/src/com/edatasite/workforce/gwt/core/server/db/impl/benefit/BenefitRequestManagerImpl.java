package com.edatasite.workforce.gwt.core.server.db.impl.benefit;

import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Djuraev on 8/7/15.
 */
@Repository("benefitRequestManager")
public class BenefitRequestManagerImpl extends BaseManager<EdsBenefitRequest> implements BenefitRequestManager {

    public BenefitRequestManagerImpl() {
        super(EdsBenefitRequest.class);
    }

    @Override
    public ListResult<BenefitRequestItem> getBenefitRequestList(ListingFilterParameter fp) {
        String companyId = getCompanyId();
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct br.id,br.*");
        if (BenefitRequestItem.REQUESTER.equals(fp.getSortField())) {
            sql.append(" ,req.firstname");
        } else if (BenefitRequestItem.APPROVER.equals(fp.getSortField())) {
            sql.append(" ,app.firstname");
        } else if (BenefitRequestItem.BENEFIT_TYPE.equals(fp.getSortField())) {
            sql.append(" ,b.name");
        } else if (BenefitRequestItem.STATUS.equals(fp.getSortField())) {
            sql.append(" ,status.name");
        }

        sql.append(" from " + companyId + ".benefitRequest br ");
        sql.append(" left join " + companyId + ".myuser app on (br.approver=app.id ) ");
        sql.append(" left join " + companyId + ".myuser req on (br.requester=req.id ) ");
        sql.append(" left join " + companyId + ".benefit b on (br.benefit_id=b.id ) ");
        sql.append(" left join " + companyId + ".reference status on (br.status=status.id ) ");
        sql.append("where (br.deleted<>true or br.deleted is null) ");

        if (fp.getEmployeeId() != null) {
            sql.append(" and req.id=" + fp.getEmployeeId() + " ");
        } else if (!ServerUtils.hasPermission(PermissionConstants.APPROVE_REJECT_ALL_BENEFIT_REQUESTS)) {
            sql.append(" and (app.id=" + user.getObjectID() + " or req.id=" + user.getObjectID() + ") ");
        }
        if (fp.getClientId() != null) {
            sql.append(" and req.id=" + fp.getClientId() + " ");
        }
        if (fp.getApproverID() != null) {
            sql.append(" and br.approver = " + fp.getApproverID() + " ");
        }
        if (fp.getName() != null) {
            sql.append(" and b.name = " + "'"+fp.getName()+ "'");
        }
        if (!ServerUtils.isNullOrEmpty(fp.getStatusCode())) {
            sql.append(" and status.code = '").append(fp.getStatusCode()).append("'");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (br.date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() + "')");
        }
      /*  if (fp.getFacetFilter() != null && fp.getFacetFilter().getFacetContentMap().get("status").getFacetItems().length > 0) {
            sql.append(" and status in (");
            for (SelectItem item : fp.getFacetFilter().getFacetContentMap().get("status").getFacetItems()) {
                sql.append("'").append(item.getCode()).append("',");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(") ");
        }*/
        else if (fp.getFacetFilter() != null && fp.getFacetFilter().isApplyFilter()) {
            FacetFilterRpc filterRpc = fp.getFacetFilter();
            HashMap<String, FacetContentRpc> facetContentMap = filterRpc.getFacetContentMap();
            if (facetContentMap.containsKey("status")) {
                if (facetContentMap.get("status") != null && facetContentMap.get("status").getFacetItems().length > 0) {
                    FacetContentRpc parent = facetContentMap.get("status");
                    String statusIds = Arrays.stream(parent.getFacetItems())
                            .map(Object::toString)  // Convert each item to string
                            .collect(Collectors.joining(", "));
                    for (SelectItem item : parent.getFacetItems()) {
                        sql.append(" and status in (").append(statusIds).append(")");
                    }
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(") ");
                }
            }
            if (facetContentMap.containsKey("type")) {
                if (facetContentMap.get("type") != null && facetContentMap.get("type").getFacetItems().length > 0) {
                    FacetContentRpc parent = facetContentMap.get("type");
                    String typeIds = Arrays.stream(parent.getFacetItems())
                            .map(Object::toString)  // Convert each item to string
                            .collect(Collectors.joining(", "));
                    for (SelectItem item : parent.getFacetItems()) {
                        sql.append(" and benefit_id in (").append(typeIds).append(")");
                    }
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(") ");
                }
            }
            if (facetContentMap.containsKey("approver")) {
                if (facetContentMap.get("approver") != null && facetContentMap.get("approver").getFacetItems().length > 0) {
                    FacetContentRpc parent = facetContentMap.get("approver");
                    String approverIds = Arrays.stream(parent.getFacetItems())
                            .map(Object::toString)  // Convert each item to string
                            .collect(Collectors.joining(", "));
                    for (SelectItem item : parent.getFacetItems()) {
                        sql.append(" and approver in (").append(approverIds).append(")");
                    }
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(") ");
                }
            }
            if (facetContentMap.containsKey("requester")) {
                if (facetContentMap.get("requester") != null && facetContentMap.get("requester").getFacetItems().length > 0) {
                    FacetContentRpc parent = facetContentMap.get("requester");
                    String requesterIds = Arrays.stream(parent.getFacetItems())
                            .map(Object::toString)  // Convert each item to string
                            .collect(Collectors.joining(", "));
                    for (SelectItem item : parent.getFacetItems()) {
                        sql.append(" and requester in (").append(requesterIds).append(")");
                    }
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(") ");
                }
            }
        }

        if (!ServerUtils.isNullOrEmpty(fp.getSearchKey())) {
            sql.append(" and (");
            sql.append(" lower(app.firstname) like '%" + fp.getSearchKey().toLowerCase() + "%' ");
            sql.append(" or lower(app.lastname) like '%" + fp.getSearchKey().toLowerCase() + "%' ");
            sql.append(" or lower(req.firstname) like '%" + fp.getSearchKey().toLowerCase() + "%' ");
            sql.append(" or lower(req.lastname) like '%" + fp.getSearchKey().toLowerCase() + "%' ");
            sql.append(" or lower(status.code) like '%" + fp.getSearchKey().toLowerCase() + "%' ");
            sql.append(" or lower(status.name) like '%" + fp.getSearchKey().toLowerCase() + "%' ");
            sql.append(" or lower(b.name) like '%" + fp.getSearchKey().toLowerCase() + "%' ");
            sql.append(") ");
        }

        Integer total = findNative(sql.toString(), EdsBenefitRequest.class).size();

        sql.append(" ORDER BY ");
        if (BenefitRequestItem.DATE.equals(fp.getSortField())) {
            sql.append("br.date");
        } else if (BenefitRequestItem.REQUESTED_QUANTITY.equals(fp.getSortField())) {
            sql.append("br.requestedQuantity");
        } else if (BenefitRequestItem.REQUESTER.equals(fp.getSortField())) {
            sql.append("req.firstname");
        } else if (BenefitRequestItem.APPROVER.equals(fp.getSortField())) {
            sql.append("app.firstname");
        } else if (BenefitRequestItem.BENEFIT_TYPE.equals(fp.getSortField())) {
            sql.append("b.name");
        } else if (BenefitRequestItem.STATUS.equals(fp.getSortField())) {
            sql.append("status.name");
        } else {
            sql.append("br.lastUpdateTime");
        }
        if (fp.isAscending()) {
            sql.append(" desc ");
        }
        if (fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" offset ").append(fp.getStart());
        }

        List<EdsBenefitRequest> result = findNative(sql.toString(), EdsBenefitRequest.class);

        ArrayList<BenefitRequestItem> list = new ArrayList<>();
        for (EdsBenefitRequest request : result) {
            list.add(request.toRequestItem(false));
        }
        return new ListResult<>(list, total);
    }

    public List<EdsBenefitRequest> getBenefitRequestList(EdsEmployee employee) {
        return find("from EdsBenefitRequest br where " +
                " br.requester=?", employee);

    }
    public List<SelectItem> getBenefitRequestTypeList() {
        StringBuilder sql = new StringBuilder();
        sql.append("select id, name, code from ")
                .append(getCompanyId()).append(".benefit ");
        sql.append("where deleted is not true");

        List<Object[]> results = findNative(sql.toString());
        SelectItem selectItem;
        List<SelectItem> benefitList = new ArrayList<>();
        for (Object[] row : results) {
            selectItem = new SelectItem();
            selectItem.setId((Integer) row[0]);
            selectItem.setName((String) row[1]);
            selectItem.setCode((String) row[2]);
            benefitList.add(selectItem);
        }
        return benefitList;

    }
    @Override
    public double getEmployeeUsedBenefitAllowance(Date startYearDate, Date endYearDate, Integer employeeID, Integer benefitID) {
        Object obj = findSingle("select sum(br.requestedQuantity) from EdsBenefitRequest br where (br.deleted<>true or br.deleted is null) and br.requester.objectID=? and br.benefit.objectID=? and (br.date between ? and ?) and br.status.code=?", employeeID, benefitID, startYearDate, endYearDate, EdsBenefitRequest.APPROVED);

        if (obj != null) {
            return (Double) obj;
        } else {
            return 0.00;
        }
    }

    @Override
    public List<EdsBenefitRequest> getBenefitRequestForPayment(ListingFilterParameter lfp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("select br.* from ").append(getCompanyId()).append(".benefitRequest br").append("\n");
        sql.append("left join ").append(getCompanyId()).append(".benefit b on b.id=br.benefit_id ").append("\n");
        sql.append("left join ").append(getCompanyId()).append(".reference ref on ref.id=br.status ").append("\n");
        sql.append("left join ").append(getCompanyId()).append(".reference bref on bref.id=b.qtytype_id ").append("\n");
        sql.append("where ").append(ServerUtils.checkForDeleted("br.deleted")).append(" and br.requester=").append(lfp.getEmployeeId()).append(" and ref.code='BR_APPROVED'").append(" and bref.code='CURRENCY'");
        if (lfp.getStartDate() != null && lfp.getEndDate() != null) {
            sql.append("AND to_date(to_char(br.date, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(lfp.getStartDate())).append("' AND '").append(dateFormat.format(lfp.getEndDate())).append("' ");
        }
        return findNative(sql.toString(), EdsBenefitRequest.class);
    }
}
