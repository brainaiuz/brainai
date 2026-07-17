package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Quartz job that updates invoice statuses for all active companies.
 * <p>
 * It creates a fixed-size thread pool, submits one task per company to:
 * 1. switch the context to that company’s schema;
 * 2. update sales‐invoice statuses;
 * 3. update purchase‐invoice statuses;
 * </p>
 * The job will wait up to 30 minutes for all tasks to finish before shutting down.
 */
public class InvoiceUpdateStatusRecurrenceJob extends BaseRecurrenceJob implements Constants {

    /**
     * Entry point for the Quartz scheduler.
     * Fetches all active companies, spins up the worker threads, and waits for completion.
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("InvoiceUpdateStatusRecurrenceJob started: {}", new Date());

        // Load the list of companies and available schemas
        List<EdsCompany> companies = companyManager.getOccupiedCompanies();
        List<String> schemas = companyManager.getExistingSchemas();

        // Create a fixed thread pool for parallel processing
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT_FOR_MULTI_PROCESS);

        // Submit one Runnable per company
        for (EdsCompany company : companies) {
            // Skip companies without a valid schema, inactive companies, or those missing a country zone
            if (!company.hasSchema(schemas) ||
                    Boolean.FALSE.equals(company.getActive()) ||
                    company.getCountryZone() == null) {
                continue;
            }

            final Integer companyId = company.getObjectID();
            // run threed 5,  threed queue N, complted N
            executor.execute(() -> {
                try {
                    // Switch to this company's (databse / schema)
                    setCompanyAndDatabase(companyId);

                    // Perform the two update operations, each in its own new transaction
                    invoiceServiceLocal.updateCompanySalesInvoicesStatuses(companyId);
                    invoiceServiceLocal.updateCompanyPurchaseInvoicesStatuses(companyId);

                } catch (Exception e) {
                    // Log any errors without stopping other tasks
                    getLogger().error("Error updating invoices for company {}", companyId, e);
                }
            });
        }

        // Initiate an orderly shutdown: no new tasks, but finish existing ones
        executor.shutdown();
        try {
            // Wait up to 30 minutes for all tasks to complete
            if (!executor.awaitTermination(30, TimeUnit.MINUTES)) {
                getLogger().warn("InvoiceUpdateStatusRecurrenceJob timed out before completing all tasks");
            }
        } catch (InterruptedException ie) {
            // Restore interrupt status and log
            Thread.currentThread().interrupt();
            getLogger().error("InvoiceUpdateStatusRecurrenceJob was interrupted", ie);
        }

        getLogger().info("InvoiceUpdateStatusRecurrenceJob ended: {}", new Date());
    }
}