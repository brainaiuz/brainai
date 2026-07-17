package com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.impl;

import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailCustomRepository;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.ArrayList;
import java.util.List;

public class EmailRepositoryImpl implements EmailCustomRepository {

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private CaseManager caseManager;

    public Long getLastFetchedMessagesUID(Integer folderId) {
        BasicQuery query = new BasicQuery("{ 'companyId': '" + SecurityContext.getCompanyID() + "', 'folderId': " + folderId + ", 'messageUID': {'$ne' : null} }", "{ 'messageUID': 1 }");
        query.setSortObject(Document.parse("{ 'messageUID': -1 }"));
        EdsEmail email = mongoTemplate.findOne(query, EdsEmail.class);
        return email != null ? email.getMessageUID() : 0;
    }

    public EdsEmail findLastByTrackerId(Integer trackerId) {
        BasicQuery query = new BasicQuery("{ 'companyId': '" + SecurityContext.getCompanyID() + "', 'trackerId': " + trackerId + ", 'folderType': 'INBOX' }");
        query.setSortObject(Document.parse("{ 'messageUID': -1 }"));
        return mongoTemplate.findOne(query, EdsEmail.class);
    }

    @Override
    public List<EdsEmail> getEmailList(ListingFilterParameter fp) {
        int page = fp.getStart() / fp.getLimit();
        Pageable pageable = PageRequest.of(page, fp.getLimit());
        BasicQuery query = new BasicQuery(getEmailListQuery(fp));
        query.setSortObject(Document.parse(getSortQuery(fp)));
        query.with(pageable);
        return mongoTemplate.find(query, EdsEmail.class);
    }

    @Override
    public Integer getEmailCount(ListingFilterParameter fp) {
        BasicQuery query = new BasicQuery(getEmailListQuery(fp));
        return Long.valueOf(mongoTemplate.count(query, EdsEmail.class)).intValue();
    }

    private String getEmailListQuery(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("{ 'deleted': false, 'companyId': '").append(SecurityContext.getCompanyID()).append("'");

        if (!fp.isShowActive()) {
            if (fp.getObjectId() != null) {
                sql.append(", '$or': [ { 'emailSettingId': ").append(fp.getObjectId()).append("}, { 'emailSettingId': null } ], 'fetched': 'false' ");
            }
            if (fp.getEmailFolderID() != null && fp.getEmailFolderID() > 0) {
                sql.append(", 'folderId': ").append(fp.getEmailFolderID()).append(" ");
            }
        }

        //This is for email right sidebar and top email notification
        if (fp.getParams() != null) {
            if (fp.getObjectId() != null) {
                sql.append(", 'emailSettingId': ").append(fp.getObjectId()).append(" ");
            }
            if (Constants.FLAG_UNREAD.equals(fp.getParams())) {
                sql.append(", 'read': false");
            } else if (Constants.FLAG_READ.equals(fp.getParams())) {
                sql.append(", 'read': true");
            }
        }
        //related email buyicha olib chiqish
        boolean doNotUseTracker = false;
        if (!StringUtils.isEmpty(fp.getLookUpBy()) && !StringUtils.isEmpty(fp.getEmail())) {
            String[] emails = fp.getEmail().split(",");
            if (emails.length > 0) {
                sql.append(", '$or': [ ");
                for (String email : emails) {
                    if (email != null && !"".equals(email)) {
                        sql.append(" { 'to': '").append(email).append("' }, ");
                        sql.append(" { 'from': '").append(email).append("' }, ");
                    }
                }
                doNotUseTracker = true;
            }
        }
        if (fp.getRelationType() != null && fp.getRelationID() != null && fp.getTrackerID() == null) {
            if (RelationItem.TYPE_CASE.equals(fp.getRelationType())) {
                EdsCase edsCase = caseManager.get(fp.getRelationID());
                if (edsCase != null && edsCase.getTracker() != null) {
                    fp.setTrackerID(edsCase.getTracker().getObjectID());
                }
            }
        }

        if (fp.getRelationID() != null && fp.getRelationType() != null) {
            List<Integer> trackerIDs = caseManager.findNative(EdsRelation.getTrackerIDsByRelationQuery(fp.getRelationID(), fp.getRelationType(), RelationItem.TYPE_EMAIL_TRACKER));
            if (trackerIDs == null) {
                trackerIDs = new ArrayList<>();
            }
            if (fp.getTrackerID() != null && !trackerIDs.contains(fp.getTrackerID())) {
                trackerIDs.add(fp.getTrackerID());
            }
            sql.append(", 'trackerId': { '$in': [ ").append(ServerUtils.getAsCommoDelimited(trackerIDs, "0", ",")).append(" ] } ");
        } else if (fp.getTrackerID() != null) {
            sql.append(", 'trackerId': ").append(fp.getTrackerID()).append(" ");
        }
        if (doNotUseTracker) {
            sql.append(" ] ");
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(", '$or': [ ");
            sql.append((" { 'subject': { '$regex': '")).append(fp.getSearchKey().trim()).append("', '$options': 'i' } } ");
            sql.append((", { 'from': { '$regex': '")).append(fp.getSearchKey().trim()).append("', '$options': 'i' } } ");
            if (!fp.isBriefly()) {
                sql.append((", { 'description': { '$regex': '")).append(fp.getSearchKey().trim()).append("', '$options': 'i' } } ");
            }
            sql.append((", { 'to': { '$regex': '")).append(fp.getSearchKey().trim()).append("', '$options': 'i' } } ] ");
        }
        sql.append("}");

        return sql.toString();
    }

    private String getSortQuery(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();

        if (fp.getSortField() != null) {
            int order = fp.isAscending() ? 1 : -1;
            if (Email.SUBJECT.equals(fp.getSortField())) {
                sql.append(" { 'subject': ").append(order).append(" } ");
            } else if (Email.TO_EMAIL.equals(fp.getSortField())) {
                sql.append(" { 'from': ").append(order).append(" } ");
            } else if (Email.ID.equals(fp.getSortField())) {
                sql.append(" { 'id': ").append(order).append(" } ");
            } else if (Email.FROM_EMAIL.equals(fp.getSortField())) {
                sql.append(" { 'to': ").append(order).append(" } ");
            } else if (Email.CREATED_DATE.equals(fp.getSortField())) {
                sql.append(" { 'createdDate': ").append(order).append(" } ");
            } else if (Email.FETCHED_DATE.equals(fp.getSortField())) {
                sql.append(" { 'fetchedDate': ").append(order).append(" } ");
            } else {
                sql.append(" { 'createdDate': ").append(order).append(" } ");
            }
        } else {
            sql.append("{ 'createdDate': -1 }");
        }

        return sql.toString();
    }
}
