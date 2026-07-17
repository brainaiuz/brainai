package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Updated: AzizH
 * Date: 15.11.2017
 * Created: Sherzod
 * Date: 5/26/11
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "genericSettings")
public class EdsGenericSettings extends EdsObject {
    public static final String REQUESTED = "REQUESTED";
    public static final String YES = "YES";
    public static final String NO = "NO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private GenericSettingsEnum key;

    @Type(type = "text")
    @Column(name = "value")
    private String value;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public GenericSettingsEnum getKey() {
        return key;
    }

    public void setKey(GenericSettingsEnum key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
