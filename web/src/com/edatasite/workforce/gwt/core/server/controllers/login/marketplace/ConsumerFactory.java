package com.edatasite.workforce.gwt.core.server.controllers.login.marketplace;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.step2.ConsumerHelper;
import org.openid4java.consumer.ConsumerAssociationStore;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
/**
 * User: Anvarbek
 * Date: May 6, 2010
 * Time: 5:57:54 PM
 */

/**
 * Simple wrapper around Guice for applications that use other frameworks/dependency injection
 * methods.  Provides access to a ConsumerHelper instance for implementing an OpenID relying party.
 */
public class ConsumerFactory {

    protected ConsumerHelper helper;

    public ConsumerFactory() {
        this(new InMemoryConsumerAssociationStore());
    }

    public ConsumerFactory(ConsumerAssociationStore store) {
        Injector injector = Guice.createInjector(new GuiceModule(store));
        helper = injector.getInstance(ConsumerHelper.class);
    }

    public ConsumerHelper getConsumerHelper() {
        return helper;
    }
}