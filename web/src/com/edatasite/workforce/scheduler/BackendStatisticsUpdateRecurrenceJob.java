package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyStatistic;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.db.CompanyStatisticManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 10/31/12
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackendStatisticsUpdateRecurrenceJob extends BaseRecurrenceJob {

    private JdbcSpringManager jdbcSpringManager = (JdbcSpringManager) ApplicationContextProvider.applicationContext.getBean("jdbcSpringManager");
    private CompanyStatisticManager companyStatisticManager = (CompanyStatisticManager) ApplicationContextProvider.applicationContext.getBean("companyStatisticManager");
    private WfmJpaOperations jpaTemplate = (WfmJpaOperations) ApplicationContextProvider.applicationContext.getBean("jpaTemplate");

    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info(">>>> Begin company update statistic for date: " + new Date());
        List<EdsCompany> companyList = jdbcSpringManager.getSchemaNameList();
        try {
            for (EdsCompany schema : companyList) {
                if (!schema.getTestCompany()) {
                    ServerSecurityContext.getInstance().setCompanyId(schema.getObjectID().toString());
                    processCompanyStatistic(schema);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getLogger().info(">>>> DONE ALL COMPANY UPDATE STATISTICS");
    }

    private void processCompanyStatistic(EdsCompany schema) {
        javax.persistence.EntityManager em = null;
        try {
            em = jpaTemplate.createHibernateEntityManager();
            org.hibernate.Session session = (org.hibernate.Session) em.getDelegate();
            Session createdSession = null;
            try {
                createdSession = session.getSessionFactory().openSession();
                createdSession.beginTransaction();

                List<Object[]> companyStatistic = companyStatisticManager.getCompanyStatistic();
                for (Object[] statistic : companyStatistic) {
                    EdsCompanyStatistic companystat = companyStatisticManager.getStatisticByCompanyID(schema.getObjectID());
                    if (companystat == null) {
                        companystat = new EdsCompanyStatistic();
                    }
                    backendService.wrapStatisticData(statistic, companystat);
                    createdSession.save(companystat);
                    createdSession.flush();
                    createdSession.getTransaction().commit();
                    break;
                }
            } catch (RuntimeException e) {
                getLogger().error("Failed update statistic for COMPANY = " + schema.getObjectID());
                e.printStackTrace();
                if (createdSession != null && createdSession.getTransaction().isActive()) {
                    createdSession.getTransaction().rollback();
                }
                throw e;
            } finally {
                getLogger().info("Done update statistic for COMPANY = " + schema.getObjectID());
                if (createdSession != null && createdSession.isOpen()) {
                    createdSession.close();
                }
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
