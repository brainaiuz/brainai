package com.edatasite.workforce.core.config;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrClientFactory;
import org.springframework.data.solr.server.support.HttpSolrClientFactory;

/**
 * @author: Dilsh0d Tadjiev on 21.07.2020 17:26.
 */
@Configuration
@EnableSolrRepositories(value = "com.edatasite.workforce.core.solr.repository")
public class ApacheSolrConfig {

    @Bean
    public SolrClient solrClient() {
        String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        SolrClientFactory solrClientFactory = null;
        /*if (!Constants.DEFAULT_SPRING_PROFILES.equals(activeProfile)) {// @TODO EmbeddedSolr will be config after migration all collections for development local environment
            try {
                solrClientFactory = new EmbeddedSolrServerFactory("classpath:com/acme/solr");
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } else {*/
            solrClientFactory = new HttpSolrClientFactory(new HttpSolrClient.Builder().withBaseSolrUrl(SpringPropertiesUtil.getProperty("cluster.solr.free.url")).build());
//        }
        return solrClientFactory.getSolrClient();
    }

    @Bean
    public SolrTemplate solrTemplate() {
        return new SolrTemplate(solrClient());
    }

}
