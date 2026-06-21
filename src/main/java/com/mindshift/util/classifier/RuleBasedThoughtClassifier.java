package com.mindshift.util.classifier;

import com.mindshift.model.enums.ThoughtCategory;
import com.mindshift.util.trie.Trie;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Concrete Strategy. Builds the Trie once at startup (Singleton-scoped
 * Spring bean) and reuses it for every classification call — insertion
 * cost is paid exactly once, lookup is paid per request.
 */
@Component
public class RuleBasedThoughtClassifier implements ThoughtClassifierStrategy {

    private final Trie trie = new Trie();

    @PostConstruct
    void buildTrie() {
        insertAll(ThoughtCategory.RUMINATION,
            "keep replaying", "cant stop thinking about what happened",
            "said the wrong thing", "going over and over", "what i said earlier");

        insertAll(ThoughtCategory.WORRY,
            "what if", "worried that", "scared that", "afraid that", "nervous about");

        insertAll(ThoughtCategory.CATASTROPHIZING,
            "everything will go wrong", "this is going to be a disaster",
            "ruin everything", "worst case", "going to fail completely");

        insertAll(ThoughtCategory.ANALYSIS_PARALYSIS,
            "cant decide", "too many options", "analyzing every", "cant choose",
            "every possible outcome");

        insertAll(ThoughtCategory.SELF_DOUBT,
            "doubt myself", "not good enough", "what if im wrong", "second guessing",
            "maybe im not capable");

        insertAll(ThoughtCategory.PERFECTIONISM,
            "has to be perfect", "not good enough yet", "redo it again",
            "one more revision", "still not right");
    }

    private void insertAll(ThoughtCategory category, String... phrases) {
        for (String p : phrases) trie.insert(p, category);
    }

    @Override
    public ThoughtCategory classify(String text) {
        if (text == null || text.isBlank()) return ThoughtCategory.UNCATEGORIZED;
        return trie.findFirstMatch(text);
    }
}
