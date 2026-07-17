package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "uploadminiosettings",
        indexes = {
                @Index(columnList = "uploadid", name = "uploadminiosettings_uploadid_index")
        })
public class EdsUploadMinIOSettings extends EdsUploadSettings {
}
