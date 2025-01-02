package org.sunny.datastructures.string.trie;

import org.sunny.datastructures.string.exceptions.InvalidInputException;

import java.util.Optional;

public interface Trie {

    void insert(String prefix);

    Optional<String> findLongestPrefix(String input) throws InvalidInputException;
}
