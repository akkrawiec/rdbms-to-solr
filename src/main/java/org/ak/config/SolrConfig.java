package org.ak.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource(value = { "classpath:solr.properties" })
@EnableAutoConfiguration
public class SolrConfig {
    @Bean
    public SolrClient concurrentSolrClient() {
        return new ConcurrentUpdateSolrClient
                .Builder(env.getProperty("solr.host"))
                .withThreadCount(Integer.parseInt(env.getProperty("solr.threadCount")))
                .withQueueSize(Integer.parseInt(env.getProperty("solr.queueSize")))
                .build();

    }

    /*
    @Bean
    public SolrClient cloudSolrClient() {
        return new CloudSolrClient
                .Builder(new ArrayList<String>(Arrays.asList(env.getProperty("solr.zk.hosts").split(","))))
                .withParallelUpdates(true)
                .build();
    }
    */

    @Bean
    public Config solrConfiguration() {
        Config config = new Config();
        config.setQueueSize(Integer.parseInt(env.getProperty("solr.queueSize")));
        config.setThreadCount(Integer.parseInt(env.getProperty("solr.threadCount")));
        config.setInitialCommitDelay(Integer.parseInt(env.getProperty("solr.initial.commit.delay")));
        config.setPeriodicCommitDelay(Integer.parseInt(env.getProperty("solr.periodic.commit.delay")));
        config.setSolrHost(env.getProperty("solr.host"));
        config.setCollectionName(env.getProperty("solr.collectionName"));
        config.setSolrHosts(new ArrayList<String>(Arrays.asList(env.getProperty("solr.hosts").split(","))));
        config.setZkHosts(new ArrayList<String>(Arrays.asList(env.getProperty("solr.zk.hosts").split(","))));
        return config;
    }

    @Autowired
    private Environment env;

    public class Config {
        private int queueSize;
        private int threadCount;
        private int initialCommitDelay;
        private int periodicCommitDelay;
        private String solrHost;
        private String collectionName;
        private List<String> solrHosts;
        private List<String> zkHosts;

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public void setThreadCount(int threadCount) {
            this.threadCount = threadCount;
        }

        public int getInitialCommitDelay() {
            return initialCommitDelay;
        }

        public void setInitialCommitDelay(int initialDelay) {
            this.initialCommitDelay = initialDelay;
        }

        public int getPeriodicCommitDelay() {
            return periodicCommitDelay;
        }

        public void setPeriodicCommitDelay(int periodicDelay) {
            this.periodicCommitDelay = periodicDelay;
        }

        public String getSolrHost() {
            return solrHost;
        }

        public void setSolrHost(String solrHost) {
            this.solrHost = solrHost;
        }

        public String getCollectionName() {
            return collectionName;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }

        public List<String> getSolrHosts() {
            return solrHosts;
        }

        public void setSolrHosts(List<String> solrHosts) {
            this.solrHosts = solrHosts;
        }

        public List<String> getZkHosts() {
            return zkHosts;
        }

        public void setZkHosts(List<String> zkHosts) {
            this.zkHosts = zkHosts;
        }
    }
}