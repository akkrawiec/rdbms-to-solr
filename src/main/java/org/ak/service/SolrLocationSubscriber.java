package org.ak.service;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ak.config.SolrConfig;
import org.ak.dto.Location;
import reactor.core.publisher.BaseSubscriber;

/**
 *
 */
@Component
public class SolrLocationSubscriber extends BaseSubscriber<Location> {

    @Autowired
    private SolrConfig.Config solrConfiguration;

    @Autowired
    SolrClient concurrentSolrClient;

    @Autowired
    SolrClient cloudSolrClient;

    @Override
    public void hookOnSubscribe(Subscription subscription) {
        System.out.println("SolrLocationSubscriber Subscribed " + solrConfiguration);
        request(1);
    }

    @Override
    public void hookOnNext(Location location) {
        request(1);
        index(location);
    }

    @Override
    public void hookOnComplete() {
        commit();
        System.out.println("SolrLocationSubscriber Completed");
    }

    @Override
    public void hookOnCancel() {
    }

    //@Override
    public void hookOnError() {
    }

    //@Override
    public void hookFinally() {
    }

    private void index(Location location) {
        try {
            UpdateResponse addResponse = concurrentSolrClient.addBean(solrConfiguration.getCollectionName(), location);
            if (addResponse.getStatus() != 0) {
                throw new RuntimeException("Error status during adding " + addResponse.getStatus());
            }
        } catch (Throwable t) {
            throw new RuntimeException("Exception during document add " + location);
        }
    }

    private void commit() {
        try {
            UpdateResponse commitResponse = concurrentSolrClient.commit(solrConfiguration.getCollectionName());
            if (commitResponse.getStatus() != 0) {
                throw new RuntimeException("Error status during commit " + commitResponse.getStatus());
            }
        } catch (Throwable t) {
            throw new RuntimeException("Exception during commit");
        }
    }
}