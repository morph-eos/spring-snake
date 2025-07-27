package com.springsnake.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Snake Backend Application - Main Entry Point
 * 
 * This is the main Spring Boot application class for the Spring Snake Key-Value Store.
 * It bootstraps the entire backend system including:
 * 
 * - REST API endpoints for CRUD operations
 * - MongoDB integration for data persistence
 * - Comprehensive error handling and validation
 * - Health check endpoints via Spring Boot Actuator
 * - Logging and monitoring capabilities
 * 
 * The application provides a robust backend service for storing and retrieving
 * key-value pairs with full REST API support, designed for containerized deployment.
 * 
 * @author M04ph3u2
 * @version 2.0
 * @since 1.0
 */
@SpringBootApplication
public class BackendApplication {
	
	/**
	 * Main method to start the Spring Boot application
	 * 
	 * This method initializes the Spring Boot application context and starts
	 * the embedded Tomcat server on the configured port (default 8080).
	 * 
	 * @param args Command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
