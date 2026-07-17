package com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * При корректировке учесть извменения в
 * multidb\reporting\src\com\finnetlimited\reportservice\core\server\parser\XmlParser.java
 * <p/>
 * User: ${Dilsh0d}
 * Date: 15-Apr-2010
 * Time: 22:09:30
 */
public class RpcConvertToXml implements IsSerializable {
    protected String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    protected ReportRpc report;

    public RpcConvertToXml(ReportRpc report) {
        this.report = report;
    }

    public RpcConvertToXml() {
    }

    /* ReportRpc object convert to xml string format */

    public String generate() {
        xml = xml + "<report>\n";
        xml = xml + " <id>" + getData(report.getId()) + "</id>\n";
        xml = xml + " <name>" + getData(report.getName()) + "</name>\n";
        xml = xml + " <discreption>" + getData(report.getDiscreption()) + "</discreption>\n";
        xml = xml + " <folderid>" + getData(report.getFolderId()) + "</folderid>\n";
        xml = xml + " <viewname>" + getData(report.getViewName()) + "</viewname>\n";
        xml = xml + " <tabletype>" + getData(report.getTableType()) + "</tabletype>\n";
        xml = xml + " <sntfiltername>" + getData(report.getSntFilterName()) + "</sntfiltername>\n";
        xml = xml + " <durationtype>" + getData(report.getDurationType()) + "</durationtype>\n";
        xml = xml + " <startdate>" + getData(report.getStartDate()) + "</startdate>\n";
        xml = xml + " <enddate>" + getData(report.getEndDate()) + "</enddate>\n";
        xml = xml + " <sntfilternamechange>" + getData(report.getSntFilterNameChange()) + "</sntfilternamechange>\n";
        xml = xml + " <durationtypechange>" + getData(report.getDurationTypeChange()) + "</durationtypechange>\n";
        xml = xml + " <startdatechange>" + getData(report.getStartDateChange()) + "</startdatechange>\n";
        xml = xml + " <enddatechange>" + getData(report.getEndDateChange()) + "</enddatechange>\n";
        xml = xml + " <isdetailed>" + getData(report.getIsDetailed().toString()) + "</isdetailed>\n";
        xml = xml + " <showrowcount>" + getData(report.isShowRowCount().toString()) + "</showrowcount>\n";
        xml = xml + " <rolechange>" + getData(report.getRoleChange()) + "</rolechange>\n";
        xml = xml + " <templateid>" + getData(report.getPdfTemplateId()) + "</templateid>\n";
        xml = xml + " <excelTemplateId>" + getData(report.getExcelTemplateId()) + "</excelTemplateId>\n";
        xml = xml + " <xmlTemplateId>" + getData(report.getXmlTemplateId()) + "</xmlTemplateId>\n";
        xml = xml + " <viewCode>" + getData(report.getViewCode()) + "</viewCode>\n";
        xml = xml + " <sortByColumn>" + getData(report.getSortTableByColumn()) + "</sortByColumn>\n";
        xml = xml + " <sortType>" + getData(report.getSortTableByColumnType()) + "</sortType>\n";
        xml = xml + " <maxExcelRowCount>" + getData(report.getMaxExcelRowCount()) + "</maxExcelRowCount>\n";
        xml = xml + " <landscape>" + getData(report.isLandscape()) + "</landscape>\n";

        if (report.getCustomFilter() != null && !report.getCustomFilter().isEmpty()) {
            xml = xml + "<customFilter>" + getData(report.getCustomFilter().toString()) + "</customFilter>\n";
        }

        xml = xml + " <timeZone>" + getData(report.getBrowserTimeZone()) + "</timeZone>\n";

        /* sort type xml */
        setXmlValue(report.getSortTypes(), "sorttypes");
        /* range type xml */
        setXmlValue(report.getRangeType(), "rangetype");
        /* '(' position */
        xml = xml + " <sett>\n";
        for (int x = 0; x < report.getSett().size(); x++) {
            xml = xml + "  <value>" + getData(report.getSett().get(x)) + "</value>\n";
        }
        xml = xml + " </sett>\n";

        /* field list */
        setColumnRpcXmlValue(report.getFieldd(), "fieldd");
        /* operation list */
        setXmlValue(report.getOperators(), "operators");
        /* values list */
        setXmlValue(report.getValues(), "values");
        /* boolType list */
        setXmlValue(report.getBoolType(), "boolType");
        /* groupColumns list */
        setColumnRpcXmlValue(report.getGroupColumns(), "groupColumns");
        /* sumaries list */
        setColumnRpcXmlValue(report.getSumaries(), "sumaries");
        /* selectedColumns list */
        setColumnRpcXmlValue(report.getSelectedColumns(), "selectedColumns");

        /* selectedColumnsMap date  */
        setColumnMapXmlValue(report.getColumnMap(), "columnMap");

        xml = xml + " <sortTableByColumn>" + getData(report.getSortTableByColumn()) + "</sortTableByColumn>\n";
        xml = xml + " <sortTableByColumnType>" + getData(report.getSortTableByColumnType()) + "</sortTableByColumnType>\n";
        xml = xml + " <limit>" + getData(report.getLimit()) + "</limit>\n";
        xml = xml + " <position>" + getData(report.getPosition()) + "</position>\n";
        xml = xml + " <filterPattern>" + getData(report.getFilterPattern()) + "</filterPattern>\n";
        xml = xml + "</report>\n";

        return xml;
    }

    private void setColumnMapXmlValue(HashMap<String, ColumnRpc> columnMap, String ownerTag) {
        xml = xml + " <" + ownerTag + ">\n";
        for (String columnMapKey : columnMap.keySet()) {
            xml = xml + "  <column>\n";
            xml = xml + "   <columnMapKey>" + getData(columnMapKey) + "</columnMapKey>\n";
            xml = xml + "   <name>" + getData(columnMap.get(columnMapKey).getName()) + "</name>\n";
            xml = xml + "   <title>" + getData(columnMap.get(columnMapKey).getTitle()) + "</title>\n";
            xml = xml + "   <type>" + getData(columnMap.get(columnMapKey).getType()) + "</type>\n";
            xml = xml + "   <format>" + getData(columnMap.get(columnMapKey).getColumnFormat()) + "</format>\n";
            xml = xml + "   <customdateformat>" + getData(columnMap.get(columnMapKey).getCustomDateFormat()) + "</customdateformat>\n";
            xml = xml + "   <checked>" + getData(columnMap.get(columnMapKey).isChecked()) + "</checked>\n";
            xml = xml + "   <sum>" + getData(columnMap.get(columnMapKey).isSum()) + "</sum>\n";
            xml = xml + "   <avg>" + getData(columnMap.get(columnMapKey).isAvg()) + "</avg>\n";
            xml = xml + "   <largest>" + getData(columnMap.get(columnMapKey).isLargest()) + "</largest>\n";
            xml = xml + "   <smallest>" + getData(columnMap.get(columnMapKey).isSmallest()) + "</smallest>\n";
            xml = xml + "   <count>" + getData(columnMap.get(columnMapKey).isCount()) + "</count>\n";
            if (columnMap.get(columnMapKey).getReletedColumn() != null && !"".equals(columnMap.get(columnMapKey).getReletedColumn())) {
                xml = xml + "   <reletedcolumn>" + getData(columnMap.get(columnMapKey).getReletedColumn()) + "</reletedcolumn>\n";
            }
            xml = xml + "   <prefix>" + getData(columnMap.get(columnMapKey).getPrefix()) + "</prefix>\n";
            xml = xml + "   <isCustomField>" + getData(columnMap.get(columnMapKey).getIsCustomField()) + "</isCustomField>\n";
            xml = xml + "   <customFieldJoin>" + getData(columnMap.get(columnMapKey).getCustomFieldJoin()) + "</customFieldJoin>\n";

            xml = xml + "  </column>\n";
        }
        xml = xml + " </" + ownerTag + ">\n";
    }

    protected String getData(String data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return Utils.encrypt(dt);
    }

    protected String getData(Boolean data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return Utils.encrypt(dt);
    }

    protected String getData(Integer data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return Utils.encrypt(dt);
    }

    protected String getData(Date data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return Utils.encrypt(dt);
    }

    /* list string parse xml format */

    private void setXmlValue(List<String> list, String ownerTag) {
        xml = xml + " <" + ownerTag + ">\n";
        for (String aList : list) {
            xml = xml + "  <value>" + getData(aList) + "</value>\n";
        }
        xml = xml + " </" + ownerTag + ">\n";
    }

    /* list ColumnRpc object parse to xml format */

    protected void setColumnRpcXmlValue(List<ColumnRpc> columns, String ownerTag) {
        xml = xml + " <" + ownerTag + ">\n";
        for (ColumnRpc column : columns) {
            xml = xml + "  <column>\n";
            xml = xml + "   <name>" + getData(column.getName()) + "</name>\n";
            xml = xml + "   <title>" + getData(column.getTitle()) + "</title>\n";
            xml = xml + "   <type>" + getData(column.getType()) + "</type>\n";
            xml = xml + "   <format>" + getData(column.getColumnFormat()) + "</format>\n";
            xml = xml + "   <customdateformat>" + getData(column.getCustomDateFormat()) + "</customdateformat>\n";
            xml = xml + "   <checked>" + getData(column.isChecked()) + "</checked>\n";
            xml = xml + "   <sum>" + getData(column.isSum()) + "</sum>\n";
            xml = xml + "   <avg>" + getData(column.isAvg()) + "</avg>\n";
            xml = xml + "   <largest>" + getData(column.isLargest()) + "</largest>\n";
            xml = xml + "   <smallest>" + getData(column.isSmallest()) + "</smallest>\n";
            xml = xml + "   <count>" + getData(column.isCount()) + "</count>\n";
            if (column.getReletedColumn() != null && !"".equals(column.getReletedColumn())) {
                xml = xml + "   <reletedcolumn>" + getData(column.getReletedColumn()) + "</reletedcolumn>\n";
            }
            xml = xml + "   <prefix>" + getData(column.getPrefix()) + "</prefix>\n";
            xml = xml + "   <isCustomField>" + getData(column.getIsCustomField()) + "</isCustomField>\n";
            xml = xml + "   <customFieldJoin>" + getData(column.getCustomFieldJoin()) + "</customFieldJoin>\n";

            xml = xml + "  </column>\n";
        }
        xml = xml + " </" + ownerTag + ">\n";
    }
}
