package com.training.lehoang.modules.skill;

import java.util.*;

public class SkillMatcher {

    // Tokenize a string into lowercase words
    public static List<String> tokenize(String text) {
        return Arrays.asList(text.toLowerCase().split("\\s+"));
    }

    // Build a vector for text using a set of unique words (vocabulary)
    public static Map<String, Integer> buildVector(List<String> tokens, Set<String> vocabulary) {
        Map<String, Integer> vector = new HashMap<>();

        for (String word : vocabulary) {
            vector.put(word, Collections.frequency(tokens, word));
        }

        return vector;
    }

    // Calculate cosine similarity between two vectors
    public static double cosineSimilarity(Map<String, Integer> vecA, Map<String, Integer> vecB) {
        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        for (String key : vecA.keySet()) {
            int valA = vecA.get(key);
            int valB = vecB.getOrDefault(key, 0);

            dotProduct += valA * valB;
            magnitudeA += Math.pow(valA, 2);
        }

        for (int valB : vecB.values()) {
            magnitudeB += Math.pow(valB, 2);
        }

        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        if (magnitudeA == 0.0 || magnitudeB == 0.0) {
            return 0.0; // Avoid division by zero
        }

        return dotProduct / (magnitudeA * magnitudeB);
    }

    public static void main(String[] args) {
        // Example user skills and job description
        String userSkills = "Java Spring Hibernate";
        String jobDescription = "We are looking for a Java developer with Spring and Hibernate experience.";

        // Tokenize skills and job description
        List<String> userSkillTokens = tokenize(userSkills);
        List<String> jobDescriptionTokens = tokenize(jobDescription);

        // Combine both token lists to get the unique vocabulary
        Set<String> vocabulary = new HashSet<>();
        vocabulary.addAll(userSkillTokens);
        vocabulary.addAll(jobDescriptionTokens);

        // Build vectors for both user skills and job description
        Map<String, Integer> userSkillVector = buildVector(userSkillTokens, vocabulary);
        Map<String, Integer> jobDescriptionVector = buildVector(jobDescriptionTokens, vocabulary);

        // Calculate cosine similarity
        double similarity = cosineSimilarity(userSkillVector, jobDescriptionVector);

        System.out.println("Cosine Similarity: " + similarity);
    }
}
