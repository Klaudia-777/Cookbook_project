package javaLabApp;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Logging {

    final static Logger logger = Logger.getLogger(Logging.class);

    public static Logger getLogger() {
        return logger;
    }

    static void setupConnection(){
        BasicConfigurator.configure();
    }


//    public static void main(String[] args) {
//
//
//        logger.info("Entering application.");
//        logger.trace("Entering application (trace).");
//        logger.debug("Entering application (debug).");
//        logger.error("Entering application (error).");
//        logger.fatal("Entering application (fatal).");
//
//        System.out.println("Application is working");
//
//        logger.info("Exiting application.");
//    }
}