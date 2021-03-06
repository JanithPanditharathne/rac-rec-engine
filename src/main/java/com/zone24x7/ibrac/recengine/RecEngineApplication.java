package com.zone24x7.ibrac.recengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application where the application will bootstrap.
 */
@SpringBootApplication
public class RecEngineApplication {

    /**
     * Main method of tha application to for bootstrapping application.
     *
     * @param args input arguments.
     */
    public static void main(String[] args) { // NOSONAR
        SpringApplication.run(RecEngineApplication.class, args); // NOSONAR
    }
}
