package com.greenhouse.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TextSimilarityService {

    public double calculateCosineSimilarity(String text1, String text2) {
        Map<String, Integer> wordFrequency1 = calculateWordFrequencies(text1);
        Map<String, Integer> wordFrequency2 = calculateWordFrequencies(text2);

        double dotProduct = calculateDotProduct(wordFrequency1, wordFrequency2);
        double magnitude1 = calculateMagnitude(wordFrequency1);
        double magnitude2 = calculateMagnitude(wordFrequency2);

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0; // To avoid division by zero
        }

        return dotProduct / (magnitude1 * magnitude2) * 100;
    }

    private Map<String, Integer> calculateWordFrequencies(String text) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (String word : words) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        return wordFrequency;
    }

    private double calculateDotProduct(Map<String, Integer> vector1, Map<String, Integer> vector2) {
        double dotProduct = 0;

        for (String word : vector1.keySet()) {
            if (vector2.containsKey(word)) {
                dotProduct += vector1.get(word) * vector2.get(word);
            }
        }

        return dotProduct;
    }

    private double calculateMagnitude(Map<String, Integer> vector) {
        double sum = 0;

        for (int frequency : vector.values()) {
            sum += frequency * frequency;
        }

        return Math.sqrt(sum);
    }
}
