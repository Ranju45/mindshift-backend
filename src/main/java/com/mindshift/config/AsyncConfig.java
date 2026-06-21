package com.mindshift.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Enables @Async so ScoreRecalculationListener runs off the request
 * thread — keeps journal/tracker/progress endpoints fast regardless
 * of how expensive score math becomes later.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
