package com.edatasite.workforce.gwt.invoice.client.ui.view.projectbasedinvoice;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;

import java.util.Set;
import java.util.TreeSet;

public class BonnardProjectBasedInvoice extends ProjectBasedInvoice {


    @Override
    public String generateDescription(ProjectBaseData data) {
        StringBuilder desc = new StringBuilder();
        boolean first = true;
        Set<String> keySet = new TreeSet<>(toDescMap.keySet());
        for (String key : keySet) {
            switch (key) {
                case TASK_NAME:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getTaskName())) {
                        if (!first) {
                            desc.append("-~");
                        } else {
                            first = false;
                        }
                        desc.append(data.getTaskName());
                    }
                    break;
                case TASK_ASSIGNEE:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getEmployeeName())) {
                        if (!first) {
                            desc.append("-~");
                        } else {
                            first = false;
                        }
                        desc.append(data.getEmployeeName());
                    }
                    break;
                case TASK_DESCRIPTION:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getTaskDescription())) {
                        if (!first) {
                            desc.append("-~");
                        } else {
                            first = false;
                        }
                        desc.append(data.getTaskDescription());
                    }
                    break;
                case TIMESHEET_ENTRY:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue()
                            && data.getTsEntryDate() != null && data.getTsEntryDate().getNonConvertedDate() != null) {
                        if (!first) {
                            desc.append("-~");
                        } else {
                            first = false;
                        }
                        desc.append(DateUtils.format(data.getTsEntryDate().getNonConvertedDate()));
                    }
                    break;
                case TIMESHEET_PERIOD:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue() && data.getTsEntryDate() != null) {
                        if (!first) {
                            desc.append("-~");
                        } else {
                            first = false;
                        }
                        desc.append(DateUtils.format(fromDate.getDate()) + "-" + DateUtils.format(toDate.getDate()));
                    }
                    break;
            }
        }
        return desc.toString();
    }
}
