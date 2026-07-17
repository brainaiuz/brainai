package com.edatasite.workforce.gwt.backend.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.website.WebsiteImportingItem;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Virus on 3/29/2014.
 */
public class WorkspaceImportExporter {
    static final String IMPORT_WORKSPACE_DASHBOART(String company, Integer parentId) {
        return "insert into \"" + company + "\".dashboard(name,columncount,centercolumntitle,code,issystem) " +
                " select name,columncount,centercolumntitle,code,issystem from \"" + parentId + "\".dashboard where issystem=true and code not in (select code from \"" + company + "\".dashboard ) limit 1;";
    }

    static final String IMPORT_WORKSPACE_DASHLET(String company, Integer parentId) {
        return "insert into \"" + company + "\".dashlet(code,columnindex,verticalposition,dashlettype,dashboardid,isSystem,reportcode) " +
                " select code,columnindex,verticalposition,dashlettype,db.id,isSystem,reportcode from \"" + parentId + "\".dashlet d, (select id from \"" + company + "\".dashboard where issystem=true limit 1) db " +
                " where code not in (select code from \"" + company + "\".dashlet);";
    }

    static final String IMPORT_WORKSPACE_DASHLET(String company, Integer parentId, String IDs) {
        return "insert into \"" + company + "\".dashlet(code,columnindex,verticalposition,dashlettype,dashboardid,isSystem,reportcode) " +
                " select code,columnindex,verticalposition,dashlettype,db.id,isSystem,reportcode from \"" + parentId + "\".dashlet d, (select id from \"" + company + "\".dashboard where issystem=true limit 1) db " +
                " where d.id in (" + IDs + ") and code not in (select code from \"" + company + "\".dashlet);";
    }

    static final String IMPORT_WORKSPACE_PERMISSION() {
        return "select distinct concat('insert into permission (code,context,name,sorder,ismainmenu,parent,iscore) select ''',p.code,''' code,''',p.context,''',''',p.name,''',',p.sorder,',''',coalesce(p.ismainmenu,false),''',coalesce((select id from permission where code=',coalesce(p2.code,'''###'''),'),0),''',p.iscore,''' from (select count(id) c from permission where code =''',p.code,''') p2 WHERE p2.c=0; ') \n" +
                "from permission p left join permission p2 on p.parent=p.id where p.context='WORKSPACE'";
    }

    static final String IMPORT_WORKSPACE_ROLEPERMISSION(String companyID) {
        return " select 0 id,'insert into \"" + companyID + "\".rolepermission(permissioncode,rolecode,access) select * from (' union\n" +
                " select distinct 1,concat('select ''',rp.permissioncode,''' permissioncode,''',rp.rolecode,''',''',access,''' union ' ) from \"" + companyID + "\".rolepermission rp join permission p on p.code=rp.permissioncode and p.context='WORKSPACE'\n" +
                " union select 2,'select null,null,null '\n" +
                " union select 3,') a where permissioncode is not null  and a.permissioncode not in (select permissioncode from \"" + companyID + "\".rolepermission)'\n" +
                " order by id";
    }

    private CoreService reportingService;
    //private WebsiteServiceLocal websiteService;


    public void openWorkspace(Integer companyID, ListingFilterParameter filterParameter) throws IOException {
        Integer parentId = filterParameter.getParentID();
        URL templateUrl = new URL(filterParameter.getURL());
        InputStream inputStream = templateUrl.openStream();
        try {
            System.out.println("-------------------------------Begin Import Workspase company_id=" + companyID + "--------------------------------");
            importDashlet(String.valueOf(companyID), parentId);
            importWebsite(inputStream, String.valueOf(companyID), parentId);
            inputStream.close();
        } catch (Exception e) {
            inputStream.close();
            e.getStackTrace();
        }
    }

    private void importDashlet(String company, Integer parentId) {
        System.out.println("-------------------------Begin Dashlet Patch !--------------------------");
        reportingService.executeNative(IMPORT_WORKSPACE_DASHBOART(company, parentId));
        reportingService.executeNative(IMPORT_WORKSPACE_DASHLET(company, parentId));
        System.out.println("-------------------------Dashlet Patch Completed !--------------------------");
    }

    private void importWebsite(InputStream inputStream, String company, Integer parentId) {
        WebsiteImportingItem website = new WebsiteImportingItem();
        website.setName("kpi Default Website");
        website.setCompanyID(company);
        website.setKpiDefaultWebsite(true);
        //websiteService.importWebsite(inputStream, website);
        reportingService.executeNative("update companySystemSettings set isShowDraggableWorkspace=true where companyid=" + company);
        System.out.println("----------------------------Successful Imported Workspase company_id=" + company + " !!!---------------------------------");
    }

    public void importPermission(String dataBase) {
        System.out.println("-------------------------Begin Permission Patch !--------------------------");
        String currentDatabase = ServerSecurityContext.getInstance().getDatabase();
        if (!currentDatabase.equals(dataBase)) {
            try {
                ServerSecurityContext.getInstance().setDatabase(dataBase);
                ArrayList<String[]> queryList = reportingService.getDataTable(IMPORT_WORKSPACE_PERMISSION());
                StringBuilder queryBuilder = new StringBuilder(100);
                for (String[] queries : queryList) {
                    if (queries.length > 0 && queries[0] != null) {
                        queryBuilder.append(queries[0]);
                    }
                }
                ServerSecurityContext.getInstance().setDatabase(currentDatabase);
                if (!queryBuilder.toString().trim().isEmpty()) {
                    reportingService.executeNative(queryBuilder.toString());
                }
            } catch (Exception e) {
                ServerSecurityContext.getInstance().setDatabase(currentDatabase);
            } finally {
                ServerSecurityContext.getInstance().setDatabase(currentDatabase);
            }
            System.out.println("-------------------------Permisson Patch Completed !--------------------------");
        }
    }

    public void importRolePermission(String companyID, String parentID, String database) {
        System.out.println("-------------------------Begin Permission Patch !--------------------------");
        String currentDatabase = ServerSecurityContext.getInstance().getDatabase();
        if (!currentDatabase.equals(database)) {
            try {
                ServerSecurityContext.getInstance().setDatabase(database);
                ArrayList<String[]> queryList = reportingService.getDataTable(IMPORT_WORKSPACE_ROLEPERMISSION(parentID));
                StringBuilder queryBuilder = new StringBuilder(100);
                if (!(queryList == null || queryList.isEmpty())) {
                    for (String[] queries : queryList) {
                        if (queries != null && queries.length > 1) {
                            queryBuilder.append(queries[1]);
                        }
                    }
                }
                ServerSecurityContext.getInstance().setDatabase(currentDatabase);
                if (!queryBuilder.toString().trim().isEmpty()) {
                    reportingService.executeNative(queryBuilder.toString());
                }
                System.out.println("-------------------------Permisson Patch Completed !--------------------------");
            } catch (Exception e) {
                ServerSecurityContext.getInstance().setDatabase(currentDatabase);
            } finally {
                ServerSecurityContext.getInstance().setDatabase(currentDatabase);
            }
        }
    }

    public void exportDashlet(ListingFilterParameter filterParameter, Integer parent, String IDs) {
        //Export from map to existing schema
        for (Integer compaiyID : filterParameter.getCompaines()) {
            try {
                if (compaiyID.equals(parent)) {
                    continue;
                }
                try {
                    reportingService.executeNative(IMPORT_WORKSPACE_DASHLET(String.valueOf(compaiyID), parent, IDs));
                    System.out.println(">>>>>>>>>>> Export Dashlet to " + compaiyID);
                } catch (Exception ignored) {
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void setReportingService(CoreService reportingService) {
        this.reportingService = reportingService;
    }

    /*public void setWebsiteService(WebsiteServiceLocal websiteService) {
        this.websiteService = websiteService;
    }*/
}
