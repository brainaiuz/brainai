package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Transactional
public class InventoryTransactionServlet implements HttpRequestHandler {
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private CompanyManager companyManager;

    @Override
    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String schemaType = request.getParameter("schema");
        if (schemaType.equals("free")) {
            ServerSecurityContext.getInstance().setDatabase(TenantContextHolder.FREE_DB);
        } else if (schemaType.equals("paid")) {
            ServerSecurityContext.getInstance().setDatabase(TenantContextHolder.PAID_DB);
        }
        List<EdsCompany> companies;
        String companyId = request.getParameter("companyId");
        if (companyId != null) {
            companies = Collections.singletonList(companyManager.getCompany(Integer.parseInt(companyId)));
        } else {
            companies = companyManager.getCompanies();
        }
        for (EdsCompany company : companies) {
            System.out.println("------------Correct inventory transactions started for company: " + company.getObjectID());
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
            List<Object[]> transactions = transactionManager.getIncorrectInventoryTransactions();
            System.out.println("---------------Wrong transactions size: " + transactions.size());
            if (!transactions.isEmpty()) {
                for (Object[] objects : transactions) {
                    Integer transactionId = (Integer) objects[0];
                    BigDecimal difference = ((BigDecimal) objects[1]).subtract((BigDecimal) objects[2]);
                    System.out.println("------------Difference for transaction: " + transactionId + " is " + difference);
                    EdsTransaction transaction = transactionManager.get(transactionId);
                    for (EdsTransactionItem item : transaction.getTransactionItems()) {
                        if (item.getDebit() != null) {
                            item.setDebit(item.getDebit().add(difference));
                            break;
                        }
                    }
                }
            }
            System.out.println("---------------Correct inventory transactions ended for company: " + company.getObjectID());
        }
    }
}
