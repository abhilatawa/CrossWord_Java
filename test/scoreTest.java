import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class scoreTest {
    private void testFeasiblePuzzle(String puzzle, String dict, String letters, List<String> words, Integer optValue, Set<String> solutions ) {
        WordPlacement game = new WordPlacement();
        StringWriter outString = new StringWriter();

        try {
            // Set up a valid puzzle that has a unique best solution.  Forcing it by having the highest valued
            // letter want to fit onto the double letter tile.

            assertTrue(game.loadBoard(new BufferedReader(new StringReader( puzzle ))), "load board");
            assertTrue(game.dictionary(new BufferedReader(new StringReader( dict ))), "load dictionary");
            assertTrue(game.letterValue(new BufferedReader(new StringReader( letters ))), "load letters");

            // Solve the puzzle
            int value = game.placeWords( words );
            if (optValue != null) {
                assertEquals(optValue, value, "solve puzzle");
            }

            // The board should return with a solution
            game.print(new PrintWriter(outString));
            String outGame = outString.toString();

            if ((solutions != null) && (solutions.size() > 0)) {
                boolean matched = false;
                for( String picture : solutions ) {
                    if (picture.equals( outGame )) {
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    fail( "solved puzzle wrong format: " + outGame );
                    assertTrue(outGame.equals("a..\nbbb\na.c\na..\n...\n...\n"), "solved puzzle");
                }
            }
        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

    @Test
    void basicWordScoring() {
        testFeasiblePuzzle(
                "....\n.*..\n....\n....\n",
                "ac\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t16\n",
                new ArrayList<>( Arrays.asList( "ac" ) ),
                10,
                null );
    }

    @Test
    void letterMultipliers() {
        testFeasiblePuzzle(
                "....\n.*23\n....\n....\n",
                "ace\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "ace" ) ),
                78,
                null );
    }

    @Test
    void doubleWord() {
        testFeasiblePuzzle(
                "....\n.*D.\n....\n....\n",
                "ace\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "ace" ) ),
                60,
                null );
    }

    @Test
    void tripleWord() {
        testFeasiblePuzzle(
                "....\n.*T.\n....\n....\n",
                "ace\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "ace" ) ),
                90,
                null );
    }

    @Test
    void cascadeWordMultipliers() {
        testFeasiblePuzzle(
                "....\n.*TD\n....\n....\n",
                "ace\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "ace" ) ),
                180,
                null );
    }

    @Test
    void prependScore() {
        testFeasiblePuzzle(
                ".....\n.2*2.\n.....\n.....\n",
                "aaa\naa\naaaa\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "aaa", "aa" ) ),
                16,
                null );
    }

    @Test
    void append() {
        testFeasiblePuzzle(
                "....\n.*.4\n....\n....\n",
                "aa\nba\naab\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "aa", "ba" ) ),
                23,
                null );
    }

    @Test
    void basicCrossing() {
        testFeasiblePuzzle(
                "....\n.*..\n....\n....\n",
                "ace\naca\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "ace", "aca" ) ),
                36,
                null );
    }

    @Test
    void basicCrossing2() {
        testFeasiblePuzzle(
                "....\n.*..\n....\n....\n",
                "ace\naa\nea\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "ace", "aa", "ea" ) ),
                45,
                null );
    }

    //@Test
    void denseFill() {
        testFeasiblePuzzle(
                ".*DD\n....\n....\n",
                "aaa\nabc\nccc\nabaa\naac\n",
                "a\t1\nb\t2\nc\t4\nd\t8\ne\t10\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc", "ccc", "abaa" ) ),
                60,
                new HashSet<>( Arrays.asList( ".aaa\nabaa\n.ccc\n" ) ) );
    }

}
