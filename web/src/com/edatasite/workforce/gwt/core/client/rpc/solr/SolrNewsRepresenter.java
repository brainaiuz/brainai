package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sher
 * Date: 04.08.2010
 * Time: 19:57:21
 * To change this template use File | Settings | File Templates.
 */
public class SolrNewsRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_NEWS_ID = "newsId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_SUBJECT = "subject";
    public static final String FIELD_SUBJECT_COMPOSITE = "subjectComposite";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_FULL_TEXT = "fullText";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_USER = "user";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_USER_ID_NAME = "userIdName";
    public static final String FIELD_NEWS_VISIBILITY = "fieldNewsVisibility";
    public static final String FIELD_NEWS_IS_GENERAL = "fieldNewsIsGeneral";
    public static final String FIELD_NEWS_TYPE = "fieldNewsType";
    public static final String FIELD_IS_BLOG = "fieldIsBlog";
    public static final String FIELD_CATEGORY_ID = "fieldCategoryId";
    public static final String FIELD_CATEGORY_NAME = "fieldCategoryName";
    public static final String FIELD_NEWS_OWNER = "fieldNewsOwner";
    public static final String FIELD_LOCATION_ID = "fieldLocationId";
    public static final String FIELD_LOCATION = "fieldLocation";
    public static final String FIELD_LOCATION_ID_NAME = "fieldLocationIdName";
    public static final String FIELD_COMMENTS = "fieldComments";

    //Solr Sortable Columns
    public static final String SORTABLE_SUBJECT = "sortableSubject";
    public static final String SORTABLE_USER = "sortableUser";
    public static final String SORTABLE_CATEGORY = "sortableCategory";
    public static final String SORTABLE_LOCATION = "sortableLocation";
    public static final String SORTABLE_COMMENTS = "sortableComments";
}
