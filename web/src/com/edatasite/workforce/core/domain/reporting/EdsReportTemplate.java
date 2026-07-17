package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.server.rpc.RpcMap;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 20.10.11
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "reportTemplate", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class EdsReportTemplate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @Index(name = "reporttemplate_index_categorycode")
    private String categoryCode;

    @Column(name = "body")
    @Type(type = "text")
    private String body;

    private Boolean isCustom = false;
    private Boolean isLibrary = false;
    private Boolean isSimplified = false;
    private Boolean useInBackup = false;

    @Index(name = "reporttemplate_index_code")
    @Column(unique = true)
    private String code;
    private Integer order_number;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "reportCode", referencedColumnName = "code")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<EdsCustomReportTemplate> customReportTemplates = new HashSet<>();

    private String permissionCode;
    private Boolean synchronization;
    @Embedded
    private EdsAuditInfo auditInfo;

    private Integer stepId;
    private Integer attachmentId;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public boolean getLibrary() {
        return Boolean.TRUE.equals(isLibrary);
    }

    public void setLibrary(Boolean library) {
        isLibrary = library;
    }

    public Boolean getSimlified() {
        return isSimplified;
    }

    public void setSimlified(Boolean simlified) {
        isSimplified = simlified;
    }

    public void setCode() {
        code = name.replaceAll("[^\\p{L}\\p{Nd}]|[\\p{InLatin-1Supplement}]+", "").toUpperCase();
    }

    public String getCode() {
        return code;
    }

    public Integer getOrder_number() {
        return order_number;
    }

    public void setOrder_number(Integer order_number) {
        this.order_number = order_number;
    }

    public String generateInsertCommand() {
        return " INSERT INTO reportTemplate(name,categoryID,body,isCustom,isLibrary,code,order_number,isSimplified) " +
                "select E'" + getName().replace("'", "''") + "',id,E'" + getBody().replace("'", "''") + "'," + getCustom() + "," + getLibrary() + ",'" + getCode().replace("'", "''") + "'," + getOrder_number() + "," + getSimlified() +
                " from reportTemplateCategory where code=E'" + getCategoryCode().replace("'", "''") + "'; \n";
    }

    public Set<EdsCustomReportTemplate> getCustomReportTemplates() {
        return customReportTemplates;
    }

    public void setCustomReportTemplates(Set<EdsCustomReportTemplate> customReportTemplates) {
        this.customReportTemplates = customReportTemplates;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public List<Integer> getCustomCompany() {
        List<Integer> list = new ArrayList<>();
        for (EdsCustomReportTemplate template : getCustomReportTemplates()) {
            list.add(template.getCompanyId());
        }
        return list;
    }

    public EdsAuditInfo getAuditInfo() {
        if (auditInfo == null) {
            auditInfo = new EdsAuditInfo();
        }
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public Boolean getSynchronization() {
        return synchronization;
    }

    public void setSynchronization(Boolean synchronization) {
        this.synchronization = synchronization;
    }

    public RpcMap getMap() {
        try {
            RpcMap rpcMap = RpcMap.get(this);
            rpcMap.getInstance().put("tempReportTemplateCategory", "" + this.categoryCode);
            return rpcMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Integer getStepId() {
        return stepId;
    }

    public Boolean getUseInBackup() {
        return useInBackup;
    }

    public void setUseInBackup(Boolean useInBackup) {
        this.useInBackup = useInBackup;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }
}
