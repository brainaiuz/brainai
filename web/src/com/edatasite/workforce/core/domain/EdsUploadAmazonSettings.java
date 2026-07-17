package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Acer
 * Date: 24-Mar-2008
 * Time: 21:57:23
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "uploadamazonsettings",
        indexes = {
                @Index(columnList = "uploadid", name = "uploadamazonsettings_uploadid_index")
        })
public class EdsUploadAmazonSettings extends EdsUploadSettings {
}
