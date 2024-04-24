import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class bigExampleTest {
    private static final String scrabbleValues =
            "a\t1\n"
                    + "e\t1\n"
                    + "i\t1\n"
                    + "o\t1\n"
                    + "u\t1\n"
                    + "l\t1\n"
                    + "n\t1\n"
                    + "s\t1\n"
                    + "t\t1\n"
                    + "r\t1\n"
                    + "d\t2\n"
                    + "g\t2\n"
                    + "b\t3\n"
                    + "c\t3\n"
                    + "m\t3\n"
                    + "p\t3\n"
                    + "f\t4\n"
                    + "h\t4\n"
                    + "v\t4\n"
                    + "w\t4\n"
                    + "y\t4\n"
                    + "k\t5\n"
                    + "j\t8\n"
                    + "x\t8\n"
                    + "q\t10\n"
                    + "z\t10\n";

    @Test
    public void testFeasiblePuzzle( ) {
        WordPlacement game = new WordPlacement();
        StringWriter outString = new StringWriter();
        String boardString = "T..3.3..T\n" +
                ".D..2..D.\n" +
                "..3...3..\n" +
                "3..2.2..3\n" +
                ".2..*..2.\n" +
                "3..2.2..3\n" +
                "..3...3..\n" +
                ".D..2..D.\n" +
                "T..3.3..T\n";

        try {
            // Set up a valid puzzle that has a unique best solution.  Forcing it by having the highest valued
            // letter want to fit onto the double letter tile.

            BufferedReader wordFile = new BufferedReader( new FileReader( "words.txt" ));
            assertTrue(game.loadBoard(new BufferedReader(new StringReader( boardString ))), "load board");
            assertTrue(game.dictionary( wordFile ), "load dictionary");
            assertTrue(game.letterValue(new BufferedReader(new StringReader( scrabbleValues ))), "load letters");

            List<String> words = new ArrayList( Arrays.asList("patent", "emit", "opera", "pasta", "said", "say", "daily", "old", "odd", "too", "italicize" ) );

            // Solve the puzzle, just hoping not to crash...

            int value = game.placeWords( words );
            assertTrue( value > 0, "there is a solution to the set of words in this order with greedy" );

            // Don't show the outcomes for now
            if (false) {
                game.print(new PrintWriter(outString));
                System.out.println(outString.toString());
            }

        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

}
