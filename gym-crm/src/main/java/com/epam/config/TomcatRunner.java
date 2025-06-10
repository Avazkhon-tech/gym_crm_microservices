package com.epam.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Slf4j
public class TomcatRunner {

    private TomcatRunner() {}

    public static void start() {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.setBaseDir(new File("embedded-tomcat").getAbsolutePath());
        tomcat.getConnector();

        String webAppDir = new File(".").getAbsolutePath();
        tomcat.addWebapp("", webAppDir);

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            log.error("Problem occurred with tomcat: {}", e.getMessage());
        }

        tomcat.getServer().await();
    }
}
