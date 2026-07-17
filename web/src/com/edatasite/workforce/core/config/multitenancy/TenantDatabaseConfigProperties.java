package com.edatasite.workforce.core.config.multitenancy;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TenantDatabaseConfigProperties {

    @Inject
    private Environment env;

    @Value("${tenant.datasource.username}")
    private String username;
    @Value("${tenant.datasource.password}")
    private String password;

    private List<Cluster> clusters;

    @PostConstruct
    public void init() {
        this.clusters = parseClusterProperties();
    }

    private List<Cluster> parseClusterProperties() {
        List<String> clusterList = new Gson().fromJson(env.getProperty("tenant.datasource.clusters", ""), List.class);
        return clusterList.stream()
                .map(code -> {
                    Cluster cluster = new Cluster();
                    cluster.setCode(code);

                    ClusterConnectionConfig master = new ClusterConnectionConfig();
                    master.setUrl(env.getProperty("tenant.datasource.master." + code + ".url"));
                    master.setUsername(username);
                    master.setPassword(password);
                    cluster.setMaster(master);

                    ClusterConnectionConfig slave = new ClusterConnectionConfig();
                    slave.setUrl(env.getProperty("tenant.datasource.slave." + code + ".url"));
                    slave.setUsername(username);
                    slave.setPassword(password);
                    cluster.setSlave(slave);

                    return cluster;
                })
                .collect(Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}
