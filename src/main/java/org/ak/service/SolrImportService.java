package org.ak.service;

import org.ak.dto.Location;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.ak.config.SolrConfig;
import reactor.core.publisher.ParallelFlux;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class SolrImportService {

    @Autowired
    private LocationExportService locationExportService;

    @Autowired
    private SolrLocationSubscriber solrLocationSubscriber;

    @Autowired
    private SolrConfig.Config solrConfiguration;

    @Autowired
    SolrClient concurrentSolrClient;

    /**
     *
     */
    public void importDataParallel() {
        System.out.println("Before commitPeriodically :" + LocalTime.now());
        commitPeriodically();
        System.out.println("After commitPeriodically :" + LocalTime.now());
        System.out.println("Before subscribe :" + LocalTime.now());
        ParallelFlux<Location> locationFlux = locationExportService.parallelLocationFlux();
        locationFlux.subscribe(solrLocationSubscriber);
        System.out.println("After subscribe :" + LocalTime.now());
    }

    private void commitPeriodically() {
        ScheduledExecutorService commitScheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable commitTask = () -> {
            try {
                System.out.println("Commit :" + LocalTime.now());
                UpdateResponse commitResponse = concurrentSolrClient.commit(solrConfiguration.getCollectionName());
                if (commitResponse.getStatus() != 0) {
                    throw new RuntimeException("Error status during commit " + commitResponse.getStatus());
                }
            } catch (Throwable t) {
                throw new RuntimeException("Exception during commit");
            }
        };
        commitScheduler.scheduleAtFixedRate(commitTask, solrConfiguration.getInitialCommitDelay(), solrConfiguration.getPeriodicCommitDelay(), TimeUnit.MINUTES);
    }
}
