package com.epam;

import com.epam.config.TomcatRunner;
import org.apache.catalina.LifecycleException;

public class Main {

    public static void main(String[] args) throws LifecycleException {

        TomcatRunner.start();

    }
}
