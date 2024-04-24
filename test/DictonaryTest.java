import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.Test;import java.io.BufferedReader;import java.io.StringReader;import java.util.Set;import static org.junit.jupiter.api.Assertions.*;public class DictonaryTest {    private WordPlacement wordPlacement;    @BeforeEach    public void setUp() {        wordPlacement = new WordPlacement();    }    @Test    public void testValidDictionary() {        String dictionaryData = "apple\nbanana\ncherry";        BufferedReader wordStream = new BufferedReader(new StringReader(dictionaryData));        assertTrue(wordPlacement.dictionary(wordStream));        assertEquals(Set.of("apple", "banana", "cherry"), wordPlacement.getValidWords());    }    @Test    public void testDictionaryWithUppercases() {        String dictionaryData = "Apple\nBanana\nCherry";        BufferedReader wordStream = new BufferedReader(new StringReader(dictionaryData));        assertFalse(wordPlacement.dictionary(wordStream));    }    @Test    public void testDictionaryWithEmptyWords() {        String dictionaryData = "apple\n\nbanana\n";        BufferedReader wordStream = new BufferedReader(new StringReader(dictionaryData));        assertFalse(wordPlacement.dictionary(wordStream));    }    @Test    public void testDictionaryWithInvalidCharacters() {        String dictionaryData = "apple\n123\nbanana";        BufferedReader wordStream = new BufferedReader(new StringReader(dictionaryData));        assertFalse(wordPlacement.dictionary(wordStream));    }    @Test    public void testDictionaryWithRepetitiveWords() {        String dictionaryData = "apple\nbanana\napple";        BufferedReader wordStream = new BufferedReader(new StringReader(dictionaryData));        assertFalse(wordPlacement.dictionary(wordStream));    }    @Test    public void testDictionaryWithWhitespaceInWords() {        String dictionaryData = "apple\n  banana  \n cherry ";        BufferedReader wordStream = new BufferedReader(new StringReader(dictionaryData));        assertFalse(wordPlacement.dictionary(wordStream));    }}