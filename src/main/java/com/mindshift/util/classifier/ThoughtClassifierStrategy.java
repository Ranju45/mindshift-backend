package com.mindshift.util.classifier;

import com.mindshift.model.enums.ThoughtCategory;

/**
 * Strategy interface — lets ChatService swap classification logic
 * (rule-based now, ML-based later) without touching call sites.
 */
public interface ThoughtClassifierStrategy {
    ThoughtCategory classify(String text);
}
