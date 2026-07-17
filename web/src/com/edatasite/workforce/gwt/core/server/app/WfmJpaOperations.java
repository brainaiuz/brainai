package com.edatasite.workforce.gwt.core.server.app;

import org.hibernate.Session;
import org.springframework.integration.jpa.core.JpaOperations;

import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 29.10.2008
 * Time: 13:39:46
 * To change this template use File | Settings | File Templates.
 */
public interface WfmJpaOperations extends JpaOperations {

    Session getHibernateSession();

    Session createHibernateSession();

    EntityManager getHibernateEntityManager();

    EntityManager createHibernateEntityManager();
}
