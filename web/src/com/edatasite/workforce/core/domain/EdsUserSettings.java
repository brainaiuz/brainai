package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 17.04.2018
 * Time: 15:02:05
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "user_settings")
public class EdsUserSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private EdsUser user;

    @Column(name = "group_type")
    @Enumerated(EnumType.STRING)
    private UserSettingsTypeEnum type;

    private String key;

    @Column(name = "value")
    private String value;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public UserSettingsTypeEnum getType() {
        return type;
    }

    public void setType(UserSettingsTypeEnum type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
