package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.EnquiryService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/30/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class EnquiryListExcelHandler extends BaseExcelHandler {

	@Autowired
	private EnquiryService enquiryService;

	@Override
	protected void setFileName() {
		filename = "EnquiryList";
	}

	@Override
	protected boolean prepareRequest(HttpServletRequest request) {
		return false;
	}

	protected HSSFWorkbook getWorkBook(Object object) {
		ListingFilterParameter filterParametrs = (ListingFilterParameter) object;

		EdsCompany edsCompany = userManager.getUser().getCompany();
		EdsCompanySettings companySettings = edsCompany.getCompanySettings();
		if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
			filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
		} else {
			filterParametrs.setLimit(LIMIT_EXCEL_ROW);
		}

		int start = -200;
		int limit = 200;
		int totalLength = 1;
		List<EnquiryItem> enquiryItems = new ArrayList<>();
		while (totalLength > (start += limit)) {
			filterParametrs.setStart(start);
			filterParametrs.setLimit(200);
			ListResult<EnquiryItem> enquiryList = enquiryService.geEnquiryList(filterParametrs);
			totalLength = enquiryList.getTotal();
			enquiryItems.addAll(enquiryList.getList());
		}
		ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

		ExcelData[] cellDatas;
		Map<String, ExcelData> mapColumnHeader = new HashMap<>();
		try {
			List<ExcelData[]> list = new LinkedList<>();
			mapColumnHeader.put(EnquiryItem.ENQUIRY_MODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.enquiryMode), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(EnquiryItem.ENQUIRY_CUSTOMER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(EnquiryItem.ENQUIRY_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.enquiryDate), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(EnquiryItem.CUSTOMER_CURRENCY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(EnquiryItem.CONTACT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contactName), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(EnquiryItem.CONTACT_EMAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(EnquiryItem.REF_INFO, new ExcelData(commonLocalizer.localize(PdfLocalizationName.refInfo), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			// Set excell header
			List<ExcelData> excellDatasList = new ArrayList<>();
			for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
				if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
					excellDatasList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
				}
			}
			cellDatas = new ExcelData[excellDatasList.size()];
			excellDatasList.toArray(cellDatas);
			list.add(cellDatas);

			for (EnquiryItem enquiry : enquiryItems) {
				Map<String, ExcelData> mapColumn = new HashMap<>();
				if (panelTools.getColumnCodeName().contains(EnquiryItem.ENQUIRY_MODE)) {
					mapColumn.put(EnquiryItem.ENQUIRY_MODE, new ExcelData(enquiry.getEnquiryMode() != null ? enquiry.getEnquiryMode().getName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}
				if (panelTools.getColumnCodeName().contains(EnquiryItem.ENQUIRY_CUSTOMER)) {
					mapColumn.put(EnquiryItem.ENQUIRY_CUSTOMER, new ExcelData(enquiry.getCustomer() != null ? enquiry.getCustomer().getName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}
				if (panelTools.getColumnCodeName().contains(EnquiryItem.ENQUIRY_DATE)) {
					mapColumn.put(EnquiryItem.ENQUIRY_DATE, new ExcelData(enquiry.getEnquiryDate() != null ? dateFormat(enquiry.getEnquiryDate()) : "", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}
                if (panelTools.getColumnCodeName().contains(EnquiryItem.CUSTOMER_CURRENCY)) {
                    mapColumn.put(EnquiryItem.CUSTOMER_CURRENCY, new ExcelData(enquiry.getCurrency().getId() != null ? enquiry.getCurrency().getName() : "", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.CONTACT_NAME)) {
                    mapColumn.put(EnquiryItem.CONTACT_NAME, new ExcelData(enquiry.getContactDetails().getName(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.CONTACT_EMAIL)) {
                    mapColumn.put(EnquiryItem.CONTACT_EMAIL, new ExcelData(enquiry.getContactDetails().getPrimaryEmail() != null ? enquiry.getContactDetails().getPrimaryEmail() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.REF_INFO)) {
                    mapColumn.put(EnquiryItem.REF_INFO, new ExcelData(enquiry.getRefInfo() != null ? enquiry.getRefInfo() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
				excellDatasList = new ArrayList<>();
				for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
					if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
						excellDatasList.add(getExcelRows(mapColumn.get(panelTools.getColumnCodeName().get(i))));
					}
				}
				cellDatas = new ExcelData[excellDatasList.size()];
				excellDatasList.toArray(cellDatas);
				list.add(cellDatas);
			}
			WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);

			return workBook.getWorkBook(filename, 0, 0, 0, 7);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
