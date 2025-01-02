package org.sunny.datastructures.string.algos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sunny.datastructures.string.exceptions.InvalidInputException;
import org.sunny.datastructures.string.exceptions.PrefixesFileCouldNotBeLoadedException;
import org.sunny.datastructures.string.trie.Trie;
import org.sunny.datastructures.string.trie.UnicodeTrieImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

public class PrefixMatching {

    private static final Logger logger = LoggerFactory.getLogger(PrefixMatching.class.getName());

    private final Trie trie;

    public PrefixMatching(Trie trie) {
        this.trie = trie;
    }

    /**
     * Loads a prefix file into the Trie data structure.
     * Each line in the file is considered a prefix and is inserted into the Trie after being normalized.
     *
     * @param prefixesFileLocation The full path to the prefix file.
     * @throws PrefixesFileCouldNotBeLoadedException If the file cannot be read or the file location is invalid.
     */
    public void loadPrefixes(final String prefixesFileLocation) throws PrefixesFileCouldNotBeLoadedException {
        logger.debug("Trying to load prefix file: {}", prefixesFileLocation);
        try (BufferedReader reader = new BufferedReader(new FileReader(prefixesFileLocation))) {
            String prefix;
            while ((prefix = reader.readLine()) != null) {
                if (!prefix.isBlank()) {
                    trie.insert(prefix);
                }
            }
            logger.debug("Prefix file: {} loaded", prefixesFileLocation);
        } catch (Exception e) {
            logger.error("Exception while loading prefixes for file name {}", prefixesFileLocation, e);
            throw new PrefixesFileCouldNotBeLoadedException(e);
        }
    }

    public Optional<String> findLongestPrefix(final String input) throws InvalidInputException {
        return trie.findLongestPrefix(input);
    }

    public static void main(String[] args) throws PrefixesFileCouldNotBeLoadedException, InvalidInputException {
        var prefixMatching = new PrefixMatching(new UnicodeTrieImpl());

        var prefixesFileLocation = (args.length > 0) ? args[0].trim() : "src/main/resources/prefixes.txt";

        prefixMatching.loadPrefixes(prefixesFileLocation);

        prefixMatching.findLongestPrefix("america").ifPresent(System.out::println);
        prefixMatching.findLongestPrefix("Meet").ifPresent(System.out::println);
        prefixMatching.findLongestPrefix("fooébar").ifPresent(System.out::println);
        prefixMatching.findLongestPrefix("a".repeat(9999) + "b".repeat(999)).ifPresent(System.out::println);
        prefixMatching.findLongestPrefix("a".repeat(9999) + "foo").ifPresent(System.out::println);
    }

}
