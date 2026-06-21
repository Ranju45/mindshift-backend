package com.mindshift.model.enums;

/**
 * The six overthinking archetypes used by the rule-based classifier
 * (see util.classifier.RuleBasedThoughtClassifier) and by the AI prompt
 * factory to ground Claude's response in a known taxonomy.
 */
public enum ThoughtCategory {
    RUMINATION,
    WORRY,
    CATASTROPHIZING,
    ANALYSIS_PARALYSIS,
    SELF_DOUBT,
    PERFECTIONISM,
    UNCATEGORIZED
}
