package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 15.01.2009
 * Time: 11:48:09
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "googledocumentssettings")
public class EdsSinxDocumentsSettings extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "documentLink")
    private String documentLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "googledocumentsid")
    private EdsSinxDocuments sinxDocuments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploadid")
    private EdsUpload upload;
    private String documentID;
    private String documentOpenID;
    private String downloadLink;

    public Integer getObjectID() {
        return objectID;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public EdsSinxDocuments getSinxDocuments() {
        return sinxDocuments;
    }

    public void setSinxDocuments(EdsSinxDocuments googleDocuments) {
        this.sinxDocuments = googleDocuments;
    }

    public EdsUpload getUpload() {
        return upload;
    }

    public void setUpload(EdsUpload upload) {
        this.upload = upload;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public String getDocumentOpenID() {
        return documentOpenID;
    }

    public void setDocumentOpenID(String documentOpenID) {
        this.documentOpenID = documentOpenID;
    }


    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
