package org.sunny.datastructures.string.algos;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sunny.datastructures.string.exceptions.InvalidInputException;
import org.sunny.datastructures.string.exceptions.PrefixesFileCouldNotBeLoadedException;
import org.sunny.datastructures.string.trie.UnicodeTrieImpl;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class PrefixMatchingTests {

    private static final String STATIC_PREFIXES_FILE_PATH = "src/test/resources/prefixes_ascii.txt";

    private static PrefixMatching prefixMatcher;

    @BeforeAll
    public static void setUpOnce() throws PrefixesFileCouldNotBeLoadedException {
        var trie = new UnicodeTrieImpl();
        prefixMatcher = new PrefixMatching(trie);
        prefixMatcher.loadPrefixes(Paths.get(STATIC_PREFIXES_FILE_PATH).toString());
    }

    @Test
    public void testFindLongestPrefixWithValidInput() throws InvalidInputException {
        var input = "foo";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "foo";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be 'foo'");
    }

    @Test
    public void testFindLongestPrefixWithValidInputButLeadingAndTrailingWhiteSpaces() throws InvalidInputException {
        var input = "   foo    ";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "foo";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be 'foo'");
    }

    @Test
    public void testFindLongestPrefixWithValidInputButMatchIsNotEndOfPrefix() throws InvalidInputException {
        var input = "arandompre";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithValidInputAndMatchIsEndOfPrefix() throws InvalidInputException {
        var input = "arandomprefix";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "arandomprefix";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be 'arandomprefix'");
    }

    @Test
    public void testFindLongestPrefixWithExactMatch() throws InvalidInputException {
        var input = "aninputthatmatchesexactinput";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        assertEquals(input, longestPrefix.get(), "Longest prefix should be 'aninputthatmatchesexactinput'");
    }

    @Test
    public void testFindLongestPrefixWithNoMatch() throws InvalidInputException {
        var input = "nomatchingprefixes";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithEmptyInputShouldNotMatch() throws InvalidInputException {
        var input = "";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithBlankInputShouldNotMatch() throws InvalidInputException {
        var input = "                                                             ";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithMultilineBlankInputShouldNotMatch() throws InvalidInputException {
        var input = "\n                                                             \n \n \t";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithMultiLineInputShouldThrowException() {
        var input = "\nfoo\n";

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> prefixMatcher.findLongestPrefix(input),
                "Expected findLongestPrefix to throw InvalidTextInputException for multiline input"
        );

        String expectedMessage = "Multiline input is not supported";
        assertTrue(exception.getMessage().contains(expectedMessage),
                "Exception message should contain the expected reason");
    }


    @Test
    public void testFindLongestPrefixWithInputContainingSpacesPartiallyMatchPhoneNumberPrefix() throws InvalidInputException {
        var input = "+44 20 7946 0958";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "+4420";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be '+4420'");
    }


    @Test
    public void testFindLongestPrefixWithNoneMatchingPhoneNumberPrefix() throws InvalidInputException {
        var input = "101 123 456 789";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithInputContainingNoSpacesPartiallyMatchPhoneNumberPrefix() throws InvalidInputException {
        var input = "+442079460958";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "+4420";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be '+4420'");
    }

    @Test
    public void testFindLongestPrefixWithInputContainingSpacesOnlyMatchPlusSignPhoneNumberPrefix() throws InvalidInputException {
        var input = "+301 123 456 789";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "+";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be '+'");
    }

    @Test
    public void testFindLongestPrefixWithMultiLinePhoneNumberShouldThrowException() {
        var input = "+1 800 355 \n5555";

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> prefixMatcher.findLongestPrefix(input),
                "Expected findLongestPrefix to throw InvalidTextInputException for multiline input"
        );

        String expectedMessage = "Multiline input is not supported";
        assertTrue(exception.getMessage().contains(expectedMessage),
                "Exception message should contain the expected reason");
    }


    @Test
    public void testFindLongestPrefixWithInputLessThanMatchedPrefix() throws InvalidInputException {
        var input = "foobar";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixPhoneNumberWithDashesAndSpaces() throws InvalidInputException {
        var input = "+44 20-7946-0958";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "+4420";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be '+4420'");
    }

    @Test
    public void testFindLongestPrefixWithNullShouldNotMatch() throws InvalidInputException {
        var longestPrefix = prefixMatcher.findLongestPrefix(null);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithVeryLargeInputAndShortPrefix() throws InvalidInputException {
        var suffix = "a".repeat(1000000);
        var input = "ashortprefix" + suffix;

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertTrue(longestPrefix.isPresent());
        var expectedLongestPrefix = "ashortprefix";
        assertEquals(expectedLongestPrefix, longestPrefix.get(), "Longest prefix should be 'ashortprefix'");
    }

    @Test
    public void testFindLongestPrefixWithVeryLargeInputAndNoMatch() throws InvalidInputException {
        var suffix = "l".repeat(1000000);
        var input = "ashortpre" + suffix;

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

    @Test
    public void testFindLongestPrefixWithInputMismatchedCase() throws InvalidInputException {
        var input = "foobarExecutioninprogress";

        var longestPrefix = prefixMatcher.findLongestPrefix(input);

        assertFalse(longestPrefix.isPresent());
    }

}
