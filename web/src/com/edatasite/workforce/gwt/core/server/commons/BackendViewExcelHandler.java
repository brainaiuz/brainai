package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsBackendManagement;
import com.edatasite.workforce.core.domain.EdsCompanyStatistic;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyList;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BackendManagementManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyStatisticManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.08.2009
 * Time: 18:56:19
 * To change this template use File | Settings | File Templates.
 */
public class BackendViewExcelHandler extends ExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(BackendViewExcelHandler.class);

    @Autowired
    private CompanyStatisticManager statisticManager;

    @Autowired
    private BackendManagementManager backendManagementManager;

    Format formatter = new SimpleDateFormat("MM/dd/yy");

    private Date from;
    private Date to;
    private boolean companyID = true;
    private boolean phone = true;
    private boolean registrationDate = true;
    private boolean accsessCount = true;
    private boolean firstAccessDate = true;
    private boolean lastAccessDate = true;
    private boolean signUpFrom = true;
    private boolean subscriptionType = true;
    private boolean paymentstatus = true;
    private boolean periodAccess = true;
    private boolean userCount = true;
    private boolean activeUsersCount = true;
    private boolean projectCount = true;
    private boolean taskCount = true;
    private boolean timeSheetCount = true;
    private boolean clientCount = true;
    private boolean supplierCount = true;
    private boolean leadCount = true;
    private boolean contactCount = true;
    private boolean crmTaskCount = true;
    private boolean eventCount = true;
    private boolean caseCount = true;
    private boolean invoiceCount = true;
    private boolean expenseCount = true;
    private boolean productCount = true;
    private boolean folderCount = true;
    private boolean fileCount = true;
    private boolean email = true;
    private boolean countryname = true;
    private boolean expirationDate = true;
    private boolean hostName = true;
    private boolean adminNames = true;
    private boolean affiliates = true;
    private boolean compaings = true;
    private boolean sourcees = true;
    private boolean noAccessUserCount = true;
    private int colCount;
    private Integer limit;

    protected HSSFWorkbook getWorkBook(HttpServletRequest request) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {

            from = df.parse(request.getParameter("from"));
            to = df.parse(request.getParameter("to"));
            limit = Integer.valueOf(request.getParameter("limit"));

            companyID = Boolean.valueOf(request.getParameter("companyID"));
            phone = Boolean.valueOf(request.getParameter("phone"));
            registrationDate = Boolean.valueOf(request.getParameter("registrationDate"));
            accsessCount = Boolean.valueOf(request.getParameter("accsessCount"));
            firstAccessDate = Boolean.valueOf(request.getParameter("firstAccessDate"));
            lastAccessDate = Boolean.valueOf(request.getParameter("lastAccessDate"));
            signUpFrom = Boolean.valueOf(request.getParameter("signUpFrom"));
            subscriptionType = Boolean.valueOf(request.getParameter("subscriptionType"));
            paymentstatus = Boolean.valueOf(request.getParameter("paymentstatus"));
            periodAccess = Boolean.valueOf(request.getParameter("periodAccess"));
            userCount = Boolean.valueOf(request.getParameter("userCount"));
            activeUsersCount = Boolean.valueOf(request.getParameter("activeUsersCount"));
            projectCount = Boolean.valueOf(request.getParameter("projectCount"));
            taskCount = Boolean.valueOf(request.getParameter("taskCount"));
            timeSheetCount = Boolean.valueOf(request.getParameter("timeSheetCount"));
            clientCount = Boolean.valueOf(request.getParameter("clientCount"));
            supplierCount = Boolean.valueOf(request.getParameter("supplierCount"));
            leadCount = Boolean.valueOf(request.getParameter("leadCount"));
            contactCount = Boolean.valueOf(request.getParameter("contactCount"));
            crmTaskCount = Boolean.valueOf(request.getParameter("crmTaskCount"));
            eventCount = Boolean.valueOf(request.getParameter("eventCount"));
            caseCount = Boolean.valueOf(request.getParameter("caseCount"));
            invoiceCount = Boolean.valueOf(request.getParameter("invoiceCount"));
            expenseCount = Boolean.valueOf(request.getParameter("expenseCount"));
            productCount = Boolean.valueOf(request.getParameter("productCount"));
            folderCount = Boolean.valueOf(request.getParameter("folderCount"));
            fileCount = Boolean.valueOf(request.getParameter("fileCount"));
            email = Boolean.valueOf(request.getParameter("adminEmail"));
            countryname = Boolean.valueOf(request.getParameter("country"));
            expirationDate = Boolean.valueOf(request.getParameter("endDate"));
            hostName = Boolean.valueOf(request.getParameter("hostName"));
            adminNames = Boolean.valueOf(request.getParameter("adminName"));
            affiliates = Boolean.valueOf(request.getParameter("affiliate"));
            compaings = Boolean.valueOf(request.getParameter("compaing"));
            sourcees = Boolean.valueOf(request.getParameter("source"));
            noAccessUserCount = Boolean.valueOf(request.getParameter("noAccessUserCount"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        colCount = 1;

        if (companyID) {
            colCount++;
        }

        if (phone) {
            colCount++;
        }

        if (registrationDate) {
            colCount++;
        }

        if (accsessCount) {
            colCount++;
        }
        if (firstAccessDate) {
            colCount++;
        }
        if (lastAccessDate) {
            colCount++;
        }
        if (signUpFrom) {
            colCount++;
        }

        if (subscriptionType) {
            colCount++;
        }

        if (paymentstatus) {
            colCount++;
        }

        if (periodAccess) {
            colCount++;
        }

        if (userCount) {
            colCount++;
        }

        if (activeUsersCount) {
            colCount++;
        }

        if (projectCount) {
            colCount++;
        }

        if (taskCount) {
            colCount++;
        }

        if (timeSheetCount) {
            colCount++;
        }

        if (clientCount) {
            colCount++;
        }

        if (supplierCount) {
            colCount++;
        }

        if (leadCount) {
            colCount++;
        }

        if (contactCount) {
            colCount++;
        }

        if (crmTaskCount) {
            colCount++;
        }

        if (eventCount) {
            colCount++;
        }

        if (caseCount) {
            colCount++;
        }

        if (invoiceCount) {
            colCount++;
        }

        if (expenseCount) {
            colCount++;
        }

        if (productCount) {
            colCount++;
        }

        if (folderCount) {
            colCount++;
        }

        if (fileCount) {
            colCount++;
        }

        if (email) {
            colCount++;
        }

        if (countryname) {
            colCount++;
        }

        if (expirationDate) {
            colCount++;
        }
        if (hostName) {
            colCount++;
        }
        if (adminNames) {
            colCount++;
        }
        if (affiliates) {
            colCount++;
        }
        if (compaings) {
            colCount++;
        }
        if (sourcees) {
            colCount++;
        }
        if (noAccessUserCount) {
            colCount++;
        }

        if (limit == null) {
            limit = 1000;
        }
        CompanyList companyList = getOptinalExport();
        List<CompanyListItem> companies = companyList.getList();
        ExcelData[] cellDatas;
        List<ExcelData[]> list = new LinkedList<>();

        try {
            cellDatas = new ExcelData[]{
                    new ExcelData(" Total subscriptions : " + companyList.getTotal(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3),
            };
            list.add(cellDatas);

            cellDatas = new ExcelData[colCount];
            int i = 0;
            if (companyID) {
                cellDatas[i++] = new ExcelData("company ID", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            cellDatas[i++] = new ExcelData("Company Name", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            if (phone) {
                cellDatas[i++] = new ExcelData("Phone", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (registrationDate) {
                cellDatas[i++] = new ExcelData("Registration Date", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (accsessCount) {
                cellDatas[i++] = new ExcelData("Access Count", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (firstAccessDate) {
                cellDatas[i++] = new ExcelData("First Access Date", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (lastAccessDate) {
                cellDatas[i++] = new ExcelData("Last Access Date", ExcelData.STRING, 8, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (signUpFrom) {
                cellDatas[i++] = new ExcelData("Sign Up From", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (subscriptionType) {
                cellDatas[i++] = new ExcelData("Subscription Type", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (paymentstatus) {
                cellDatas[i++] = new ExcelData("Payment Status", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (periodAccess) {
                cellDatas[i++] = new ExcelData("Period Access", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (userCount) {
                cellDatas[i++] = new ExcelData("User Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (activeUsersCount) {
                cellDatas[i++] = new ExcelData("Active Users Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (projectCount) {
                cellDatas[i++] = new ExcelData("Project Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (taskCount) {
                cellDatas[i++] = new ExcelData("Task Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (timeSheetCount) {
                cellDatas[i++] = new ExcelData("TimeSheet Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (clientCount) {
                cellDatas[i++] = new ExcelData("Client Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (supplierCount) {
                cellDatas[i++] = new ExcelData("Supplier Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (leadCount) {
                cellDatas[i++] = new ExcelData("Lead Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (contactCount) {
                cellDatas[i++] = new ExcelData("Contact Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (crmTaskCount) {
                cellDatas[i++] = new ExcelData("CRM Task Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (eventCount) {
                cellDatas[i++] = new ExcelData("Event Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (caseCount) {
                cellDatas[i++] = new ExcelData("Case Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (invoiceCount) {
                cellDatas[i++] = new ExcelData("Invoice Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (expenseCount) {
                cellDatas[i++] = new ExcelData("Expense Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (productCount) {
                cellDatas[i++] = new ExcelData("Product Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (folderCount) {
                cellDatas[i++] = new ExcelData("Folder Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (fileCount) {
                cellDatas[i++] = new ExcelData("File Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (email) {
                cellDatas[i++] = new ExcelData("Admin Email", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (countryname) {
                cellDatas[i++] = new ExcelData("Country", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            if (expirationDate) {
                cellDatas[i++] = new ExcelData("Expiry Date", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (hostName) {
                cellDatas[i++] = new ExcelData("Host Name", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (adminNames) {
                cellDatas[i++] = new ExcelData("Admin Name", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (affiliates) {
                cellDatas[i++] = new ExcelData("Affiliate", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (compaings) {
                cellDatas[i++] = new ExcelData("Campaign", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (sourcees) {
                cellDatas[i++] = new ExcelData("Source", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            if (noAccessUserCount) {
                cellDatas[i++] = new ExcelData("No Access User Count", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);
            for (CompanyListItem item : companies) {
                Integer companyIdExcel = item.getCompanyID() != null ? item.getCompanyID() : 0;
                String company = item.getCompanyName() != null ? item.getCompanyName() : "";
                Integer accessCountToExcel = item.getAccessCount() != null ? Integer.valueOf(item.getAccessCount()) : 0;
                String signedUpfrom = item.getCompanySignedUpFrom() != null ? item.getCompanySignedUpFrom() : "";
                String subscriptionTypeExcel = item.getUsagePlanPaymentType() != null && !"Free trial".equals(item.getUsagePlanPaymentType()) ? "Paid" : "Free trial";
                String paymentStatusExcel = item.getUsagePlanPaymentStatus() != null ? item.getUsagePlanPaymentStatus() : "";
                Long periodAccessExcel = item.getPeriodAccess();
                Integer userCountExcel = item.getUserCount() != null ? item.getUserCount() : 0;
                String activeUsersCountExcel = item.getActiveUsersCount() != null ? item.getActiveUsersCount() : "";
                String projectCountExcel = item.getProjectCount() != null ? item.getProjectCount() : "";
                String taskCountEcxel = item.getTaskCount() != null ? item.getTaskCount() : "";
                Integer timeSheetCountExcel = item.getTimesheetCount() != null ? item.getTimesheetCount() : 0;
                String clientCountExcel = item.getClientsCount() != null ? item.getClientsCount() : "";
                Integer supplierCountExcel = item.getSupplierCount() != null ? item.getSupplierCount() : 0;
                Integer leadCountExcel = item.getLeadCount() != null ? item.getLeadCount() : 0;
                Integer contactCountExcel = item.getContactCount() != null ? item.getContactCount() : 0;
                Integer crmTaskCountExcel = item.getCrmtaskCount() != null ? item.getCrmtaskCount() : 0;
                Integer eventCountExcel = item.getEventCount() != null ? item.getEventCount() : 0;
                Integer caseCountExcel = item.getCaseCount() != null ? item.getCaseCount() : 0;
                String invoiceCountExcel = item.getInvoiceCount() != null ? item.getInvoiceCount() : "";
                Integer expenceCountExcel = item.getExpenseCount() != null ? item.getExpenseCount() : 0;
                Integer productCountExcel = item.getProductCount() != null ? item.getProductCount() : 0;
                Integer folderCountExcel = item.getFolderCount() != null ? item.getFolderCount() : 0;
                Integer fileCountExcel = item.getFileCount() != null ? item.getFileCount() : 0;
                String adminEmail = item.getAdminEmail() != null ? item.getAdminEmail() : "";
                String country = item.getCountry() != null ? item.getCountry() : "";
                String expirationDateS = item.getUsagPlanEndDate() != null ? item.getUsagPlanEndDate().toString() : "";
                String hostNameS = item.getHostName() != null ? item.getHostName() : "";
                String adminName = item.getAdminName() != null ? item.getAdminName() : "";
                String affiliate = item.getAffiliate() != null ? item.getAffiliate() : "";
                String compaing = item.getCompaing() != null ? item.getCompaing() : "";
                String source = item.getSource() != null ? item.getSource() : "";
                Integer noAccessUserCountValue = item.getNoAccessUserCount() != null ? item.getNoAccessUserCount() : 0;
                i = 0;

                cellDatas = new ExcelData[colCount];
                if (companyID) {
                    cellDatas[i++] = new ExcelData(companyIdExcel, ExcelData.INTEGER, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }

                cellDatas[i++] = new ExcelData(company, ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

                if (phone) {
                    cellDatas[i++] = new ExcelData(item.getPhone(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (registrationDate) {
                    cellDatas[i++] = new ExcelData(getDate(item.getRegistrationDate()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (accsessCount) {
                    cellDatas[i++] = new ExcelData(item.getAccessCount(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (firstAccessDate) {
                    cellDatas[i++] = new ExcelData(getDate(item.getFirstAccessDate()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (lastAccessDate) {
                    cellDatas[i++] = new ExcelData(getDate(item.getLastAccessDate()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (signUpFrom) {
                    cellDatas[i++] = new ExcelData(item.getCompanySignedUpFrom(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (subscriptionType) {
                    cellDatas[i++] = new ExcelData(subscriptionTypeExcel, ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (paymentstatus) {
                    cellDatas[i++] = new ExcelData(paymentStatusExcel, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (periodAccess) {
                    cellDatas[i++] = new ExcelData(periodAccessExcel.toString(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (userCount) {
                    cellDatas[i++] = new ExcelData(userCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (activeUsersCount) {
                    cellDatas[i++] = new ExcelData(activeUsersCountExcel, ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (projectCount) {
                    cellDatas[i++] = new ExcelData(projectCountExcel, ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (taskCount) {
                    cellDatas[i++] = new ExcelData(taskCountEcxel, ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (timeSheetCount) {
                    cellDatas[i++] = new ExcelData(timeSheetCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (clientCount) {
                    cellDatas[i++] = new ExcelData(clientCountExcel, ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (supplierCount) {
                    cellDatas[i++] = new ExcelData(supplierCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (leadCount) {
                    cellDatas[i++] = new ExcelData(leadCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (contactCount) {
                    cellDatas[i++] = new ExcelData(contactCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (crmTaskCount) {
                    cellDatas[i++] = new ExcelData(crmTaskCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (eventCount) {
                    cellDatas[i++] = new ExcelData(eventCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (caseCount) {
                    cellDatas[i++] = new ExcelData(caseCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (invoiceCount) {
                    cellDatas[i++] = new ExcelData(invoiceCountExcel, ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (expenseCount) {
                    cellDatas[i++] = new ExcelData(expenceCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (productCount) {
                    cellDatas[i++] = new ExcelData(productCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (folderCount) {
                    cellDatas[i++] = new ExcelData(folderCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (fileCount) {
                    cellDatas[i++] = new ExcelData(fileCountExcel, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (email) {
                    cellDatas[i++] = new ExcelData(adminEmail, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (countryname) {
                    cellDatas[i++] = new ExcelData(country, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (expirationDate) {
                    cellDatas[i++] = new ExcelData(expirationDateS, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (hostName) {
                    cellDatas[i++] = new ExcelData(hostNameS, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (adminNames) {
                    cellDatas[i++] = new ExcelData(adminName, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (affiliates) {
                    cellDatas[i++] = new ExcelData(affiliate, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (compaings) {
                    cellDatas[i++] = new ExcelData(compaing, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (sourcees) {
                    cellDatas[i++] = new ExcelData(source, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (noAccessUserCount) {
                    cellDatas[i++] = new ExcelData(noAccessUserCountValue, ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate backend view excel report, exception: " + e);
        }
        return null;
    }

    public CompanyList getOptinalExport() {
        EdsUser user = statisticManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            fp.setParams(backendManagement.getHostNames());
        }
        List<Object[]> companyListItems = statisticManager.getLimitedSubscriptions(fp, from, to, limit);

        HashMap<Integer, Integer> existingCompanies = new HashMap<>();

        for (Iterator i = companyListItems.iterator(); i.hasNext(); ) {
            Object[] iObj = (Object[]) i.next();
            EdsCompanyStatistic cStatic = (EdsCompanyStatistic) iObj[0];

            if (existingCompanies.get(cStatic.getCompanyID()) != null) {
                i.remove();
            } else {
                existingCompanies.put(cStatic.getCompanyID(), cStatic.getCompanyID());
            }
        }

        ExcelData[] cellDatas;
        ArrayList<CompanyListItem> result = new ArrayList<>();
        Date statisticLasUpdatedTime = null;// = new Date();
        int totalCount = companyListItems.size();

        for (Object[] objs : companyListItems) {
            EdsCompanyStatistic cStatic = (EdsCompanyStatistic) objs[0];
            EdsUsagePlan usagePlan = (EdsUsagePlan) objs[1];
            if (statisticLasUpdatedTime == null && cStatic.getStatisticUpdatedTime() != null) {
                statisticLasUpdatedTime = new Date(cStatic.getStatisticUpdatedTime().getTime());
            }
            String shadowLoginlink = "shadowLogin?id=";
            shadowLoginlink = shadowLoginlink + EncryptionHelper.encryptURL(cStatic.getCompanyID().toString());
            try {
                CompanyListItem res = cStatic.getRPC_CompanyListItem();
                res.setUsagePlanPaymentStatus(usagePlan.getStatus() != null ? usagePlan.getStatus().getName() : "");
                res.setUsagePlanPaymentType(usagePlan.getPeriodType() != null ? usagePlan.getPeriodType().getName() : "");
                res.setPeriodStartDate(formatter.format(usagePlan.getStartDate()));
                res.setPeriodEndDate(formatter.format(usagePlan.getEndDate()));
                res.setUsagPlanEndDate(usagePlan.getEndDate());
                result.add(res);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return new CompanyList(result, totalCount);
    }

    @Override
    public void setFileName(String name) {
        filename = "Company statistics report";
    }

    private String getDate(Date date) {
        return ServerUtils.longDateFormat(date, statisticManager.getUser());
    }
}
