package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.project.client.rpc.BookingItemsItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: WFT01
 * Date: 07.11.12
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemListExcelHandler extends BaseExcelHandler {
	@Override
	protected void setFileName() {
		filename = "BookingItem_List";
	}

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserManager userManager;
    private static final Logger log = LoggerFactory.getLogger(BookingItemListExcelHandler.class);

	@Autowired
	@Qualifier("allReferenceWfmMessageSource")
	protected WfmResourceBundleMessageSource excelReferenceMessageSource;

	@Autowired
	private PropertManager propertManager;

	@Override
	protected boolean prepareRequest(HttpServletRequest request) {
		return false;
	}

	protected HSSFWorkbook getWorkBook(Object object) {

		ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
		String shortDateFormat = "MM/dd/yyyy";

		filterParametrs.setLimit(200);
		ListResult<BookingItemsItem> bookingItemLists = projectService.getBookingItems(filterParametrs);
		ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
		List<BookingItemsItem> bookingItems = bookingItemLists.getList();
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
		ExcelData[] cellDatas;
		Map<String, ExcelData> mapColumnData = new HashMap<>();

		try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
			mapColumnData.put(BookingItemsItem.ITEM_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnData.put(BookingItemsItem.ITEM_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

			mapColumnData.put(BookingItemsItem.CATEGORY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.category), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnData.put(BookingItemsItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

			mapColumnData.put(BookingItemsItem.LOCATION, new ExcelData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnData.put(BookingItemsItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
			list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), "Booking Items", workBook.getSheet(), 1));
			list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
			for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
				if (mapColumnData.containsKey(panelTools.getColumnCodeName().get(i))) {
					excelDataList.add(mapColumnData.get(panelTools.getColumnCodeName().get(i)));
				}
			}
			cellDatas = new ExcelData[excelDataList.size()];
			excelDataList.toArray(cellDatas);
			list.add(cellDatas);
			for (BookingItemsItem bookingItem : bookingItemLists.getList()) {
				Map<String, ExcelData> mapColumn = new HashMap<>();

				if (panelTools.getColumnCodeName().contains(BookingItemsItem.ITEM_NUMBER)) {
					mapColumn.put(BookingItemsItem.ITEM_NUMBER, new ExcelData(bookingItem.getItemNumber(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}
				if (panelTools.getColumnCodeName().contains(BookingItemsItem.ITEM_NAME)) {
					mapColumn.put(BookingItemsItem.ITEM_NAME, new ExcelData(bookingItem.getItemName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}

				if (panelTools.getColumnCodeName().contains(BookingItemsItem.CATEGORY)) {
					mapColumn.put(BookingItemsItem.CATEGORY, new ExcelData(bookingItem.getCategory().getName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}
				if (panelTools.getColumnCodeName().contains(BookingItemsItem.STATUS)) {
					mapColumn.put(BookingItemsItem.STATUS, new ExcelData(bookingItem.getStatus(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}

				if (panelTools.getColumnCodeName().contains(BookingItemsItem.LOCATION)) {
					mapColumn.put(BookingItemsItem.LOCATION, new ExcelData(bookingItem.getLocation(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}
				if (panelTools.getColumnCodeName().contains(BookingItemsItem.DESCRIPTION)) {
					mapColumn.put(BookingItemsItem.DESCRIPTION, new ExcelData(bookingItem.getDescription(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
				}

				excelDataList = new ArrayList<>();
				for (int j = 0; j < panelTools.getColumnCodeName().size(); j++) {
					if (mapColumn.containsKey(panelTools.getColumnCodeName().get(j))) {
						excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(j)));
					}
				}
				cellDatas = new ExcelData[excelDataList.size()];
				excelDataList.toArray(cellDatas);
				list.add(cellDatas);
			}
            workBook.setList(list);
			return workBook.getWorkBook(filename, 0, 0, 0, 6);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Cannot generate project list excel report, exception: " + e);
		}


		return null;
	}


}
