package com.finnetlimited.reportservice.core.server.db.dao.schema;

import com.edatasite.workforce.core.domain.reporting.EdsReportTemplateCategory;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.finnetlimited.reportservice.core.server.db.schema.FoldersManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsFolders;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: ${Dilsh0d}
 * Date: 06-Mar-2010
 * Time: 16:58:49
 */
@Repository("foldersManager")
public final class FoldersManagerImpl extends BaseManager<EdsFolders> implements FoldersManager {
    public FoldersManagerImpl() {
        super(EdsFolders.class);
    }

    public ArrayList<EdsFolders> list(String domainName, Integer companyId, Integer userId) {
        String sql = "SELECT f.* FROM " + getCompanyId() + ".folders f " +
                " left join " + getPublic() + ".reportTemplateCategory c on f.categoryCode=c.code " +
                "  join " + getCompanyId() + ".mymodule md on c.moduleCode = md.code " +
                " WHERE (f.type='" + FolderType.System.name() + "' OR f.type='" + FolderType.Public.name() +"' " +
                "        OR ( f.type='" + FolderType.Private.name() + "' and f.userid=" + userId + ") " +
                " ) " +
                " AND f.deleted is not true " +
                " AND md.active = true " +
                " ORDER BY c.sorder, f.id DESC ";
        return (ArrayList<EdsFolders>) findNative(sql, EdsFolders.class);
    }

    public ArrayList<EdsFolders> search(String search, String domainName, Integer companyId, Integer userId) {
        return (ArrayList<EdsFolders>) findNative("SELECT f.* FROM " + getCompanyId() + ".folders f  left join " + getPublic() + ".reportTemplateCategory c on f.categoryCode=c.code WHERE " +
                " ( " +
                "(f.domainName='" + domainName + "' AND f.type='" + FolderType.System.name() + "') OR " +
                "(f.companyid=" + companyId + " AND f.type='" + FolderType.Public.name() +
                "') OR (f.companyid=" + companyId + " AND f.userid=" + userId + " AND f.type='" + FolderType.Private.name() + "') " +
                " ) AND (f.deleted is null OR f.deleted <> true) " + "AND (lower(f.name) like '%" + search.toLowerCase() + "%')" +
                " ORDER BY c.sorder, f.id DESC", EdsFolders.class);
    }

    public boolean isFolderYes(FolderRpc folder, Integer companyId, boolean issave) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", folder.getName().toLowerCase());
        Boolean hasFolder;
        if (issave) {
            hasFolder = ((Long) findSingleByNamedParams("SELECT count(f) FROM EdsFolders f WHERE  (f.deleted is null OR f.deleted <> true) AND lower(f.name)=:name ", map)) == 0;
        } else {
            map.put("folderId", folder.getId());
            hasFolder = ((Long) findSingleByNamedParams("SELECT count(f) FROM EdsFolders f WHERE  (f.deleted is null OR f.deleted <> true) AND lower(f.name)=:name AND f.id!=:folderId", map)) == 0;
        }
        return hasFolder;
    }

    @Override
    public EdsFolders getSystemFolder() {
        EdsFolders edsFolders = (EdsFolders) findSingle("SELECT f FROM EdsFolders f WHERE (f.deleted is null OR f.deleted <> true) AND f.type='" + FolderType.System.name() + "'");
        if (edsFolders == null) {
            String reportingAddSystemFolder = "insert into " + getCompanyId() + ".folders(name,type,domainName,companyid,createdate,showhide) " +
                    "select 'System','System','#'," + ServerSecurityContext.getInstance().getCompanyId() + ",now(),true";
            updateNative(reportingAddSystemFolder);
        } else {
            return edsFolders;
        }
        return getSystemFolder();
    }

    @Override
    public List<EdsFolders> getByCategory(EdsReportTemplateCategory category, String domainName, Integer companyId, Integer userId) {
        return (ArrayList<EdsFolders>) findNative("SELECT f.* FROM " + getCompanyId() + ".folders f " +
                " left join " + getPublic() + ".reportTemplateCategory c on f.categoryCode=c.code WHERE  " +
                " f.deleted is not true and f.categoryCode = '" + category.getCode() + "' " +
                " ORDER BY c.sorder, f.sorder, f.name", EdsFolders.class);
    }

    @Override
    public EdsFolders getByName(String name) {
        return (EdsFolders) findSingle("select t from EdsFolders t where t.name=?", name);
    }

    @Override
    public EdsFolders getByCategoryAndName(String category, String name) {
        return (EdsFolders) findNativeSingle("select f.* from " + getCompanyId() + ".folders f  left join " + getPublic() + ".reportTemplateCategory c on f.categoryCode=c.code where c.name ='" + category + "' and f.name='" + name + "' ", EdsFolders.class);
    }
}
