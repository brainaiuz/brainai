package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceMarkListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AttendanceMarksListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(AttendanceMarksListExcelHandler.class);

    @Autowired
    private UserFingerPrintmanager userFingerPrintmanager;

    @Override
    protected void setFileName() {
        EdsUser user = userManager.getUser();
        if (user != null) {
            filename = user.getFirstName() + "_" + user.getLastName() + "_AttendanceMarksList_" + dateFormat(user.getUserDate());
        } else {
            filename = "AttendanceMarksList";
        }
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        fp.setLimit(LIMIT_EXCEL_ROW);

        List<AttendanceMarkListItem> result = userFingerPrintmanager.getAttendanceMarkListForExport(fp);
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(AttendanceMarkListItem.EMPLOYEE_CODE, commonLocalizer.localize("employeeCode"));
        mapColumnHeader.put(AttendanceMarkListItem.EMPLOYEE_NAME, commonLocalizer.localize("employee"));
        mapColumnHeader.put(AttendanceMarkListItem.DATE, commonLocalizer.localize("date"));
        mapColumnHeader.put(AttendanceMarkListItem.LOCATION, commonLocalizer.localize("location"));
        mapColumnHeader.put(AttendanceMarkListItem.DEPARTMENT, commonLocalizer.localize("department"));
        mapColumnHeader.put(AttendanceMarkListItem.POSITION, commonLocalizer.localize("position"));
        mapColumnHeader.put(AttendanceMarkListItem.TIMESLOT, commonLocalizer.localize("timeslot"));
        mapColumnHeader.put(AttendanceMarkListItem.SUPERVISOR, commonLocalizer.localize("supervisor"));
        mapColumnHeader.put(AttendanceMarkListItem.DEVICE_ID, commonLocalizer.localize("deviceId"));
        mapColumnHeader.put(AttendanceMarkListItem.TERMINAL, commonLocalizer.localize("terminal"));
        mapColumnHeader.put(AttendanceMarkListItem.EMPLOYEE_STATUS, commonLocalizer.localize("status"));
        mapColumnHeader.put(AttendanceMarkListItem.ROLE, commonLocalizer.localize("role"));
        mapColumnHeader.put(AttendanceMarkListItem.VALIDATED, commonLocalizer.localize("inOut"));
        mapColumnHeader.put(AttendanceMarkListItem.SOURCE, commonLocalizer.localize("source"));
        mapColumnHeader.put(AttendanceMarkListItem.IS_AUTO, commonLocalizer.localize("isAuto"));
        mapColumnHeader.put(AttendanceMarkListItem.CREATED_DATE, commonLocalizer.localize("createdDate"));
        mapColumnHeader.put(AttendanceMarkListItem.NOTE, commonLocalizer.localize("note"));
        mapColumnHeader.put(AttendanceMarkListItem.CREATED_BY_NAME, commonLocalizer.localize("createdBy"));
        mapColumnHeader.put(AttendanceMarkListItem.UPDATED_AT, commonLocalizer.localize("modifiedDate"));
        mapColumnHeader.put(AttendanceMarkListItem.UPDATED_BY_NAME, commonLocalizer.localize("modifiedBy"));
        mapColumnHeader.put(AttendanceMarkListItem.MAP, commonLocalizer.localize("location"));
        mapColumnHeader.put(AttendanceMarkListItem.PROFILE_PICTURE_URL, commonLocalizer.localize("profilePhoto"));
        mapColumnHeader.put("pictureUrl", commonLocalizer.localize("photo"));

        try {
            List<ExcelData[]> list = new LinkedList<>();
            ExcelData[] cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);

            for (AttendanceMarkListItem item : result) {
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    String col = header.get(j);
                    String val = "";
                    if (AttendanceMarkListItem.EMPLOYEE_CODE.equals(col)) {
                        val = item.getEmployeeCode() != null ? item.getEmployeeCode() : "";
                    } else if (AttendanceMarkListItem.EMPLOYEE_NAME.equals(col)) {
                        val = item.getEmployeeName() != null ? item.getEmployeeName() : "";
                    } else if (AttendanceMarkListItem.DATE.equals(col)) {
                        val = item.getDate() != null ? longDateFormat(item.getDate().getDate(), true) : "";
                    } else if (AttendanceMarkListItem.LOCATION.equals(col)) {
                        val = item.getLocation() != null ? item.getLocation() : "";
                    } else if (AttendanceMarkListItem.DEPARTMENT.equals(col)) {
                        val = item.getDepartment() != null ? item.getDepartment() : "";
                    } else if (AttendanceMarkListItem.POSITION.equals(col)) {
                        val = item.getPosition() != null ? item.getPosition() : "";
                    } else if (AttendanceMarkListItem.TIMESLOT.equals(col)) {
                        val = item.getTimeslotName() != null ? item.getTimeslotName() : "";
                    } else if (AttendanceMarkListItem.SUPERVISOR.equals(col)) {
                        val = item.getSupervisor() != null ? item.getSupervisor() : "";
                    } else if (AttendanceMarkListItem.DEVICE_ID.equals(col)) {
                        val = item.getDeviceId() != null ? item.getDeviceId() : "";
                    } else if (AttendanceMarkListItem.TERMINAL.equals(col)) {
                        val = item.getTerminal() != null ? item.getTerminal() : "";
                    } else if (AttendanceMarkListItem.EMPLOYEE_STATUS.equals(col)) {
                        val = item.getEmployeeStatus() != null ? item.getEmployeeStatus() : "";
                    } else if (AttendanceMarkListItem.ROLE.equals(col)) {
                        val = item.getRole() != null ? item.getRole() : "";
                    } else if (AttendanceMarkListItem.VALIDATED.equals(col)) {
                        val = item.getValidated() != null ? item.getValidated() : "";
                    } else if (AttendanceMarkListItem.SOURCE.equals(col)) {
                        val = formatSource(item.getSource());
                    } else if (AttendanceMarkListItem.IS_AUTO.equals(col)) {
                        val = Boolean.TRUE.equals(item.getIsAuto()) ? commonLocalizer.localize("yes") : commonLocalizer.localize("no");
                    } else if (AttendanceMarkListItem.CREATED_DATE.equals(col)) {
                        val = item.getCreatedDate() != null ? longDateFormat(item.getCreatedDate().getDate(), true) : "";
                    } else if (AttendanceMarkListItem.NOTE.equals(col)) {
                        val = item.getNote() != null ? item.getNote() : "";
                    } else if (AttendanceMarkListItem.CREATED_BY_NAME.equals(col)) {
                        val = item.getCreatedByName() != null ? item.getCreatedByName() : "";
                    } else if (AttendanceMarkListItem.UPDATED_AT.equals(col)) {
                        val = item.getUpdatedAt() != null ? longDateFormat(item.getUpdatedAt().getDate(), true) : "";
                    } else if (AttendanceMarkListItem.UPDATED_BY_NAME.equals(col)) {
                        val = item.getUpdatedByName() != null ? item.getUpdatedByName() : "";
                    } else if (AttendanceMarkListItem.MAP.equals(col)) {
                        val = (item.getLatitude() != null && item.getLongitude() != null)
                                ? item.getLatitude() + ", " + item.getLongitude() : "";
                    } else if (AttendanceMarkListItem.PROFILE_PICTURE_URL.equals(col) || "pictureUrl".equals(col)) {
                        val = "";
                    }
                    cellDatas[j] = new ExcelData(val, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }

            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            log.error("Failed to generate AttendanceMarksList Excel", e);
        }
        return null;
    }

    private String formatSource(String source) {
        if (source == null) return "";
        return switch (source) {
            case "FACE_ID" -> "Face ID";
            case "MOBILE" -> "Mobile";
            case "WEB" -> "Web";
            case "UNKNOWN" -> "Unknown";
            default -> source;
        };
    }
}
