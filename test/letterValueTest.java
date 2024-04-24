import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class letterValueTest {
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
    void parameterTests() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.letterValue(null), "null stream" );
            assertFalse( game.letterValue( new BufferedReader( new StringReader( "" )) ),"empty stream" );
        } catch (Exception e) {
            // nothing to do...expecting an exception
        }
    }

    @Test
    void singleLine() {
        WordPlacement game = new WordPlacement();

        try {
            assertTrue( game.letterValue( new BufferedReader( new StringReader( "a\t1\n" )) ),"single letter" );
        } catch (Exception e) {
            // Shouldn't fail
            fail("No exception expected");
        }
    }

    @Test
    void manyLine() {
        WordPlacement game = new WordPlacement();

        try {
            assertTrue( game.letterValue( new BufferedReader( new StringReader( scrabbleValues )) ),"many letters" );
        } catch (Exception e) {
            // Shouldn't fail
            fail("No exception expected");
        }
    }

    @Test
    void tooFewFields() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.letterValue( new BufferedReader( new StringReader( "a\n" )) ),"too few fields" );
        } catch (Exception e) {
            // nothing to do...expecting an exception
        }
    }

    @Test
    void tooManyFields() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.letterValue( new BufferedReader( new StringReader( "a\t1\t3\n" )) ),"too many fields" );
        } catch (Exception e) {
            // nothing to do...expecting an exception
        }
    }

    @Test
    void badValues() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.letterValue( new BufferedReader( new StringReader( "a\t-1\nb\t0\nc\tx\n" )) ),"bad value" );
        } catch (Exception e) {
            // nothing to do...expecting an exception
        }
    }

    @Test
    void characterIsAString() {
        WordPlacement game = new WordPlacement();

        try {
            assertFalse( game.letterValue( new BufferedReader( new StringReader( "ab\t1\n" )) ),"not a character mapping" );
        } catch (Exception e) {
            // nothing to do...expecting an exception
        }
    }

    @Test
    void overwritePrevious() {
        WordPlacement game = new WordPlacement();

        try {
            assertTrue( game.letterValue( new BufferedReader( new StringReader( "a\t1\n" )) ),"first load" );
            assertTrue( game.letterValue( new BufferedReader( new StringReader( "b\t9\n" )) ),"second load" );
        } catch (Exception e) {
            // Shouldn't fail
            fail("No exception expected");
        }
    }

}