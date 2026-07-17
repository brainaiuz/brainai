package com.finnetlimited.reportservice.core.server.db.dao.schema;


import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.finnetlimited.reportservice.core.server.db.schema.CustomHtmlManager;
import com.finnetlimited.reportservice.core.server.db.schema.ReportingManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsCustomHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository()

public class CustomHTMLManagerImpl  extends BaseManager<EdsCustomHtml> implements CustomHtmlManager, PermissionConstants {
    //    @PersistenceContext(unitName = "masterPersistenceUnit")
//    protected EntityManager masterEntityManager;
    @Autowired
    private ReportingManager reportingManager;



    public CustomHTMLManagerImpl() {
        super(EdsCustomHtml.class);
    }
    public static void save(EdsCustomHtml edsCustomHtml) {
    }

    @Override
    public EdsCustomHtml getCustomHtmlByReportId(Integer id) {
        return (EdsCustomHtml) findNativeSingle("Select * from "+getCompanyId()+".customhtmlcode where edsreport_id="+id+" ",EdsCustomHtml.class);

    }


//        public  String create( String text, Integer reportId) {
//
////            edsCustomHtml.setHtmlCode("");
////            manager.crete();
////            serpor.sEtCH(edsCustomHtml.getObjectID());
////
//
//
////            masterEntityManager.createQuery("INSERT INTO "+ getCompanyId() +"customhtmlcode (htmlCode, edsReport_id) VALUES ("+text+", "+reportId+");\n");
//          return "creted";
//        }


}
