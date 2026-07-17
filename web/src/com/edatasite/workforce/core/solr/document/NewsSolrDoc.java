package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
@SolrDocument(collection = "newsCore")
public class NewsSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("newsId")
    @Indexed(name = "newsId", type = "pint", required = true)
    private Integer newsId;

    @Field("subject")
    private String subject;

    @Field("subjectComposite")
    private String subjectComposite;

    @Field("composite")
    private String composite;

    @Field("date")
    private Date date;

    @Field("creationDate")
    private Date creationDate;

    @Field("fullText")
    private String fullText;

    @Field("user")
    private String user;

    @Field("userId")
    @Indexed(name = "userId", type = "pint", stored = false)
    private Integer userId;

    @Field("userIdName")
    @Indexed(name = "userIdName", type = "string", stored = false)
    private String userIdName;

    @Field("fieldNewsVisibility")
    private Boolean fieldNewsVisibility;

    @Field("fieldNewsIsGeneral")
    private Boolean fieldNewsIsGeneral;

    @Field("fieldIsBlog")
    private Boolean fieldIsBlog;

    @Field("fieldCategoryId")
    @Indexed(name = "fieldCategoryId", type = "pints")
    private List<Integer> fieldCategoryId = new ArrayList<>();

    @Field("fieldCategoryName")
    @Indexed(name = "fieldCategoryName", type = "strings")
    private List<String> fieldCategoryName = new ArrayList<>();

    @Field("fieldNewsType")
    private String fieldNewsType;

    @Field("fieldNewsOwner")
    private String fieldNewsOwner;

    @Field("fieldLocation")
    private String fieldLocation;

    @Field("fieldLocationId")
    private Integer fieldLocationId;

    @Field("fieldLocationIdName")
    @Indexed(name = "fieldLocationIdName", type = "string", stored = false)
    private String fieldLocationIdName;

    @Field("fieldComments")
    private Integer fieldComments;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectComposite() {
        return subjectComposite;
    }

    public void setSubjectComposite(String subjectComposite) {
        this.subjectComposite = subjectComposite;
    }

    public String getComposite() {
        return composite;
    }

    public void setComposite(String composite) {
        this.composite = composite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserIdName() {
        return userIdName;
    }

    public void setUserIdName(String userIdName) {
        this.userIdName = userIdName;
    }

    public boolean isFieldNewsVisibility() {
        return fieldNewsVisibility;
    }

    public void setFieldNewsVisibility(boolean fieldNewsVisibility) {
        this.fieldNewsVisibility = fieldNewsVisibility;
    }

    public boolean isFieldNewsIsGeneral() {
        return fieldNewsIsGeneral;
    }

    public void setFieldNewsIsGeneral(boolean fieldNewsIsGeneral) {
        this.fieldNewsIsGeneral = fieldNewsIsGeneral;
    }

    public boolean isFieldIsBlog() {
        return fieldIsBlog;
    }

    public void setFieldIsBlog(boolean fieldIsBlog) {
        this.fieldIsBlog = fieldIsBlog;
    }

    public List<Integer> getFieldCategoryId() {
        return fieldCategoryId;
    }

    public void setFieldCategoryId(List<Integer> fieldCategoryId) {
        this.fieldCategoryId = fieldCategoryId;
    }

    public List<String> getFieldCategoryName() {
        return fieldCategoryName;
    }

    public void setFieldCategoryName(List<String> fieldCategoryName) {
        this.fieldCategoryName = fieldCategoryName;
    }

    public String getFieldNewsType() {
        return fieldNewsType;
    }

    public void setFieldNewsType(String fieldNewsType) {
        this.fieldNewsType = fieldNewsType;
    }

    public String getFieldNewsOwner() {
        return fieldNewsOwner;
    }

    public void setFieldNewsOwner(String fieldNewsOwner) {
        this.fieldNewsOwner = fieldNewsOwner;
    }

    public String getFieldLocation() {
        return fieldLocation;
    }

    public void setFieldLocation(String fieldLocation) {
        this.fieldLocation = fieldLocation;
    }

    public Integer getFieldLocationId() {
        return fieldLocationId;
    }

    public void setFieldLocationId(Integer fieldLocationId) {
        this.fieldLocationId = fieldLocationId;
    }

    public String getFieldLocationIdName() {
        return fieldLocationIdName;
    }

    public void setFieldLocationIdName(String fieldLocationIdName) {
        this.fieldLocationIdName = fieldLocationIdName;
    }

    public Integer getFieldComments() {
        return fieldComments;
    }

    public void setFieldComments(Integer fieldComments) {
        this.fieldComments = fieldComments;
    }
}
