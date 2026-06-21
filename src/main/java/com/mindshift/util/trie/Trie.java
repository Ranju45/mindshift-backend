package com.mindshift.util.trie;

import com.mindshift.model.enums.ThoughtCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Minimal Trie (prefix tree) used to tag a journal entry's terminal node
 * with a ThoughtCategory once a trigger phrase is fully matched.
 *
 * Why a Trie instead of a list of .contains() checks:
 *   - With N trigger phrases and a text of length M, naive substring
 *     scanning is O(N*M). Walking the trie once per starting index is
 *     O(M*L) where L is the longest phrase — independent of N. With 100+
 *     trigger phrases (the real library has far more once tuned), this
 *     matters in a hot path called on every journal save.
 *   - Insertion is O(L) per phrase, done once at startup.
 *
 * This is intentionally a teaching-grade structure (not Aho-Corasick),
 * but the asymptotic win over N independent .contains() calls is real.
 */
public class Trie {

    private final TrieNode root = new TrieNode();

    public void insert(String phrase, ThoughtCategory category) {
        TrieNode node = root;
        String normalized = phrase.toLowerCase().trim();
        for (char c : normalized.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isTerminal = true;
        node.category = category;
    }

    /**
     * Scans the full text starting at every index and returns the first
     * category whose phrase is matched as a substring. Returns
     * UNCATEGORIZED if nothing matches.
     */
    public ThoughtCategory findFirstMatch(String text) {
        String normalized = text.toLowerCase();
        for (int start = 0; start < normalized.length(); start++) {
            TrieNode node = root;
            int i = start;
            while (i < normalized.length() && node.children.containsKey(normalized.charAt(i))) {
                node = node.children.get(normalized.charAt(i));
                if (node.isTerminal) {
                    return node.category;
                }
                i++;
            }
        }
        return ThoughtCategory.UNCATEGORIZED;
    }

    /** Internal node — package-private, not meant to leak outside the trie. */
    static class TrieNode {
        final Map<Character, TrieNode> children = new HashMap<>();
        boolean isTerminal = false;
        ThoughtCategory category;
    }
}
