import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import java.io.StringReader;

class loadBoardTest {

    @Test
    void parameterTests() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard(null), "null board" );
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( "" )) ),"empty board" );
        } catch (Exception e) {
            // nothing to do...expecting an exception
        }
    }

    @Test
    void oneRow() {
        WordPlacement game = new WordPlacement();

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader( ".*.\n" )) ),"single row" );
        } catch (Exception e) {
            // There should be no exception generated
            fail("No exception expected");
        }
    }

    @Test
    void oneColumn() {
        WordPlacement game = new WordPlacement();

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader( ".\n*\n.\n" )) ),"single column" );
        } catch (Exception e) {
            // There should be no exception generated
            fail("No exception expected");
        }
    }

    @Test
    void noStart() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( "...\n...\n" )) ),"no start" );
        } catch (Exception e) {
            // An exception here could be acceptable
        }
    }

    @Test
    void tooManyStarts() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( ".*.\n.*.\n" )) ),"too many starts" );
        } catch (Exception e) {
            // An exception here could be acceptable
        }
    }

    @Test
    void badBoardCharacter() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( ".*.\n.x.\n" )) ),"bad board input character" );
        } catch (Exception e) {
            // An exception here could be acceptable
        }
    }

    @Test
    void hasShortRow() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( ".*.\n..\n...\n" )) ),"short row" );
        } catch (Exception e) {
            // An exception here could be acceptable
        }
    }

    @Test
    void hasLongRow() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( "...\n.*.\n....\n" )) ),"long row" );
        } catch (Exception e) {
            // An exception here could be acceptable
        }
    }

    @Test
    void loadBoardOverBoard() {
        WordPlacement game = new WordPlacement();

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader( "...\n.*.\n...\n" )) ),"first load" );
            assertTrue( game.loadBoard( new BufferedReader( new StringReader( "....\n.*..\n....\n....\n" )) ),"second load" );
        } catch (Exception e) {
            fail("not expecting an exception");
        }
    }

    @Test
    void loadAllCharsInBoard() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( "..............\n.DT*123456789.\n..............\n" )) ),"all possible characters" );
        } catch (Exception e) {
            fail("not expecting an exception");
        }
    }

    @Test
    void zeroLetterMultiplier() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader( "....\n.*0.\n....\n" )) ),"zero multiplier" );
        } catch (Exception e) {
            // An exception here could be accpetable
        }
    }

    @Test
    void exampleBoard() {
        WordPlacement game = new WordPlacement();

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader(
                    "T..3.3..T\n" +
                            ".D..2..D.\n" +
                            "..3...3..\n" +
                            "3..2.2..3\n" +
                            ".2..*..2.\n" +
                            "3..2.2..3\n" +
                            "..3...3..\n" +
                            ".D..2..D.\n" +
                            "T..3.3..T\n" )) ),"sample puzzle from the specification" );
        } catch (Exception e) {
            fail("not expecting an exception");
        }
    }

}