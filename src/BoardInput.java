import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set

public class BoardInput {
    /**
     * accepts a sequence of lines, with one word per line, as the dictionary of the words
     * that will be allowed on the board. A word not in the dictionary must not appear on the board.
     * Return true if this dictionary is ready to be used for the puzzle. Return false if some error happened
     *
     * @param wordStream
     * @param validWords
     * @return true if ready to be used false otherwise
     */

    public boolean boarddictonary(BufferedReader wordStream, Set<String> validWords) {

        boolean wordsnotvalid = false;
        String word;
        if (wordStream != null) {
            try {
                while ((word = wordStream.readLine()) != null) {
                    if (!word.isEmpty()) {
                        if (word.matches("[a-z]+")) {
                            if (!validWords.contains(word)) {
                                validWords.add(word);
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            wordsnotvalid = true;
        }
        return wordsnotvalid;
    }

    /**
     * accepts a sequence of lines that identify the value of a letter when scoring.
     * A line contains two tab-separated values: the first is a letter and the second is an
     * integer that is the letter’s value when scoring. Treat a
     * repeated call to the method as a complete replacement of letter values. Return true if the given
     * letter values are ready to be used for the puzzle.
     * Return false if the value assignment for the letter will not be used.
     *
     * @param valueStream
     * @param newLetterValues
     * @return
     */

    public boolean charactervalues(BufferedReader valueStream, Map<Character, Integer> newLetterValues) {

        boolean InvalidLetterValues = false;

        if (valueStream != null) {

            String line;
            try {

                line = valueStream.readLine();
                while (line != null) {

                    /* Ignore blank lines. */

                    if (!line.trim().isEmpty()) {

                        /* Get the two evaluation parts. */
                        String[] parts = line.split("\t");
                        if (parts.length == 2) {
                            char letters = parts[0].charAt(0);
                            int numericvalues;

                            try {
                                numericvalues = Integer.parseInt(parts[1]);
                            } catch (NumberFormatException e) {
                                return false;
                                // Invalid integer value
                            }
                            newLetterValues.put(letters, numericvalues);
                        }
                    }
                }
                line = valueStream.readLine();
            }
        } catch(Exception e){
            return false;
        }
        InvalidLetterValues = true;
    }return InvalidLetterValues;
}
}
