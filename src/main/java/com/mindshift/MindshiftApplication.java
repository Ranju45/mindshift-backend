package com.mindshift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MindShift Backend — Entry Point
 *
 * Layered architecture:
 *   Controller -> Service -> Repository -> Database
 *
 * Cross-cutting concerns:
 *   - Security (JWT filter chain)
 *   - Global exception handling
 *   - Event-driven score recalculation (Observer pattern)
 */
@SpringBootApplication
public class MindshiftApplication {
    public static void main(String[] args) {
        SpringApplication.run(MindshiftApplication.class, args);
    }
}
