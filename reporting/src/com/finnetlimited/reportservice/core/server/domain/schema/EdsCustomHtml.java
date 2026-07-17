package com.finnetlimited.reportservice.core.server.domain.schema;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA,name = "customhtmlcode")

public class EdsCustomHtml extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "htmlCode", length = 99000)
    private String htmlCode;
    @OneToOne
    private EdsReport edsReport;



    public EdsReport getEdsReport() {
        return edsReport;
    }

    public void setEdsReport(EdsReport edsReport) {
        this.edsReport = edsReport;
    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }
}
