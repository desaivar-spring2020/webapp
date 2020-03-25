package com.csye.user.metrics;

@Configuration
public class MetricsConfig {
    @Bean
    public StatsDClient statsDClient(
            @Value("${metrics.statsd.host:localhost}") String host,
            @Value("${metrics.statsd.port:8125}") int port,
            @Value("${metrics.prefix:example.app}") String prefix
    ) {
        return new NonBlockingStatsDClient(prefix, host, port);
    }
}