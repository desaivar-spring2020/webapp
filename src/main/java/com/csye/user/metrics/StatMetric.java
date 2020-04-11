package com.csye.user.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Service
public class StatMetric {

    private static final Logger logger = LoggerFactory.getLogger(StatMetric.class);

    private static final StatsDClient statsd = new NonBlockingStatsDClient("csye6225.webapp.restapi", "localhost", 8125);

    public void incrementStat(String endpoint) {
        logger.info("Incrementing counter for service : " + endpoint);
        statsd.increment(endpoint);
    }

    public void timerStat(String endpoint, long duration){
        logger.info("Measuring time :" + endpoint);
        statsd.gauge(endpoint, duration);
    }

}