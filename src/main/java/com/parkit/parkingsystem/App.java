package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class of the project.l
 */
public class App {
  /**
   * To have different types of messages and more detailed.
   */
  private static final Logger logger = LogManager.getLogger("App");

  /**
   * Principal method of the project.
   * @param args arguments.
   */
  public static void main(String[] args) {
    logger.info("Initializing Parking System");
    InteractiveShell.loadInterface();
  }
}
