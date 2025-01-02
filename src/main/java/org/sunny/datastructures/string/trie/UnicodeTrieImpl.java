package org.sunny.datastructures.string.trie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sunny.datastructures.string.exceptions.InvalidInputException;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides a Trie-based implementation for finding the longest prefix
 * from a set of pre-loaded prefixes, given an input string.
 * It supports the normalization of input and prefix data for consistent matching.
 */
public class UnicodeTrieImpl implements Trie {

    private static final Logger logger = LoggerFactory.getLogger(UnicodeTrieImpl.class.getName());

    private final TrieNode root;

    public UnicodeTrieImpl() {
        this.root = new TrieNode();
    }

    @Override
    public void insert(final String prefix) {
        var normalizedPrefix = normalize(prefix);
        var currentNode = root;
        for (char c : normalizedPrefix.toCharArray()) {
            currentNode = currentNode.children.computeIfAbsent(c, k -> new TrieNode());
        }
        currentNode.isEndOfPrefix = true;
    }

    /**
     * Finds the longest valid prefix from the pre-loaded prefixes that matches the given input string.
     * The method stops at the first point where no further matching characters are found.
     *
     * @param input The input string to check for a matching prefix.
     * @return An Optional containing the longest valid prefix, if one exists.
     * @throws InvalidInputException If the input string is invalid, e.g., empty, null, or multiline.
     */
    @Override
    public Optional<String> findLongestPrefix(final String input) throws InvalidInputException {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }

        if (input.lines().count() > 1) {
            var errMessage = String.format("Multiline input is not supported so cannot process input string: %s", input);
            logger.error(errMessage);
            throw new InvalidInputException(errMessage);
        }

        var normalizedInput = normalize(input); // since the insertion into Trie was based on normalization, we need to do the same here.
        var currentNode = root;
        var longestPrefix = new StringBuilder();
        for (char c : normalizedInput.toCharArray()) {
            var nextNode = currentNode.children.get(c);
            if (nextNode == null) {
                break;
            }
            longestPrefix.append(c);
            currentNode = nextNode;
        }

        // to ensure prefix is actually the one from the prefixes list
        if (currentNode.isEndOfPrefix && !longestPrefix.isEmpty()) {
            return Optional.of(longestPrefix.toString());
        }
        return Optional.empty();
    }

    private String normalize(String input) {
        return Normalizer.normalize(input.trim().replaceAll("\\s+", ""), Normalizer.Form.NFC);
    }

    private static class TrieNode {
        private final Map<Character, TrieNode> children = new HashMap<>(); //This is a map because we still need to achieve lookup table like behaviour
        private boolean isEndOfPrefix = false;
    }
}
