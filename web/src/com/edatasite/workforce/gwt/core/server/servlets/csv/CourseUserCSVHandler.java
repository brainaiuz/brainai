package com.edatasite.workforce.gwt.core.server.servlets.csv;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/18/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseUserCSVHandler extends AbstractBaseCSVHandler {

    @Autowired
    private TCService tcService;
    @Autowired
    private UserManager userManager;

    public static final String DELIMETER_CONST = ",";
    public static final String TITLE_CONST = "// RESPONSE Roster";
    public static final String[] TITLES_CONST = {
            "Clicker ID", "GroupId", "First",
            "Last", "Middle", "Student ID", "Nickname",
            "User ID", "Phone", "Email", "Gender", /*"Grade Level",*/ "Other"
    };

    @Override
    protected CSVTransferObject buildCSV(CSVTransferObject transferObject, Object dataClass) {
        if (transferObject == null) {
            transferObject = new CSVTransferObject();
        }

        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        filterParametrs.setLimit(2000);//set limit

        List<StudentItem> queriesList = tcService.getStudentListForCSV(filterParametrs);
        String shortDateFormat = "MM/dd/yyyy";
        Integer clickerID = 1;
        List<String[]> rows = new ArrayList<>();

        //initialize response roster
        String[] responseRoster = new String[TITLES_CONST.length];
        responseRoster[0] = TITLE_CONST;
        rows.add(responseRoster);

        rows.add(TITLES_CONST);

        for (StudentItem item : queriesList) {
            List<String> itemList = new ArrayList<>();
            itemList.add(clickerID.toString());
            itemList.add(item.getCourseSchedulerNumber());
            itemList.add(item.getFirstName());
            itemList.add(item.getLastName());
            itemList.add(ServerUtils.refactorNA(item.getMiddleName()));
            itemList.add(item.getObjectId() != null ? item.getObjectId().toString() : "N/A");
            itemList.add(item.getCompany());
            itemList.add(item.getInstructor());
            itemList.add(ServerUtils.refactorPhone(item.getPrimaryPhone()));
            itemList.add(item.getPrimaryEmail());
            itemList.add(ServerUtils.refactorNA(item.getGender()));
            itemList.add((ServerUtils.dateFormat(item.getCourseSchedulerStartDate(), shortDateFormat)));

            rows.add(itemList.toArray(new String[0]));
            clickerID++;
        }
        transferObject.setRows(rows);
        if (transferObject.getRows() != null && !transferObject.getRows().isEmpty()) {
            transferObject.getRows().get(0);
        }
        return transferObject;
    }

    @Override
    String getDinamicFilename(Object ob) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) ob;
        // filterParametrs.setLimit(1);
        //  List<StudentItem> queriesList = tcService.getStudentListForCSV(filterParametrs);
        ScheduledCourseItem scheduleCourseItem = tcService.getCourseSchedule(filterParametrs.getScheduledCourseID(), true);
        EdsUser user = userManager.getUser();
        String firstName = user.getFirstName() != null ? user.getFirstName().replace(" ", "") : "";
        String lastName = user.getLastName() != null ? user.getLastName().replace(" ", "") : "";
        String date = ServerUtils.dateFormat(user.getUserDate(), "MM_dd_yyyy");
        String userName = scheduleCourseItem.getInstructorName() != null ? scheduleCourseItem.getInstructorName() : (firstName + "_" + lastName);
        return scheduleCourseItem.getNumber() + "_" + userName + "_" + date;
    }

    @Override
    String getFileName() {
        return null;
    }

    @Override
    protected Object prepareRequest(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        for (Map map : (Iterable<Map>) filterMap.entrySet()) {
            Map.Entry entry = (Map.Entry) map;
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        fp.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(fp.getFacetFilterJson()));
        fp.setListPanelTool(WfmJsonUtils.jsonConvertToListPanelToolRpc(fp.getListPanelToolJson()));
        return fp;
    }

}
