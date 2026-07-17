package com.edatasite.workforce.gwt.core.server.app;

import com.csvreader.CsvReader;
import com.edatasite.workforce.components.ImageScaleDown;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.solr.component.ChartOfAccountSolrComponent;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeePresentItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.actions.BankAccountDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.ProductCategoryDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.ProductDocumentCommand;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankAccountAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryPictureManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.db.impl.AmazonManagerImpl;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmMultipartFile;
import com.edatasite.workforce.gwt.task.server.actions.CreateTaskCommentCommand;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mspdi.MSPDIReader;
import net.sf.mpxj.reader.ProjectReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * User: Ilhombek
 * Date: 5/31/11
 * Time: 7:41 PM
 */
@Service("wfmCommandService")
public class WfmCommandServiceImpl implements WfmCommandServiceLocal, CommandConstants, Constants {
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private AccountTemplateManager accountTemplateManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    private BankAccountAttachmentManager bankAccountAttachmentManager;
    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private UserFingerPrintDeviceManager userFingerPrintDeviceManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private ProductCategoryPictureManager productCategoryPictureManager;
    @Autowired
    private ProductPictureManager productPictureManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private TaskCommentManager taskCommentManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private TimeTrackManager timeTrackManager;
    @Autowired
    private UserFingerPrintmanager userFingerPrintmanager;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskService;
    @Autowired
    @Qualifier("tcService")
    private TCServiceLocal tcService;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ChartOfAccountSolrComponent chartOfAccountSolrComponent;
    @Autowired
    private CommonService commonService;

    private static final Logger log = LoggerFactory.getLogger(WfmCommandServiceImpl.class);

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Related company logo and pdf logo attachments
     *
     * @param createDocumentCommand - document command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createLogoHandler(CreateDocumentCommand createDocumentCommand) throws Throwable {
        EdsUser user = companyAttachmentManager.getUser();
        EdsCompany company = user.getCompany();

        if (createDocumentCommand.getCompanyID() != null && createDocumentCommand.getCompanyID() > 0) {
            company = companyManager.getCompany(createDocumentCommand.getCompanyID());
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
        }
        List<EdsCompanyAttachment> compAttachments = companyAttachmentManager.getCompanyAttachments(company, referenceManager.findReference("_LOGO_TYPE", createDocumentCommand.getLogoType() != null ? createDocumentCommand.getLogoType() : CommandConstants.FOR_PDF));
        for (EdsCompanyAttachment compAttach : compAttachments) {
            if (compAttach != null) {
                try {
                    EdsCompanyAttachment companyAttachment = companyAttachmentManager.get(compAttach.getObjectID());
                    if (companyAttachment != null) {
                        uploadManager.deleteFile(companyAttachment);
                        uploadManager.delete(companyAttachment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        EdsCompanyAttachment attachment = new EdsCompanyAttachment();
        WfmMultipartFile file = createDocumentCommand.getFiles()[0];

        String[] values = new String[2];
        if (file.getFile() != null) {
            try {
                ImageLoader image = new ImageLoader(file.getFile().getInputStream());
                Integer width = createDocumentCommand.getImageWidth() != null ? createDocumentCommand.getImageWidth() : 240;
                Integer height = createDocumentCommand.getImageHeight() != null ? createDocumentCommand.getImageHeight() : 60;

                if (company.getObjectID().equals(9331) && FOR_INVOICEPDF.equals(createDocumentCommand.getLogoType())) {
                    //COMPANY_ID:9331 ----> The PMO Company
                    width = 495;
                    height = 150;
                }

//                if (image.getImageWidth() <= width || image.getImageHeight() <= height) {
                String fileName = file.getFile().getOriginalFilename().substring(0, file.getFile().getOriginalFilename().lastIndexOf("."));
                String type = file.getFile().getOriginalFilename().substring(file.getFile().getOriginalFilename().lastIndexOf(".") + 1);
                EdsReference logoType = referenceManager.findReference("_LOGO_TYPE", createDocumentCommand.getLogoType());

                //small formatda saqlash; Medium format: 240px X 60px;
                ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                Object[] objects = imageScaleDown.getAdvancedImageScaleDownInputStream(width, height);
                attachment.setInputStream((InputStream) objects[0]);
                attachment.setOriginalName(fileName + "_" + objects[1] + "x" + objects[2] + "." + type);
                attachment.setContentType(file.getFile().getContentType());
                attachment.setLogoType(logoType);
                attachment.setFolderName(STATIC_FOLDER);
                attachment.setImageSize(IMAGE_SIZE_SMALL);
                attachment.setDownloadable(!StringUtils.isNotBlank(createDocumentCommand.getNotdownloadable()));
                companyAttachmentManager.create(attachment);

                //medium formatda saqlash; Medium format: 480px X 120px;
                EdsCompanyAttachment childAttachment = new EdsCompanyAttachment();
                imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                objects = imageScaleDown.getAdvancedImageScaleDownInputStream((2 * width), (2 * height));
                childAttachment.setInputStream((InputStream) objects[0]);
                childAttachment.setOriginalName(fileName + "_" + objects[1] + "x" + objects[2] + "." + type);
                childAttachment.setContentType(file.getFile().getContentType());
                childAttachment.setLogoType(logoType);
                childAttachment.setFolderName(STATIC_FOLDER);
                childAttachment.setImageSize(IMAGE_SIZE_MEDIUM);
                childAttachment.setParent(attachment);
                childAttachment.setDownloadable(!StringUtils.isNotBlank(createDocumentCommand.getNotdownloadable()));
                companyAttachmentManager.create(childAttachment);

                //large formatda saqlash; Large format: 720px X 180px;
                childAttachment = new EdsCompanyAttachment();
                imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                objects = imageScaleDown.getAdvancedImageScaleDownInputStream((3 * width), (3 * height));
                childAttachment.setInputStream((InputStream) objects[0]);
                childAttachment.setOriginalName(fileName + "_" + objects[1] + "x" + objects[2] + "." + type);
                childAttachment.setContentType(file.getFile().getContentType());
                childAttachment.setLogoType(logoType);
                childAttachment.setFolderName(STATIC_FOLDER);
                childAttachment.setImageSize(IMAGE_SIZE_LARGE);
                childAttachment.setParent(attachment);
                childAttachment.setDownloadable(!StringUtils.isNotBlank(createDocumentCommand.getNotdownloadable()));
                companyAttachmentManager.create(childAttachment);

                //original formatda saqlash;
                childAttachment = new EdsCompanyAttachment();
                imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                childAttachment.setInputStream(file.getFile().getInputStream());
                childAttachment.setOriginalName(fileName + "_" + image.getImageWidth() + "x" + image.getImageHeight() + "." + type);
                childAttachment.setContentType(file.getFile().getContentType());
                childAttachment.setLogoType(logoType);
                childAttachment.setFolderName(STATIC_FOLDER);
                childAttachment.setImageSize(IMAGE_SIZE_ORIGINAL);
                childAttachment.setParent(attachment);
                childAttachment.setDownloadable(!StringUtils.isNotBlank(createDocumentCommand.getNotdownloadable()));
                companyAttachmentManager.create(childAttachment);
                values[0] = attachment.getObjectID().toString();
                /*} else {
                    values[1] = "Image size should not be greater than 235px x 58px.";
//					setErrorString("Unexpected error occurred during saving logo");
                }*/
            } catch (Exception ex) {
                ex.printStackTrace();
                values[1] = "Image size should not be greater than 235px x 58px. Maximum image size is 100KB.";
            }
        }
        return values;
    }

    /**
     * Related attendance report doc upload
     *
     * @param wfmCommand - wdm command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createAttendanceReportUploadHandler(WfmCommand wfmCommand) throws Throwable {
        String[] values = new String[2];
        Date leftDate = null;
        Date currentDate;
        Map<Integer, Calendar> mapDate;
        EdsUser user = companyManager.getUser();
        TimeZone timeZone = user.getUserTimezone();
        Map<String, EdsEmployee> employees = employeeManager.getEmployeeMapByCode();
        for (WfmMultipartFile file : wfmCommand.getFiles()) {
            HSSFWorkbook wb = new HSSFWorkbook(file.getFile().getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            EdsReference status = referenceManager.getReference(20);
            mapDate = getXslDate(sheet.getRow(3), values);
            currentDate = new Date();
            values[0] = "";
            for (int k = 5; k <= sheet.getLastRowNum(); k++) {
                HSSFRow row = sheet.getRow(k);
                HSSFCell cell = row.getCell(0);
                HSSFCell cell2 = row.getCell(1);
                try {
                    if ((cell == null || cell2 == null) && !isNumber(cell.getStringCellValue())) {
                        continue;
                    }
                    EdsEmployee employee = employees.get(cell.getStringCellValue());
                    if (employee == null) {
                        employee = employeeManager.getEmployeeByFirstNameViaLastName(cell2.getStringCellValue());
                    }
                    int step = 1;
                    if (employee != null) {
                        for (int i = 2; i < row.getLastCellNum(); i += 2) {
                            HSSFCell cellCame = row.getCell(i);
                            HSSFCell cellLeft = row.getCell(i + 1);
                            if (cellCame != null && cellLeft != null) {
                                String came = getCameCount(cellCame);
                                String left = getLeftCount(cellLeft, currentDate);
                                if (!came.equals("") && !left.equals("") && (!came.equals("00:00") || !left.equals("00:00"))) {
                                    Calendar cal = mapDate.get(step);
                                    Date tempTime = cal.getTime();
                                    int hourCame = 0, minCame = 0, hourLeft = 0, minLeft = 0;
                                    if ((came.contains(":") && left.contains(":"))) {
                                        hourCame = isNumber(came.split(":")[0]) ? Integer.parseInt(came.split(":")[0]) : 0;
                                        minCame = isNumber(came.split(":")[1]) ? Integer.parseInt(came.split(":")[1]) : 0;
                                        hourLeft = isNumber(left.split(":")[0]) ? Integer.parseInt(left.split(":")[0]) : 0;
                                        minLeft = isNumber(left.split(":")[1]) ? Integer.parseInt(left.split(":")[1]) : 0;
                                        if ((hourCame != 0 || minCame != 0) || (hourLeft != 0 || minLeft != 0)) {
                                            EdsTimeTrack timeTrack = new EdsTimeTrack();
                                            timeTrack.setEmployee(employee);
                                            timeTrack.setStatus(status);

                                            cal.set(Calendar.HOUR_OF_DAY, hourCame);
                                            cal.set(Calendar.MINUTE, minCame);
                                            cal.set(Calendar.SECOND, 0);
                                            timeTrack.setStartDate(ServerUtils.convertUserDateToServerDate(cal.getTime(), timeZone));
                                            if (leftDate != null) {
                                                timeTrack.setEndDate(ServerUtils.convertUserDateToServerDate(leftDate, timeZone));
                                                leftDate = null;
                                            } else {
                                                cal.set(Calendar.HOUR_OF_DAY, hourLeft);
                                                cal.set(Calendar.MINUTE, minLeft);
                                                timeTrack.setEndDate(ServerUtils.convertUserDateToServerDate(cal.getTime(), timeZone));
                                            }
                                            timeTrackManager.deleteEqualsStartDate(employee.getObjectID(), timeTrack.getStartDate());
                                            timeTrackManager.create(timeTrack);
                                        }
                                    } else if (came.contains(".") && left.contains(".")) {
                                        hourCame = isNumber(came.split("\\.")[0]) ? Integer.parseInt(came.split("\\.")[0]) : 0;
                                        minCame = isNumber(came.split("\\.")[1]) ? Integer.parseInt(came.split("\\.")[1]) : 0;
                                        hourLeft = isNumber(left.split("\\.")[0]) ? Integer.parseInt(left.split("\\.")[0]) : 0;
                                        minLeft = isNumber(left.split("\\.")[1]) ? Integer.parseInt(left.split("\\.")[1]) : 0;
                                        if ((hourCame != 0 || minCame != 0) || (hourLeft != 0 || minLeft != 0)) {
                                            EdsTimeTrack timeTrack = new EdsTimeTrack();
                                            timeTrack.setEmployee(employee);
                                            timeTrack.setStatus(status);
                                            cal.set(Calendar.HOUR_OF_DAY, hourCame);
                                            cal.set(Calendar.MINUTE, minCame);
                                            cal.set(Calendar.SECOND, 0);
                                            if (leftDate != null) {
                                                timeTrack.setEndDate(ServerUtils.convertUserDateToServerDate(leftDate, timeZone));
                                                leftDate = null;
                                            } else {
                                                timeTrack.setStartDate(ServerUtils.convertUserDateToServerDate(cal.getTime(), timeZone));
                                            }
                                            cal.set(Calendar.HOUR_OF_DAY, hourLeft);
                                            cal.set(Calendar.MINUTE, minLeft);
                                            cal.set(Calendar.SECOND, 0);
                                            timeTrack.setEndDate(ServerUtils.convertUserDateToServerDate(cal.getTime(), timeZone));
                                            timeTrackManager.deleteEqualsStartDate(employee.getObjectID(), timeTrack.getStartDate());
                                            timeTrackManager.create(timeTrack);
                                        }
                                    } else if (isNumber(came) && isNumber(left)) {
                                        hourCame = Integer.parseInt(came);
                                        hourLeft = Integer.parseInt(left);
                                        EdsTimeTrack timeTrack = new EdsTimeTrack();
                                        timeTrack.setEmployee(employee);
                                        timeTrack.setStatus(status);
                                        cal.set(Calendar.HOUR_OF_DAY, hourCame);
                                        cal.set(Calendar.MINUTE, minCame);
                                        cal.set(Calendar.SECOND, 0);
                                        if (leftDate != null) {
                                            timeTrack.setEndDate(ServerUtils.convertUserDateToServerDate(leftDate, timeZone));
                                            leftDate = null;
                                        } else {
                                            timeTrack.setStartDate(ServerUtils.convertUserDateToServerDate(cal.getTime(), timeZone));
                                        }
                                        cal.set(Calendar.HOUR_OF_DAY, hourLeft);
                                        cal.set(Calendar.MINUTE, minLeft);
                                        cal.set(Calendar.SECOND, 0);

                                        timeTrack.setEndDate(ServerUtils.convertUserDateToServerDate(cal.getTime(), timeZone));
                                        timeTrackManager.deleteEqualsStartDate(employee.getObjectID(), timeTrack.getStartDate());
                                        timeTrackManager.create(timeTrack);
                                    }
                                    cal.setTime(tempTime);
                                }
                            }
                            step++;
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    values[1] = "Please use our template";
                    values[0] = null;
                }
            }
        }

        return values;
    }
    @Override
    @Transactional
    public String[] createAttendanceReportHoursUploadHandler(WfmCommand document) throws Throwable {
        Date currentDate;
        Date leftDate = null;
        String[] values = new String[2];
        Map<Integer, Calendar> mapDate;
        Map<String, EdsEmployee> employees = employeeManager.getEmployeeMapByCode();

        for (WfmMultipartFile file : document.getFiles()) {
            HSSFWorkbook wb = new HSSFWorkbook(file.getFile().getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            mapDate = getXslDate(sheet.getRow(3), values);
            currentDate = new Date();
            values[0] = "";

            for (int k = 5; k <= sheet.getLastRowNum(); k++) {
                HSSFRow row = sheet.getRow(k);
                if (row == null) continue;
                HSSFCell cell = row.getCell(0);
                HSSFCell cell2 = row.getCell(1);

                try {
                    if ((cell == null || cell.getStringCellValue().isEmpty()) &&
                        (cell2 == null || cell2.getStringCellValue().isEmpty() || !isNumber(cell2.getStringCellValue())))
                        continue;

                    String employeecode = cell.getStringCellValue();
                    EdsEmployee employee = employees.get(employeecode);

                    if (employee == null && cell2 != null && cell2.getStringCellValue() != null)
                        employee = employeeManager.getEmployeeByFirstNameViaLastName(cell2.getStringCellValue());

                    int step = 1;
                    if (employee != null) {
                        for (int i = 2; i < row.getLastCellNum(); i += 2) {
                            HSSFCell cellCame = row.getCell(i);
                            HSSFCell cellLeft = row.getCell(i + 1);
                            if (cellCame != null && cellLeft != null) {
                                String came = getCameCount(cellCame);
                                String left = getLeftCount(cellLeft, currentDate);

                                if (!came.isEmpty() && !left.isEmpty() && (!came.equals("00:00") || !left.equals("00:00"))) {
                                    Calendar cal = mapDate.get(step);
                                    Date tempTime = cal.getTime();

                                    int[] cameTime = parseTime(came);
                                    int[] leftTime = parseTime(left);

                                    if ((cameTime[0] != 0 || cameTime[1] != 0) || (leftTime[0] != 0 || leftTime[1] != 0)) {
                                        cal.set(Calendar.HOUR_OF_DAY, cameTime[0]);
                                        cal.set(Calendar.MINUTE, cameTime[1]);
                                        cal.set(Calendar.SECOND, 0);

                                        Calendar enddate = (Calendar) cal.clone();
                                        if (leftDate != null) {
                                            enddate.setTime(leftDate);
                                            leftDate = null;
                                        } else {
                                            enddate.set(Calendar.HOUR_OF_DAY, leftTime[0]);
                                            enddate.set(Calendar.MINUTE, leftTime[1]);
                                        }
                                        insertAttendaceHours(employee.getObjectID(), cal, enddate);
                                    }
                                    cal.setTime(tempTime);
                                }
                            }
                            step++;
                        }
                    }
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                    values[1] = "Please use our template";
                    values[0] = null;
                }
            }
        }

        return values;
    }

    private int[] parseTime(String time) {
        int hour = 0, minute = 0;
        if (time.contains(":")) {
            String[] parts = time.split(":");
            hour = isNumber(parts[0]) ? Integer.parseInt(parts[0]) : 0;
            minute = isNumber(parts[1]) ? Integer.parseInt(parts[1]) : 0;
        } else if (time.contains(".")) {
            String[] parts = time.split("\\.");
            hour = isNumber(parts[0]) ? Integer.parseInt(parts[0]) : 0;
            minute = isNumber(parts[1]) ? Integer.parseInt(parts[1]) : 0;
        } else if (isNumber(time)) {
            hour = Integer.parseInt(time);
        }
        return new int[]{hour, minute};
    }


    private void insertAttendaceHours(Integer employeeId, Calendar startDate, Calendar endDate) {
        Integer status = commonService.saveAttendanceHour(new EmployeePresentItem(employeeId, new DateNonConvertable(startDate.getTime()), new DateNonConvertable(startDate.getTime()), new DateNonConvertable(endDate.getTime()), null, null, null));
        if (status == Constants.ERROR || status ==Constants.WARNING)
            log.warn("Import isn't finished for user id:{}",employeeId);
    }

    private String getLeftCount(HSSFCell cellLeft, Date currentDate) {
        String left = "";
        try {
            if (!"".equals(cellLeft) && cellLeft.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                left = cellLeft.getStringCellValue();
            } else if (HSSFDateUtil.isCellDateFormatted(cellLeft)) {
                Date end = cellLeft.getDateCellValue();
                if (end != null) {
                    Calendar endCal = new GregorianCalendar();
                    endCal.setTime(end);
                    Calendar curCal = new GregorianCalendar();
                    curCal.setTime(currentDate);
                    left = endCal.get(Calendar.HOUR_OF_DAY) + ":" + endCal.get(Calendar.MINUTE);
                }
            } else if (cellLeft.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                left = String.valueOf(cellLeft.getNumericCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return left;
    }

    private String getCameCount(HSSFCell cellCame) {
        String came = "";
        try {
            if (!"".equals(cellCame) && cellCame.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                came = cellCame.getStringCellValue();
            } else if (!"".equals(cellCame) && HSSFDateUtil.isCellDateFormatted(cellCame)) {
                Date start = cellCame.getDateCellValue();
                if (start != null) {
                    Calendar cameCal = new GregorianCalendar();
                    cameCal.setTime(start);
                    came = cameCal.get(Calendar.HOUR_OF_DAY) + ":" + cameCal.get(Calendar.MINUTE);
                }
            } else if (!"".equals(cellCame) && cellCame.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                came = String.valueOf(cellCame.getNumericCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return came;
    }

    private Map<Integer, Calendar> getXslDate(HSSFRow row, String[] values) {
        Map<Integer, Calendar> map = new HashMap<>();
        try {
            for (int i = 2; i < row.getLastCellNum(); i += 2) {
                HSSFCell cell = row.getCell((short) i);
                if (cell == null) {
                    continue;
                }
                String str = cell.getStringCellValue();
                Calendar sCal = new GregorianCalendar();
                sCal.set(Calendar.YEAR, Integer.parseInt(str.split("\\.")[2]));
                sCal.set(Calendar.MONTH, Integer.parseInt(str.split("\\.")[1]) - 1);
                sCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.split("\\.")[0]));
                sCal.set(Calendar.HOUR, 0);
                sCal.set(Calendar.SECOND, 0);
                map.put(Integer.valueOf(str.split("\\.")[0]), sCal);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
//            setErrorString("Please uses our template");
            values[1] = "Please uses our template";
        }
        return map;
    }

    private boolean isNumber(String cellValue) {
        for (int i = 0; i < cellValue.length(); i++) {
            if ("0123456789".indexOf(cellValue.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Related chart of account backend
     *
     * @param wfmCommand - wfm command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createChartOfAccountsBackendHandler(WfmCommand wfmCommand) throws Throwable {
        accountTemplateManager.deleteAlAccountTemplates();

        String[] values = new String[2];

        for (WfmMultipartFile file : wfmCommand.getFiles()) {
            InputStreamReader isr;
            CsvReader reader = new CsvReader(isr = new InputStreamReader(file.getFile().getInputStream()), ',');
            int raw = 0;
            reader.readRecord();//the first header line - Code,Name,Type,Description,Expense Claims
            while (reader.readRecord()) {
                String code;
                try {
                    code = reader.get(0);
                    EdsAccountTemplate accountTemplate = accountTemplateManager.getAccountByCode(Integer.valueOf(code));
                    if (accountTemplate == null) {
                        accountTemplate = new EdsAccountTemplate();
                        accountTemplateManager.create(accountTemplate);
                    }
                    accountTemplate.setCode(Integer.valueOf(code));
                    accountTemplate.setCodeString(code);

                    accountTemplate.setName(reader.get(1));
                    EdsAccountType accountType = accountingManager.getAccountTypeByCode(reader.get(2));

                    if (accountType == null) {
                        values[1] = "Error while parsing, check the file entry format!" + " File = " + file.getFile() + " Raw = " + raw + ", Column = " + "2";
                        throw new Exception("Error while parsing, check the file entry format!" + " File = " + file.getFile() + " Raw = " + raw + ", Column(" + reader.get(2) + ") = " + "2");
                    }
                    accountTemplate.setAccountType(accountType);

                    String yesNo = reader.get(3);
                    if (yesNo != null) {
                        accountTemplate.setShowInExpense("yes".equalsIgnoreCase(yesNo));
                    }
                    yesNo = reader.get(4);
                    if (yesNo != null) {
                        accountTemplate.setEnablePayments("yes".equalsIgnoreCase(yesNo));
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    values[1] = "Error while parsing, check the file entry format!" + " Raw = " + raw + ", Column = " + "0";
                }
                raw++;
            }
            isr.close();
        }
        return values;
    }

    /**
     * Related chart of accounts
     *
     * @param wfmCommand - wfm command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createChartOfAccountsHandler(WfmCommand wfmCommand) throws Throwable {
        EdsUser user = accountingManager.getUser();
        String[] values = new String[2];
        List<Integer> deleteGLAccountIds = accountingManager.getGLAccountsInattendedInInvoices();
        accountingManager.deleteGLAccountsInattendedInInvoices();
        List<EdsAccount> list = Lists.newArrayList();
        for (WfmMultipartFile file : wfmCommand.getFiles()) {
            InputStreamReader isr;
            CsvReader reader = new CsvReader(isr = new InputStreamReader(file.getFile().getInputStream()), ',');
            int raw = 0;
            reader.readRecord();//the first header line - Code,Name,Type,Description,Expense Claims
            while (reader.readRecord()) {
                String code;
                try {
                    if (!"".equals(reader.get(0).trim()) || !"".equals(reader.get(1).trim()) || !"".equals(reader.get(2).trim())) {
                        code = reader.get(0);
                        EdsAccount account = accountingManager.getAccountByCode(/*Integer.valueOf(*/code/*)*/);
                        if (account == null) {
                            account = new EdsAccount();
                            account.setCreator(user);
                            account.setCreationTime(new Date());
                            accountingManager.create(account);
                        } else {
                            account.setUpdater(user);
                            account.setLastUpdatedDate(new Date());
                        }
                        account.setAccountCode(code);
                        account.setCodeString(code);
                        account.setName(reader.get(1));
                        EdsAccountType accountType;

                        String accountTypeCode = "EXPENSE".equals(reader.get(2)) ? EdsAccountType.DIRECT_EXPENSES : reader.get(2);
                        accountType = accountingManager.getAccountTypeByCode(accountTypeCode);

                        if (accountType == null) {
                            values[1] = "Error while parsing, check the file entry format!" + " File = " + file.getFile() + " Raw = " + raw + ", Column = " + "2";
                            throw new Exception("Error while parsing, check the file entry format!" + " File = " + file.getFile() + " Raw = " + raw + ", Column(" + reader.get(2) + ") = " + "2");
                        }
                        account.setAccountType(accountType);
                        String yesNo = reader.get(3);
                        if (yesNo != null) {
                            account.setShowInExpense("yes".equalsIgnoreCase(yesNo));
                        }
                        yesNo = reader.get(4);
                        if (yesNo != null) {
                            account.setEnablePayments("yes".equalsIgnoreCase(yesNo));
                        }
                        list.add(account);
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    values[1] = "Error while parsing, check the file entry format!" + " Raw = " + raw + ", Column = " + "0";
                }
                raw++;
            }
            isr.close();
        }

        if (!list.isEmpty()) {
            try {
                chartOfAccountSolrComponent.indexes(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (deleteGLAccountIds != null && !deleteGLAccountIds.isEmpty()) {
            try {
                solrManager.removeChartOfAccountByIds(deleteGLAccountIds.toArray(Integer[]::new));
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }

        return values;
    }

    /**
     * Related attachmentd upload
     *
     * @param documentCommand - document command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createAttachmentHandler(CreateDocumentCommand documentCommand) throws Throwable {
        String[] values = new String[2];
        ArrayList<EdsAttachment> attachments = new ArrayList<>();
        String returnVal = "";
        for (WfmMultipartFile file : documentCommand.getFiles()) {
            EdsAttachment attachment;
            if (documentCommand.getAttachmentID() != null) {
                attachment = attachmentManager.get(documentCommand.getAttachmentID());
                if (attachment == null) {
                    attachment = new EdsAttachment();
                }
            } else {
                attachment = new EdsAttachment();
            }

            attachment.setDownloadable(!StringUtils.isNotBlank(documentCommand.getNotdownloadable()));

            attachment.setDescription(ADD_DESCRIPTION.equals(file.getDescription()) ? "" : file.getDescription());
            attachment.setFolderName((!"".equals(documentCommand.getFolderName()) && documentCommand.getFolderName() != null) ? documentCommand.getFolderName() : "");
            String type = documentCommand.getImgType();
            boolean isImageType = (IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                    || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type));
            try {
                if (isImageType) {
                    ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                    if (documentCommand.getWithoutResize()) {
                        BufferedImage image = ImageIO.read(file.getFile().getInputStream());
                        documentCommand.setImageWidth(image.getWidth());
                        documentCommand.setImageHeight(image.getHeight());
                    }
                    if (documentCommand.getImageHeight() != null && documentCommand.getImageWidth() != null) {
                        Object[] objects = imageScaleDown.getAdvancedImageScaleDownInputStream(documentCommand.getImageWidth(), documentCommand.getImageHeight());
                        attachment.setInputStream((InputStream) objects[0]);
                    } else {
                        Object[] objects = imageScaleDown.getAdvancedImageScaleDownInputStream(600, 650);
                        attachment.setInputStream((InputStream) objects[0]);
                        attachment.setWidth((String) objects[1]);
                        attachment.setHeight((String) objects[2]);
                    }
                } else {
                    attachment.setInputStream(file.getFile().getInputStream());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            long size = 0;
            try {
                InputStream name = file.getFile().getInputStream();
                size = name.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
            attachment.setSize(size);
            if (documentCommand.getImageWidth() != null) {
                attachment.setWidth(documentCommand.getImageWidth().toString());
            }
            if (documentCommand.getImageHeight() != null) {
                attachment.setHeight(documentCommand.getImageHeight().toString());
            }

            String fileName = file.getFile().getName();
            if (file.getFile().getOriginalFilename().contains(".")) {
                fileName = file.getFile().getOriginalFilename().substring(0, file.getFile().getOriginalFilename().lastIndexOf("."));
            } else {
                if (type != null) {
                    if (file.getFile().getOriginalFilename().toLowerCase().contains(type.toLowerCase())) {
                        fileName = file.getFile().getOriginalFilename().substring(0, file.getFile().getOriginalFilename().lastIndexOf("."));
                    } else {
                        fileName = file.getFile().getOriginalFilename().concat(".").concat(type.toLowerCase());
                    }
                }
            }
            attachment.setOriginalName(file.getFile().getOriginalFilename());
            attachment.setContentType(file.getFile().getContentType());

            EdsReference uploadType;
            if (file.getUploadType() != null && file.getUploadType().equals(GOOGLE_DOCS_PARAM_NAME)) {
                uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.GOOGLE);
            } else if (file.getUploadType() != null && file.getUploadType().equals(OFFICE_365_DOCS_PARAM_NAME)) {
                uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365);
            } else if (file.getUploadType() != null && file.getUploadType().equals(OFFICE_365_DOCS_SHARE_POINT_PARAM_NAME)) {
                uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365_SHARE_POINT);
            } else if (file.getUploadType() != null && file.getUploadType().equals(LOCAL_PARAM_NAME)) {
                uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.LOCAL);
            } else if (file.getUploadType() != null && file.getUploadType().equals(MINIO_PARAM_NAME)) {
                uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.MINIO);
            } else {
                uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.AMAZON);
            }
            attachment.setType(uploadType);

            if (file.getFile().getSize() < AmazonManagerImpl.MAX_FILE_SIZE) {
                if (isImageType) {
                    ImageLoader image = new ImageLoader(file.getFile().getInputStream());
                    Integer width = -1;
                    Integer height = -1;
                    if (BMT_SURVEY_LOGO.equals(documentCommand.getLogoType())) {
                        width = 235;
                        height = 58;
                    }
                    if (((width < 0 && height < 0) || (image.getImageWidth() < width && image.getImageHeight() < height))) {
                        attachment.setFolderName(documentCommand.getFolderName());
                        if (documentCommand.getParameters() != null && "true".equals(documentCommand.getParameters().get(MULTIPLE_FILES))) {
                            attachments.add(attachment);
                        } else {
                            returnVal = createOrUpdateAttachment(values, returnVal, attachment, null, IMAGE_SIZE_LARGE);
                        }

                        //Amazon static fayllar uchun;
                        if (isImageType && documentCommand.getFolderName() != null && !"".equals(documentCommand.getFolderName()) && STATIC_FOLDER.equals(documentCommand.getFolderName())
                                && !documentCommand.getWithoutResize()) {
                            attachment.setImageSize(IMAGE_SIZE_SMALL);
                            //create other resized images
                            //medium formatda saqlash; Medium format: 300px X 325px;
                            EdsAttachment childAttachment = new EdsAttachment();
                            if (attachment.getObjectID() != null) {
                                childAttachment = (EdsAttachment) uploadManager.getImageWithParentAndSize(attachment, IMAGE_SIZE_MEDIUM);
                                if (childAttachment == null) {
                                    childAttachment = new EdsAttachment();
                                }
                            }
                            ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                            Object[] objects = imageScaleDown.getAdvancedImageScaleDownInputStream(300, 325);
                            childAttachment.setInputStream((InputStream) objects[0]);
                            childAttachment.setSize(size);
                            childAttachment.setOriginalName(fileName + "_" + objects[1] + "x" + objects[2] + "." + type);
                            childAttachment.setContentType(file.getFile().getContentType());
                            childAttachment.setFolderName(STATIC_FOLDER);
                            childAttachment.setDownloadable(!StringUtils.isNotBlank(documentCommand.getNotdownloadable()));
                            createOrUpdateAttachment(new String[2], "", childAttachment, attachment, IMAGE_SIZE_MEDIUM);

                            //large formatda saqlash; Large format: 600px X 650px;
                            childAttachment = new EdsAttachment();
                            if (attachment.getObjectID() != null) {
                                childAttachment = (EdsAttachment) uploadManager.getImageWithParentAndSize(attachment, IMAGE_SIZE_LARGE);
                                if (childAttachment == null) {
                                    childAttachment = new EdsAttachment();
                                }
                            }
                            imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                            objects = imageScaleDown.getAdvancedImageScaleDownInputStream(600, 650);
                            childAttachment.setInputStream((InputStream) objects[0]);
                            childAttachment.setSize(size);
                            childAttachment.setOriginalName(fileName + "_" + objects[1] + "x" + objects[2] + "." + type);
                            childAttachment.setContentType(file.getFile().getContentType());
                            childAttachment.setFolderName(STATIC_FOLDER);
                            childAttachment.setDownloadable(!StringUtils.isNotBlank(documentCommand.getNotdownloadable()));
                            createOrUpdateAttachment(new String[2], "", childAttachment, attachment, IMAGE_SIZE_LARGE);

                            //original formatda saqlash;
                            childAttachment = new EdsAttachment();
                            if (attachment.getObjectID() != null) {
                                childAttachment = (EdsAttachment) uploadManager.getImageWithParentAndSize(attachment, IMAGE_SIZE_ORIGINAL);
                                if (childAttachment == null) {
                                    childAttachment = new EdsAttachment();
                                }
                            }
                            childAttachment.setInputStream(file.getFile().getInputStream());
                            childAttachment.setSize(size);
                            childAttachment.setOriginalName(fileName + "_" + image.getImageWidth() + "x" + image.getImageHeight() + "." + type);
                            childAttachment.setContentType(file.getFile().getContentType());
                            childAttachment.setFolderName(STATIC_FOLDER);
                            childAttachment.setDownloadable(!StringUtils.isNotBlank(documentCommand.getNotdownloadable()));
                            createOrUpdateAttachment(new String[2], "", childAttachment, attachment, IMAGE_SIZE_ORIGINAL);
                        }
                    } else {
                        if (width > 0 && height > 0) {
                            values[1] = "Image size should not be greater than " + width + "px x " + height + "px.";
                        } else {
                            values[1] = "Image size should not be greater.";
                        }
                    }
                } else {
                    if (documentCommand.getParameters() != null && "true".equals(documentCommand.getParameters().get(MULTIPLE_FILES))) {
                        attachments.add(attachment);
                    } else {
                        returnVal = createOrUpdateAttachment(values, returnVal, attachment, null, null);
                    }
                }
            } else {
                values[1] = "Document size should not\n be greater than allowed size.";
            }
        }
        if (documentCommand.getParameters() != null && "true".equals(documentCommand.getParameters().get(MULTIPLE_FILES))) {
            returnVal = createOrUpdateAttachment2(values, returnVal, attachments);
        }
        values[0] = returnVal;

        return values;
    }

    private String createOrUpdateAttachment2(String[] values, String returnVal, ArrayList<EdsAttachment> attachments) {
        Integer tempID = null;
        for (EdsAttachment attachment : attachments) {
            try {
                if (attachment.getObjectID() == null) {
                    if (tempID == null) {
                        attachmentManager.create(attachment);
                        tempID = attachment.getObjectID();
                    } else if (tempID != null) {
                        attachment.setAttachmentId(tempID);
                        attachmentManager.create(attachment);
                    }
                }
            } catch (Exception ex) {
                values[1] = ex.getMessage();
                ex.printStackTrace();
            }
        }
        return tempID == null ? returnVal : tempID.toString();
    }

    private String createOrUpdateAttachment(String[] values, String returnVal, EdsAttachment attachment, EdsAttachment parentAttachment, String imageSize) {
        try {
            if (attachment.getObjectID() == null) {
                if (BMT_SURVEY_LOGO.equals(attachment.getDescription())) {
                    attachment.setCreator(attachmentManager.getUser());
                    attachment.setAttachmentId(0);
                }
                attachmentManager.create(attachment);
                returnVal = attachment.getObjectID().toString();
            } else {
                attachmentManager.update(attachment);
                returnVal = attachment.getObjectID().toString();
            }
            //Bu Amazon static folderga imageni 4 ta razmerda saqlash uchun kerak;
            if (parentAttachment != null && imageSize != null) {
                attachment.setParent(parentAttachment);
                attachment.setImageSize(imageSize);
                attachmentManager.update(attachment);
            }
        } catch (Exception ex) {
            values[1] = ex.getMessage();
            ex.printStackTrace();
        }
        return returnVal;
    }

    /**
     * Related network attachment
     *
     * @param documentCommand - document command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createNetworkAttachmentHandler(CreateDocumentCommand documentCommand) throws Throwable {
        String[] values = new String[2];
        MultipartFile file = documentCommand.getFiles()[0].getFile();

        EdsAttachment attachment = new EdsAttachment();
        attachment.setInputStream(file.getInputStream());
        attachment.setOriginalName(file.getOriginalFilename());
        attachment.setContentType(Utils.determineContentType(file.getContentType(), file.getOriginalFilename()));
        attachment.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType()));

        try {
            InputStream name = file.getInputStream();
            attachment.setSize((long) name.available());
        } catch (IOException ex) {
            ex.printStackTrace();
            values[1] = ("Image size should not be greater than maximum allowed size.");
        }

        String returnValue = "";
        try {
            ImageLoader image = new ImageLoader(file.getInputStream());
            Integer width, height;
            if ((width = documentCommand.getImageWidth()) == null) {
                width = 240;
            }
            if ((height = documentCommand.getImageHeight()) == null) {
                height = 85;
            }

            if (image.getImageWidth() < width && image.getImageHeight() < height && file.getSize() < AmazonManagerImpl.MAX_FILE_SIZE) {
                attachmentManager.create(attachment);
                returnValue = attachment.getObjectID().toString();
            } else {
                //Current way is stupid, but now there was no other optimal way to fix current problem, so through 'FAIL' string
                //we are defining that there was error. Then we have parsed it in WfmFormPanel class.
                values[1] = (FAIL + "Image size should not be greater than maximum allowed size.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            values[1] = ("Image size should not be greater than maximum allowed size.");
        }

        values[0] = returnValue;

        return values;
    }

    /**
     * Related product category pictures
     *
     * @param documentCommand - document command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createProductCategoryPicturesHandler(ProductCategoryDocumentCommand documentCommand) throws Throwable {
        String[] values = new String[2];

        EdsUser user = productCategoryPictureManager.getUser();

        EdsProductCategoryPicture picture = new EdsProductCategoryPicture();
        picture.setCategory(productCategoryManager.get(documentCommand.getCategoryID()));
        picture.setCreatedBy(user);

        WfmMultipartFile file = documentCommand.getFiles()[0];

        String returnValue = "";
        if (file.getFile() != null) {
            picture.setOriginalName(file.getFile().getOriginalFilename());
            picture.setName(file.getFile().getOriginalFilename());
            picture.setContentType(Utils.determineContentType(file.getFile().getContentType(), file.getFile().getOriginalFilename()));

            EdsProductCategoryPicture clonePicture = picture.cloneShallow();

            picture.setFileSizeType(FILE_SIZE_DEFAULT);

            try {
                String type = documentCommand.getImgType();
                if ((IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                        || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type))) {
                    ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                    picture.setInputStream((InputStream) imageScaleDown.getAdvancedImageScaleDownInputStream(400, 300)[0]);
                } else {
                    picture.setInputStream(file.getFile().getInputStream());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            try {
                productCategoryPictureManager.create(picture);
                returnValue = picture.getObjectID().toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                values[1] = ("Error during upload.");
            }

            if (returnValue != null) {
                // Medium image
                EdsProductCategoryPicture mediumPicture = clonePicture.cloneShallow();
                mediumPicture.setParentId(picture.getObjectID());
                mediumPicture.setFileSizeType(FILE_SIZE_MEDIUM);
                try {
                    String type = documentCommand.getImgType();
                    if ((IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                            || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type))) {
                        ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                        mediumPicture.setInputStream((InputStream) imageScaleDown.getAdvancedImageScaleDownInputStream(300, 300)[0]);
                    } else {
                        mediumPicture.setInputStream(file.getFile().getInputStream());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    productCategoryPictureManager.create(mediumPicture);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    values[1] = ("Error during upload.");
                }

                // Small image
                EdsProductCategoryPicture smallPicture = clonePicture.cloneShallow();
                smallPicture.setParentId(picture.getObjectID());
                smallPicture.setFileSizeType(FILE_SIZE_SMALL);
                try {
                    String type = documentCommand.getImgType();
                    if ((IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                            || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type))) {
                        ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                        smallPicture.setInputStream((InputStream) imageScaleDown.getAdvancedImageScaleDownInputStream(102, 78)[0]);
                    } else {
                        smallPicture.setInputStream(file.getFile().getInputStream());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    productCategoryPictureManager.create(smallPicture);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    values[1] = ("Error during upload.");
                }
            }
        }
        values[0] = returnValue;

        return values;
    }

    /**
     * Related product pictures
     *
     * @param documentCommand - document command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] createProductPicturesHandler(ProductDocumentCommand documentCommand) throws Throwable {
        String returnCombined = "";
        String[] values = new String[2];
        EdsUser user = productPictureManager.getUser();
        for (int i = 0; i < documentCommand.getFiles().length; i++) {

            EdsProductPicture picture = new EdsProductPicture();

            if (documentCommand.getProductID() != null) {
                picture.setProduct(itemManager.get(documentCommand.getProductID()));
            }
            picture.setCreatedBy(user);

            WfmMultipartFile file = documentCommand.getFiles()[i];

            String returnValue = "";
            if (file.getFile() != null) {
                picture.setOriginalName(file.getFile().getOriginalFilename());
                picture.setName(file.getFile().getOriginalFilename());
                picture.setContentType(Utils.determineContentType(file.getFile().getContentType(), file.getFile().getOriginalFilename()));

                EdsProductPicture clonePicture = picture.cloneShallow();

                picture.setFileSizeType(FILE_SIZE_DEFAULT);

                try {
                    String type = documentCommand.getImgType();
                    if ((IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                            || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type))) {
                        ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                        picture.setInputStream((InputStream) imageScaleDown.getAdvancedImageScaleDownInputStream(400, 300)[0]);
                    } else {
                        picture.setInputStream(file.getFile().getInputStream());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    productPictureManager.create(picture);
                    returnValue = picture.getObjectID().toString();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    values[1] = ("Error during upload.");
                }

                if (returnValue != null) {
                    // Medium image
                    EdsProductPicture mediumPicture = clonePicture.cloneShallow();
                    mediumPicture.setParentId(picture.getObjectID());
                    mediumPicture.setFileSizeType(FILE_SIZE_MEDIUM);
                    try {
                        String type = documentCommand.getImgType();
                        if ((IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                                || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type))) {
                            ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                            mediumPicture.setInputStream((InputStream) imageScaleDown.getAdvancedImageScaleDownInputStream(300, 300)[0]);
                        } else {
                            mediumPicture.setInputStream(file.getFile().getInputStream());
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        productPictureManager.create(mediumPicture);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        values[1] = ("Error during upload.");
                    }

                    // Small image
                    EdsProductPicture smallPicture = clonePicture.cloneShallow();
                    smallPicture.setParentId(picture.getObjectID());
                    smallPicture.setFileSizeType(FILE_SIZE_SMALL);
                    try {
                        String type = documentCommand.getImgType();
                        if ((IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                                || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type))) {
                            ImageScaleDown imageScaleDown = new ImageScaleDown(file.getFile().getInputStream(), type.toLowerCase());
                            smallPicture.setInputStream((InputStream) imageScaleDown.getAdvancedImageScaleDownInputStream(102, 78)[0]);
                        } else {
                            smallPicture.setInputStream(file.getFile().getInputStream());
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        productPictureManager.create(smallPicture);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        values[1] = ("Error during upload.");
                    }

                    // Original image
                    EdsProductPicture originalPicture = clonePicture.cloneShallow();
                    originalPicture.setParentId(picture.getObjectID());
                    originalPicture.setFileSizeType(FILE_SIZE_ORIGINAL);
                    try {
                        String type = documentCommand.getImgType();
                        if ((IMG_JPG.equalsIgnoreCase(type) || IMG_JPEG.equalsIgnoreCase(type) || IMG_PNG.equalsIgnoreCase(type)
                                || IMG_GIF.equalsIgnoreCase(type) || IMG_BMP.equalsIgnoreCase(type) || IMG_ICO.equalsIgnoreCase(type))) {
                            originalPicture.setInputStream(file.getFile().getInputStream());
                        } else {
                            originalPicture.setInputStream(file.getFile().getInputStream());
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        productPictureManager.create(originalPicture);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        values[1] = ("Error during upload.");
                    }
                }

            }
            returnCombined = returnCombined + returnValue + ",";
            values[0] = returnCombined;
        }
        return values;
    }

    /**
     * Related task comment
     *
     * @param commentCommand - comment command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public void createTaskCommentHandler(CreateTaskCommentCommand commentCommand) throws Throwable {
        String[] values = new String[2];
        EdsEmployee employee = (EdsEmployee) taskManager.getUser();
        EdsTaskComment comment = new EdsTaskComment();
        comment.setCreationDate(new Date());
        comment.setText(commentCommand.getText());
        if (commentCommand.getTaskUID() != null) {
            comment.setTask(taskManager.get(commentCommand.getTaskUID()));
        } else {
            throw new Exception("TaskComment must have target task set!");
        }
        comment.setUser(employee);
        taskCommentManager.create(comment);
    }

    /**
     * Related wfp attachments
     *
     * @param documentCommand - document command
     * @return
     * @throws Throwable
     */
    /*@Override
    @Transactional
    public String[] createWfpAttachmentsHandler(WfpDocumentCommand documentCommand) throws Throwable {
        String[] values = new String[2];
        StringBuilder returnValue = new StringBuilder();
        EdsUser user = attachmentsManager.getUser();

        if (documentCommand.getFiles() != null && !documentCommand.getFiles().equals("")) {
            for (int i = 0; i < documentCommand.getFiles().length; i++) {
                WfmMultipartFile file = documentCommand.getFiles()[i];
                if (file != null && file.getFile() != null) {

                    EdsWfpAttachments picture = new EdsWfpAttachments();
                    picture.setCreatedBy(user);
                    picture.setName(file.getFile().getOriginalFilename());
                    picture.setOriginalName(file.getFile().getOriginalFilename());
                    picture.setContentType(Utils.determineContentType(file.getFile().getContentType(), file.getFile().getOriginalFilename()));

                    EdsReference uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.AMAZON);
                    picture.setType(uploadType);

                    try {
                        picture.setInputStream(file.getFile().getInputStream());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {

                        if (file.getDescription().equals(WEBSITE_LAYOUT_ZIP_TEMPLATE)) {
                            unZipTemplates(file, documentCommand.getTemplateFolderName());
                        }
                        switch (file.getDescription()) {
                            case WEBSITE_IMAGE_FILE:
                            case WEBSITE_PDF_FILE:
                                EdsAttachment fileNews = new EdsAttachment();
                                fileNews.setCreator(picture.getCreatedBy());
                                fileNews.setOriginalName(picture.getName());
                                fileNews.setContentType(picture.getContentType());
                                fileNews.setType(picture.getType());
                                fileNews.setInputStream(picture.getInputStream());
                                attachmentManager.create(fileNews);
                                returnValue.append(file.getDescription()).append("=").append(fileNews.getObjectID()).append(";");
                                break;
                            case PUBLIC_EVENT_LOGO:
                                picture.setType(picture.getType());
                                if (documentCommand.getHeight() != null && documentCommand.getWidth() != null) {
                                    ImageScaleDown imageScaleDown = new ImageScaleDown(picture.getInputStream(), documentCommand.getImageType());
                                    picture.setInputStream(imageScaleDown.createResizedCopy(documentCommand.getWidth(), documentCommand.getHeight(), false));
                                } else {
                                    picture.setInputStream(picture.getInputStream());
                                }
                                attachmentsManager.create(picture);
                                returnValue.append(file.getDescription()).append("=").append(picture.getObjectID()).append(";");

                                break;
                            default:
                                attachmentsManager.create(picture);
                                returnValue.append(file.getDescription()).append("=").append(picture.getObjectID()).append(";");
                                break;
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        values[1] = ("Error during upload.");
                    }
                }
            }
        }

        values[0] = returnValue.toString();
        return values;
    }
*/

    /**
     * Related import bank transactions
     *
     * @param documentCommand - document command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public String[] importBankTransactionsHandler(BankAccountDocumentCommand documentCommand) throws Throwable {
        String[] values = new String[2];

        EdsBankAccountAttachment attachment = new EdsBankAccountAttachment();
        attachment.setBankAccount(bankAccountManager.get(documentCommand.getBankAccountID()));

        WfmMultipartFile file = documentCommand.getFiles()[0];

        String returnValue = "";
        if (file.getFile() != null) {
            attachment.setOriginalName(file.getFile().getOriginalFilename());
            attachment.setContentType(Utils.determineContentType(file.getFile().getContentType(), file.getFile().getOriginalFilename()));
            attachment.setBankAccAttchType(referenceManager.findReference(_BANK_ACCOUNT_TYPE, documentCommand.getBankAccAttchType()));
            attachment.setImported(Boolean.FALSE);
            try {
                attachment.setInputStream(file.getFile().getInputStream());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            try {
                bankAccountAttachmentManager.create(attachment);
                returnValue = attachment.getObjectID().toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                values[1] = ("Error during upload.");
            }
        }
        values[0] = returnValue;

        return values;
    }

    @Override
    @Transactional
    public String[] createReportingExcelTemplateAttachmentHandler(WfmCommand documentCommand) throws Throwable {
        String[] values = new String[3];
        StringBuilder returnValue = new StringBuilder();
        String companyId = ServerSecurityContext.getInstance().getCompanyId();

        if (documentCommand.getCompanyId() != null && !"".equals(documentCommand.getCompanyId())) {
            ServerSecurityContext.getInstance().setSessionId(null);
            ServerSecurityContext.getInstance().setCompanyId(documentCommand.getCompanyId());
        }

        if (documentCommand.getFiles() != null && !documentCommand.getFiles().equals("")) {
            for (int i = 0; i < documentCommand.getFiles().length; i++) {
                WfmMultipartFile file = documentCommand.getFiles()[i];
                if (file != null && file.getFile() != null) {

                    EdsUpload excelFile = new EdsUpload();
                    excelFile.setOriginalName(file.getFile().getOriginalFilename());
                    excelFile.setContentType(Utils.determineContentType(file.getFile().getContentType(), file.getFile().getOriginalFilename()));

                    EdsReference uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType());
                    excelFile.setType(uploadType);

                    try {
                        excelFile.setInputStream(file.getFile().getInputStream());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        uploadManager.create(excelFile);
                        returnValue.append(file.getDescription()).append("=").append(excelFile.getObjectID()).append(";");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        values[1] = ("Error during upload.");
                    }
                }
            }
        }

        values[0] = returnValue.toString();
        values[2] = companyId;
        return values;
    }

    /**
     * Related ms project file upload
     *
     * @param command - command
     * @return
     * @throws Throwable
     */
    @Override
    @Transactional
    public void mSProjectFileUploadHandler(WfmCommand command) throws Throwable {
        if (command.getFiles() != null) {
            command.getFiles();
            for (WfmMultipartFile file : command.getFiles()) {
                ProjectFile projectFile = new ProjectFile();
                // create default reader - this is read only .mpp file
                ProjectReader reader = new MPPReader();
                try {
                    // if file is xml format then change Reader type to MSPDIReader
                    if (file.getFile().getOriginalFilename().contains(".xml")) {
                        reader = new MSPDIReader();
                    }
                    projectFile = reader.read(file.getFile().getInputStream());
                } catch (MPXJException e) {
                    e.printStackTrace();
                }
                if (projectFile != null) {
                    LinkedList<Task> taskList = (LinkedList<Task>) projectFile.getAllTasks();
                    if (taskList != null && !taskList.isEmpty()) {
                        taskService.importDataFromMPPFile(taskList, command.getParameters());
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public String[] importTestResultsUploadHandler(WfmCommand command) throws Throwable {
        String[] result = new String[2];
        if (command.getFiles() != null) {
            command.getFiles();
            for (WfmMultipartFile file : command.getFiles()) {
                result = tcService.importXML(file.getFile().getInputStream());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public String putFileToPublicFolder(EdsUpload upload, InputStream stream) throws Throwable {
        String s = null;
        try {
            s = uploadManager.putFileToPublicFolder(upload, stream);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    @Transactional
    public String copyUploadDocumentSize() {
        return String.valueOf(uploadManager.copyCompanyDocumentsSizeToUploadTable(1));
    }

    @Override
    @Transactional
    public String[] uploadProductSerials(WfmCommand command) throws Throwable {
        String[] values = new String[2];
        log.info("-------------------------------{{  STARTED UPLOAD SERIALS }}-------------------------------");
        if (command == null || command.getFiles() == null || command.getFiles().length == 0) {
            log.info("-------------------------------{{  ERROR: no more files }}-------------------------------");
            values[1] = "Error while parsing, check the file entry format!";
            return values;
        }
        String paramValue = "5";
        final Object columnCountParam = command.getParameters().get("columnCount");

        if (columnCountParam instanceof String[]) {
            paramValue = ((String[]) columnCountParam)[0];
        }
        int columnCount = Optional.of(Integer.valueOf(paramValue)).orElse(5);

        StringBuilder value = new StringBuilder();

        for (WfmMultipartFile file : command.getFiles()) {
            final InputStreamReader isr = new InputStreamReader(file.getFile().getInputStream());
            final CsvReader reader = new CsvReader(isr, ',');
            int row = 0;
            reader.readRecord();//the first header line - Product Number,Serail Number,Expiration Date
            while (reader.readRecord()) {
                try {
//                        value.append(reader.get(0)).append("=").append(reader.get(1)).append("=").append(reader.get(2)).append("=").append(reader.get(3)).append("=").append(reader.get(4)).append(";");
                    for (int i = 0; i < columnCount; i++) {
                        value.append(reader.get(i));
                        if (i == columnCount - 1) {
                            value.append(";");
                            continue;
                        }
                        value.append("=");
                    }
                } catch (IOException e) {
                    log.info("-------------------------------{{  ERROR: Error while parsing file }}-------------------------------");
                    e.printStackTrace();
                    values[1] = "Error while parsing, check the file entry format!" + " Row = " + row;
                }
                row++;
            }
            isr.close();
        }
        values[0] = value.toString().replace("==;", "");
        log.info("-------------------------------{{  VALUES: " + values[0] + " }}-------------------------------");
        return values;
    }


}
