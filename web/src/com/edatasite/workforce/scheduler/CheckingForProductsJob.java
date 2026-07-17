package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla
 * Date: May 7, 2010
 * Time: 8:56:36 PM
 * To change this template use File | Settings | File Templates.
 */

public class CheckingForProductsJob extends BaseRecurrenceJob {

    private ProductService productService = (ProductService) ApplicationContextProvider.applicationContext.getBean("productService");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("Product reorder point start: " + new Date());
        List<EdsCompany> companyList = companyManager.getOccupiedCompanies();
        List<String> schemas = companyManager.getExistingSchemas();

        for (EdsCompany company : companyList) {
            try {
                if (company.hasSchema(schemas) && company.getActive() != null && company.getActive()) {
                    setCompanyAndDatabase(company.getObjectID());
                    productService.reorderPointEmail(company.getObjectID());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getLogger().info("Product Reorder Point End: " + new Date());
    }
}

