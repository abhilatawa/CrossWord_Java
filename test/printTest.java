import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class printTest {
    @Test
    void parameterTests() {
        WordPlacement game = new WordPlacement();

        try {
            game.print(null);
        } catch (Exception e) {
            // nothing to do...expecting an exception
        }
    }

    @Test
    void exampleBoard() {
        WordPlacement game = new WordPlacement();
        String boardInfo = "T..3.3..T\n" +
                ".D..2..D.\n" +
                "..3...3..\n" +
                "3..2.2..3\n" +
                ".2..*..2.\n" +
                "3..2.2..3\n" +
                "..3...3..\n" +
                ".D..2..D.\n" +
                "T..3.3..T\n";
        StringWriter outString = new StringWriter();

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader(
                    boardInfo )) ),"sample puzzle from the specification" );
            game.print( new PrintWriter( outString ) );
            assertEquals( boardInfo, outString.toString(), "example board match up " );
        } catch (Exception e) {
            fail("not expecting an exception");
        }
    }

    @Test
    void afterBoardLoadFail() {
        WordPlacement game = new WordPlacement();
        StringWriter outString = new StringWriter();

        try {
            assertFalse( game.loadBoard( new BufferedReader( new StringReader(
                    "...\n" )) ),"bad board" );
        } catch (Exception e) {
            // expecting an exception as ok
        }

        game.print( new PrintWriter( outString ) );
        assertEquals( "", outString.toString(), "no board should be here" );
    }

    @Test
    void noBoardLoaded() {
        WordPlacement game = new WordPlacement();
        StringWriter outString = new StringWriter();

        game.print( new PrintWriter( outString ) );
        assertEquals( "", outString.toString(), "no board should be here" );
    }

    @Test
    void smallestPuzzle() {
        WordPlacement game = new WordPlacement();
        String puzzle = "*\n";
        StringWriter outString = new StringWriter();

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader(
                    puzzle )) ),"smallest board" );
            game.print( new PrintWriter( outString ) );
            assertEquals( puzzle, outString.toString(), "smallest board" );
        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

    @Test
    void oneRow() {
        WordPlacement game = new WordPlacement();
        String puzzle = ".*.\n";
        StringWriter outString = new StringWriter();

        try {
            assertTrue(game.loadBoard(new BufferedReader(new StringReader(
                    puzzle))), "smallest board");
            game.print(new PrintWriter(outString));
            assertEquals(puzzle, outString.toString(), "single row");
        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

    @Test
    void oneColumn() {
        WordPlacement game = new WordPlacement();
        String puzzle = ".\n*\n.\n";
        StringWriter outString = new StringWriter();

        try {
            assertTrue(game.loadBoard(new BufferedReader(new StringReader(
                    puzzle))), "smallest board");
            game.print(new PrintWriter(outString));
            assertEquals(puzzle, outString.toString(), "single column");
        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

    @Test
    void doubleLoad() {
        WordPlacement game = new WordPlacement();
        String puzzle1 = ".*.\n...\n";
        String puzzle2 = "*.\n..\n";
        StringWriter outString = new StringWriter();

        try {
            assertTrue(game.loadBoard(new BufferedReader(new StringReader( puzzle1))), "first board");
            assertTrue(game.loadBoard(new BufferedReader(new StringReader( puzzle2))), "second board");
            game.print(new PrintWriter(outString));
            assertEquals(puzzle2, outString.toString(), "match second puzzle only");
        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

}
