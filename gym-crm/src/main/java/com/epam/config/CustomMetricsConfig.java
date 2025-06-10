package com.epam.config;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.management.ManagementFactory;

@Configuration
public class CustomMetricsConfig {

    @Bean
    public Gauge memoryUsageGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("jvm.memory.used", () -> ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed())
        .tag("area", "heap")
        .register(meterRegistry);
    }

    @Bean
    public Gauge monitorConnectionPoolSize(MeterRegistry meterRegistry, HikariDataSource dataSource) {
        return Gauge.builder("database.connection.pool.size", dataSource, ds -> ds.getHikariPoolMXBean().getActiveConnections())
                .tags("pool", "Hikari")
                .register(meterRegistry);
    }
}
