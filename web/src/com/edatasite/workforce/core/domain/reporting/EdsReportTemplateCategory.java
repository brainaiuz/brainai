package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 20.10.11
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "reportTemplateCategory")
public class EdsReportTemplateCategory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Index(name = "folders_index_code")
    @Column(unique = true)
    private String code;

    private String name;
    private Integer sorder = 100;

    private String moduleCode;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (getObjectID() == null) {
            code = name.replaceAll("[^\\p{L}\\p{Nd}]|[\\p{InLatin-1Supplement}]+", "").toUpperCase();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }


    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
}
