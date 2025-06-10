package com.epam.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            String pathToCheck = "/";
            FileSystem fs = FileSystems.getDefault();
            long totalSpace = fs.getPath(pathToCheck).toFile().getTotalSpace() / (1024 * 1024);
            long freeSpace = fs.getPath(pathToCheck).toFile().getFreeSpace() / (1024 * 1024);
            double threshold = 0.5 * totalSpace;
            long usedSpace = totalSpace - freeSpace;
            double usedPercentage = (usedSpace * 100.0) / totalSpace;

            if (freeSpace > threshold) {
                return Health.up()
                        .withDetail("Disk Space", "Sufficient space available")
                        .withDetail("Total Space (MB)", totalSpace)
                        .withDetail("Free Space (MB)", freeSpace)
                        .withDetail("Used Space (MB)", usedSpace)
                        .withDetail("Used Percentage", usedPercentage)
                        .build();
            } else {
                return Health.down()
                        .withDetail("Disk Space", "Low disk space")
                        .withDetail("Total Space (MB)", totalSpace)
                        .withDetail("Free Space (MB)", freeSpace)
                        .withDetail("Used Space (MB)", usedSpace)
                        .withDetail("Used Percentage", usedPercentage)
                        .withDetail("Threshold (MB)", threshold)
                        .build();
            }
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("Disk Space", "Error while checking")
                    .withDetail("Error", ex.getMessage())
                    .build();
        }
    }
}
