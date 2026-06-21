package com.mindshift.util.classifier;

import com.mindshift.model.User;
import com.mindshift.model.enums.ThoughtCategory;
import org.springframework.stereotype.Component;

/**
 * Factory pattern — encapsulates the (non-trivial, copy-heavy) logic of
 * building the Claude system prompt so ChatService stays focused on
 * orchestration, not string templating.
 */
@Component
public class PromptFactory {

    private static final String TEMPLATE =
        "You are MindShift AI - a world-class mind coach trained on neuroscience "
      + "(Harvard, MIT, Stanford), CBT, Stoic philosophy, Vedic wisdom, and Buddhist psychology.\n\n"
      + "User profile:\n"
      + "- Name: %s\n"
      + "- Overthinking severity: %d/10\n"
      + "- Goal: %s\n"
      + "%s\n\n"
      + "When the user shares a thought or spiral, respond with EXACTLY this structure "
      + "(no markdown headers, just clear sections):\n\n"
      + "TYPE: [Rumination / Worry / Catastrophizing / Analysis Paralysis / Self-doubt / Perfectionism]\n"
      + "ROOT CAUSE: [1-2 sentences, specific to what they shared]\n"
      + "TECHNIQUE: [Named technique + exact steps to do right now]\n"
      + "REFRAME: [One sentence that shifts their perspective]\n\n"
      + "Rules: never generic, max 220 words, warm but direct, coach not therapist. "
      + "If they mention self-harm, gently direct them to professional help.";

    public String buildSystemPrompt(User user, ThoughtCategory preClassified) {
        String hint = preClassified == ThoughtCategory.UNCATEGORIZED
            ? ""
            : "A fast rule-based pre-classifier suggests this might be: " + preClassified
              + ". Use this as a hint only - re-evaluate independently from what the user actually wrote.";

        return String.format(TEMPLATE,
            user.getName(), user.getSeverity(), user.getGoal(), hint);
    }
}
