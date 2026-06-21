package com.mindshift.service.impl;

import com.mindshift.model.JournalEntry;
import com.mindshift.model.User;
import com.mindshift.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Top-K Frequent Trigger Words — the same "Top K Frequent Elements"
 * pattern tested at Google/Amazon/Meta system-design and DSA rounds,
 * applied to a real product feature: surfacing what a user actually
 * ruminates on most.
 *
 * Algorithm:
 *   1. Tokenize all journal text into words, stripping stopwords.
 *   2. Count frequency with a HashMap   -> O(n)
 *   3. Maintain a min-heap of size K    -> O(n log K)
 *      (bounded heap beats sorting the whole vocabulary, which would be
 *       O(n log n) — matters once a user has thousands of entries)
 *
 * Honesty note on "ML": this is statistical frequency analysis
 * (a real, classical NLP/ML technique — the same idea behind TF in
 * TF-IDF), not a trained model. The actual semantic understanding of
 * *why* a thought is a particular overthinking type is delegated to
 * Claude in ChatServiceImpl, which is the genuine ML/LLM component of
 * this system. We don't fake a custom neural net here — that would be
 * both wrong and pointless when a frontier LLM is one HTTP call away.
 */
@Service
@RequiredArgsConstructor
public class JournalAnalyticsService {

    private static final Set<String> STOPWORDS = Set.of(
        "the","a","an","is","am","are","was","were","be","been","i","me","my",
        "to","of","in","on","at","it","that","this","and","or","but","so","with",
        "for","about","just","like","im","its","not","do","does","did","have",
        "has","had","will","would","could","should","what","when","where","how"
    );

    private final JournalEntryRepository journalRepository;

    public List<TriggerWord> getTopTriggers(User user, int k) {
        List<JournalEntry> entries = journalRepository.findByUserOrderByCreatedAtDesc(user);

        Map<String, Integer> freq = new HashMap<>();
        for (JournalEntry e : entries) {
            for (String token : tokenize(e.getText())) {
                freq.merge(token, 1, Integer::sum);
            }
        }

        // Min-heap bounded to size k: push everything, pop the smallest
        // whenever size exceeds k. Net result is the k largest counts
        // without ever sorting the full vocabulary.
        PriorityQueue<Map.Entry<String, Integer>> minHeap =
            new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> entry : freq.entrySet()) {
            minHeap.offer(entry);
            if (minHeap.size() > k) minHeap.poll();
        }

        List<TriggerWord> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            Map.Entry<String, Integer> e = minHeap.poll();
            result.add(new TriggerWord(e.getKey(), e.getValue()));
        }
        Collections.reverse(result); // highest frequency first
        return result;
    }

    private List<String> tokenize(String text) {
        if (text == null) return List.of();
        String[] raw = text.toLowerCase().replaceAll("[^a-z0-9\\s']", " ").split("\\s+");
        List<String> tokens = new ArrayList<>();
        for (String w : raw) {
            if (w.length() > 2 && !STOPWORDS.contains(w)) tokens.add(w);
        }
        return tokens;
    }

    public record TriggerWord(String word, int frequency) {}
}
