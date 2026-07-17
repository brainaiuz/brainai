package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Iskandar
 * Date: 09-Aug-2007
 * Time: 07:21:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "signup_message")
public class EdsSignupMessage extends EdsSuperMessage {

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "signup_message_upload",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "fileids_id")}
    )
    @org.hibernate.annotations.ForeignKey(name = "none")
    List<EdsUpload> fileIDs = new ArrayList<>();

    @Override
    public List<EdsUpload> getFileIDs() {
        return fileIDs;
    }

    @Override
    public void setFileIDs(List<EdsUpload> fileIDs) {
        this.fileIDs = fileIDs;
    }
}
