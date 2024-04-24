import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class placeWordsTest {
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
    void parameterTests() {
        WordPlacement game = new WordPlacement();
        String startBoard = "....\n.*..\n....\n....\n";
        List<String> emptyWords = new ArrayList<>();

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader( startBoard )) ),"load board" );
            assertTrue( game.dictionary( new BufferedReader( new StringReader( "one" )) ),"load dictionary" );
            assertTrue( game.letterValue( new BufferedReader( new StringReader( "e\t1\n" )) ),"load letters" );
            assertTrue( game.placeWords( null ) < 0, "null word");
            assertEquals( 0, game.placeWords( emptyWords ), "empty word");
        } catch (Exception e) {
            // Shouldn't fail
            fail( "No exception expected" );
        }
    }

    @Test
    void beyondBounds() {
        WordPlacement game = new WordPlacement();
        String startBoard = "...\n.*.\n...\n";
        StringWriter outString1 = new StringWriter();
        List<String> words = new ArrayList<>();
        words.add("cant");

        try {
            assertTrue( game.loadBoard( new BufferedReader( new StringReader( startBoard )) ),"load board" );
            assertTrue( game.dictionary( new BufferedReader( new StringReader( "cant\n" )) ),"load dictionary" );
            assertTrue( game.letterValue( new BufferedReader( new StringReader( "a\t1\nc\t1\nn\t1\nt\t1\n" )) ),"load letters" );
            assertTrue( game.placeWords( words ) < 0, "too small of a grid");
            game.print(new PrintWriter( outString1 ) );
            assertEquals( startBoard, outString1.toString(), "add grid too small" );

        } catch (Exception e) {
            // Shouldn't fail
            fail( "No exception expected" );
        }
    }

    @Test
    void placeOneHorizontal() {
        testFeasiblePuzzle(
                "....\n.*..\n....\n",
                "cant\n",
                "a\t1\nc\t1\nn\t1\nt\t1\n",
                new ArrayList<>( Arrays.asList( "cant" ) ),
                null,
                new HashSet<>( Arrays.asList( "....\ncant\n....\n" ) ) );
    }

    @Test
    void placeOneVertical() {
        testFeasiblePuzzle(
                "...\n.*.\n...\n...\n",
                "cant\n",
                "a\t1\nc\t1\nn\t1\nt\t1\n",
                new ArrayList<>( Arrays.asList( "cant" ) ),
                null,
                new HashSet<>( Arrays.asList( ".c.\n.a.\n.n.\n.t.\n" ) ) );
    }

    @Test
    void startTopRow() {
        testFeasiblePuzzle(
                "*...\n....\n....\n",
                "cant\n",
                "a\t1\nc\t1\nn\t1\nt\t1\n",
                new ArrayList<>( Arrays.asList( "cant" ) ),
                null,
                new HashSet<>( Arrays.asList( "cant\n....\n....\n" ) ) );
    }

    @Test
    void startLeftColumn() {
        testFeasiblePuzzle(
                "*..\n...\n...\n...\n",
                "cant\n",
                "a\t1\nc\t1\nn\t1\nt\t1\n",
                new ArrayList<>( Arrays.asList( "cant" ) ),
                null,
                new HashSet<>( Arrays.asList( "c..\na..\nn..\nt..\n" ) ) );
    }

    @Test
    void startRightColumn() {
        testFeasiblePuzzle(
                "...\n...\n...\n..*\n",
                "cant\n",
                "a\t1\nc\t1\nn\t1\nt\t1\n",
                new ArrayList<>( Arrays.asList( "cant" ) ),
                null,
                new HashSet<>( Arrays.asList( "..c\n..a\n..n\n..t\n" ) ) );
    }

    @Test
    void startBottomRow() {
        testFeasiblePuzzle(
                "....\n....\n...*\n",
                "cant\n",
                "a\t1\nc\t1\nn\t1\nt\t1\n",
                new ArrayList<>( Arrays.asList( "cant" ) ),
                null,
                new HashSet<>( Arrays.asList( "....\n....\ncant\n" ) ) );
    }

    @Test
    void startScoreLeft() {
        testFeasiblePuzzle(
                ".....\n.....\n..*D.\n.....\n.....\n",
                "ab\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "ab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\n..ab.\n.....\n.....\n" ) ) );
    }

    @Test
    void startScoreRight() {
        testFeasiblePuzzle(
                ".....\n.....\n.T*..\n.....\n.....\n",
                "ab\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "ab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\n.ab..\n.....\n.....\n" ) ) );
    }

    @Test
    void startScoreUp() {
        testFeasiblePuzzle(
                ".....\n..2..\n..*..\n.....\n.....\n",
                "ab\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "ab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n..a..\n..b..\n.....\n.....\n" ) ) );
    }

    @Test
    void startScoreDown() {
        testFeasiblePuzzle(
                ".....\n.....\n..*..\n..3..\n.....\n",
                "ab\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "ab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\n..a..\n..b..\n.....\n" ) ) );
    }

    @Test
    void crossUsesFirstLetter() {
        // horizontal
        testFeasiblePuzzle(
                ".....\n.D*D.\n.....\n.D...\n.....\n",
                "aaa\nabc\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.aaa.\n.b...\n.c...\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n.D*D.\n.....\n..D..\n.....\n",
                "aaa\nabc\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.aaa.\n..b..\n..c..\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n.D*D.\n.....\n...D.\n.....\n",
                "aaa\nabc\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.aaa.\n...b.\n...c.\n.....\n" ) ) );

        // vertical
        testFeasiblePuzzle(
                ".DD..\n.*...\n.D...\n.....\n.....\n",
                "aaa\nabc\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".abc.\n.a...\n.a...\n.....\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".D...\n.*D..\n.D...\n.....\n.....\n",
                "aaa\nabc\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".a...\n.abc.\n.a...\n.....\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".D...\n.*...\n.DD..\n.....\n.....\n",
                "aaa\nabc\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".a...\n.a...\n.abc.\n.....\n.....\n" ) ) );
    }

    @Test
    void crossUsesLastLetter() {
        // horizontal
        testFeasiblePuzzle(
                ".....\n.D...\n.....\n.D*D.\n.....\n",
                "aaa\ncba\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cba" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.c...\n.b...\n.aaa.\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n..D..\n.....\n.D*D.\n.....\n",
                "aaa\ncba\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cba" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n..c..\n..b..\n.aaa.\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n...D.\n.....\n.D*D.\n.....\n",
                "aaa\ncba\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cba" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n...c.\n...b.\n.aaa.\n.....\n" ) ) );

        // vertical
        testFeasiblePuzzle(
                ".....\n.....\n.DD..\n..*..\n..D..\n",
                "aaa\ncba\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cba" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\ncba..\n..a..\n..a..\n" ) ) );
        testFeasiblePuzzle(
                ".....\n.....\n..D..\n.D*..\n..D..\n",
                "aaa\ncba\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cba" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\n..a..\ncba..\n..a..\n" ) ) );
        testFeasiblePuzzle(
                ".....\n.....\n..D..\n..*..\n.DD..\n",
                "aaa\ncba\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cba" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\n..a..\n..a..\ncba..\n" ) ) );

    }

    @Test
    void crossUsesMiddleLetter() {
        // horizontal
        testFeasiblePuzzle(
                ".....\n.D...\n.D*D.\n.....\n.....\n",
                "aaa\ncab\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.c...\n.aaa.\n.b...\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n..D..\n.D*D.\n.....\n.....\n",
                "aaa\ncab\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cab" ) ),
                30,
                new HashSet<>( Arrays.asList( ".....\n..c..\n.aaa.\n..b..\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n...D.\n.D*D.\n.....\n.....\n",
                "aaa\ncab\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n...c.\n.aaa.\n...b.\n.....\n" ) ) );

        // vertical
        testFeasiblePuzzle(
                ".....\n.DD..\n..*..\n..D..\n.....\n",
                "aaa\ncab\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.cab.\n..a..\n..a..\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n..D..\n.D*..\n..D..\n.....\n",
                "aaa\ncab\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n..a..\n.cab.\n..a..\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n..D..\n..*..\n.DD..\n.....\n",
                "aaa\ncab\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "cab" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n..a..\n..a..\n.cab.\n.....\n" ) ) );
    }

    @Test
    void crossBeforeWord() {
        // horizontal
        testFeasiblePuzzle(
                "D....\n.....\n.D*D.\n.....\n.....\n",
                "aaa\nabc\naaaa\nbaaa\ncaaa\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( "a....\nb....\ncaaa.\n.....\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\nD....\n.D*D.\nD....\n.....\n",
                "aaa\nabc\naaaa\nbaaa\ncaaa\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\na....\nbaaa.\nc....\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n.....\n.D*D.\n.....\nD....\n",
                "aaa\nabc\naaaa\nbaaa\ncaaa\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\naaaa.\nb....\nc....\n" ) ) );

        // vertical
        testFeasiblePuzzle(
                "D....\n..D..\n..*..\n..D..\n.....\n",
                "aaa\nabc\naaaa\nbaaa\ncaaa\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( "abc..\n..a..\n..a..\n..a..\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".D.D.\n..D..\n..*..\n..D..\n.....\n",
                "aaa\nabc\naaaa\nbaaa\ncaaa\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".abc.\n..a..\n..a..\n..a..\n.....\n" ) ) );
        testFeasiblePuzzle(
                "....D\n..D..\n..*..\n..D..\n.....\n",
                "aaa\nabc\naaaa\nbaaa\ncaaa\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( "..abc\n..a..\n..a..\n..a..\n.....\n" ) ) );
    }

    @Test
    void crossAfterWord() {
        // horizontal
        testFeasiblePuzzle(
                "....D\n.....\n.D*D.\n.....\n.....\n",
                "aaa\nabc\naaaa\naaab\naaac\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( "....a\n....b\n.aaac\n.....\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n....D\n.D*D.\n....D\n.....\n",
                "aaa\nabc\naaaa\naaab\naaac\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n....a\n.aaab\n....c\n.....\n" ) ) );
        testFeasiblePuzzle(
                ".....\n.....\n.D*D.\n.....\n....D\n",
                "aaa\nabc\naaaa\naaab\naaac\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n.....\n.aaaa\n....b\n....c\n" ) ) );

        // vertical
        testFeasiblePuzzle(
                ".....\n..D..\n..*..\n..D..\nD....\n",
                "aaa\nabc\naaaa\naaab\naaac\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n..a..\n..a..\n..a..\nabc..\n" ) ) );
        testFeasiblePuzzle(
                ".....\n..D..\n..*..\n..D..\n.D.D.\n",
                "aaa\nabc\naaaa\naaab\naaac\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n..a..\n..a..\n..a..\n.abc.\n" ) ) );
        testFeasiblePuzzle(
                ".....\n..D..\n..*..\n..D..\n....D\n",
                "aaa\nabc\naaaa\naaab\naaac\n",
                "a\t1\nb\t1\nc\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abc" ) ),
                null,
                new HashSet<>( Arrays.asList( ".....\n..a..\n..a..\n..a..\n..abc\n" ) ) );

    }

    @Test
    void multiCrossingWord() {
        testFeasiblePuzzle(
                ".......\n.......\n.*DDDD.\n.D.....\n.DD....\n.......\n...D...\n",
                "abcde\naxy\ncstuy\nayete\n",
                "a\t1\nb\t1\nc\t1\nd\t1\ne\t2\nx\t1\ny\t1\ns\t1\nt\t1\nu\t1\n",
                new ArrayList<>( Arrays.asList( "abcde", "axy", "cstuy", "ayete" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.......\n.abcde.\n.x.s...\nayete..\n...u...\n...y...\n" ) ) );
    }

    @Test
    void embedWord() {
        testFeasiblePuzzle(
                ".......\n.......\n.*DD...\n.D.....\n.......\n.......\n.......\n",
                "aaa\nabb\nbcc\nxbxdx\naxc\nadc\n",
                "a\t1\nb\t1\nc\t1\nd\t1\ne\t2\nx\t1\ny\t1\ns\t1\nt\t1\nu\t1\nx\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "abb", "bcc", "xbxdx" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.......\n.aaa...\nxbxdx..\n.bcc...\n.......\n.......\n" ) ) );
    }

    @Test
    void badWordLetter() {
        WordPlacement game = new WordPlacement();
        StringWriter outString = new StringWriter();
        String boardConfig = ".......\n.......\n.*.....\n.......\n";

        try {
            // Set up a valid puzzle that has a unique best solution.  Forcing it by having the highest valued
            // letter want to fit onto the double letter tile.

            assertTrue(game.loadBoard(new BufferedReader(new StringReader( boardConfig ))), "load board");
            assertTrue(game.dictionary(new BufferedReader(new StringReader( "one\ntwo\nthree\n" ))), "load dictionary");
            assertTrue(game.letterValue(new BufferedReader(new StringReader( "o\t1\nn\t1\ne\t1\nt\t1\nw\t1\nh\t1\n" ))), "load letters");

            // Missing the "r"
            List<String> words = new ArrayList( Arrays.asList( "one", "two", "three") );
            int value = game.placeWords( words );
            assertTrue( value < 0, "use letter not in frequencies" );

            // The board should return with a solution
            game.print(new PrintWriter(outString));
            String outGame = outString.toString();

            assertEquals( boardConfig, outGame, "ending configuration" );

        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

    @Test
    void noSolution() {
        WordPlacement game = new WordPlacement();
        StringWriter outString = new StringWriter();
        String boardConfig = "..*....\n.......\n.....2.\n.......\n";

        try {
            // Set up a valid puzzle that has a unique best solution.  Forcing it by having the highest valued
            // letter want to fit onto the double letter tile.

            assertTrue(game.loadBoard(new BufferedReader(new StringReader( boardConfig ))), "load board");
            assertTrue(game.dictionary(new BufferedReader(new StringReader( "aaa\nabb\nbcc\nced\n" ))), "load dictionary");
            assertTrue(game.letterValue(new BufferedReader(new StringReader( "a\t1\nb\t1\nc\t1\nd\t1\ne\t1\n" ))), "load letters");

            // Missing the "r"
            List<String> words = new ArrayList( Arrays.asList( "aaa", "abb", "bcc", "ced") );
            int value = game.placeWords( words );
            assertTrue( value < 0, "solution doesn't fit the grid" );

            // The board should return with a solution
            game.print(new PrintWriter(outString));
            String outGame = outString.toString();

            assertEquals( boardConfig, outGame, "ending configuration" );

        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

    @Test
    void greedyDoesntWork() {
        WordPlacement game = new WordPlacement();
        StringWriter outString = new StringWriter();
        String boardConfig = ".*DD..\n.D....\n......\n......\n";

        try {
            // Set up a valid puzzle that has a unique best solution.  Forcing it by having the highest valued
            // letter want to fit onto the double letter tile.

            assertTrue(game.loadBoard(new BufferedReader(new StringReader( boardConfig ))), "load board");
            assertTrue(game.dictionary(new BufferedReader(new StringReader( "aaa\nabb\nbcc\ncab\n" ))), "load dictionary");
            assertTrue(game.letterValue(new BufferedReader(new StringReader( "a\t1\nb\t1\nc\t1\n" ))), "load letters");

            // Missing the "r"
            List<String> words = new ArrayList( Arrays.asList( "aaa", "abb", "bcc", "cab") );
            int value = game.placeWords( words );
            assertTrue( value < 0, "solution doesn't fit the grid" );

            // The board should return with a solution
            game.print(new PrintWriter(outString));
            String outGame = outString.toString();

            assertEquals( boardConfig, outGame, "ending configuration" );

        } catch (Exception e) {
            // shouldn't fail
            fail("Not expecting exception");
        }
    }

    @Test
    void wordAboveParallel() {
        testFeasiblePuzzle(
                ".......\n.......\n.*DD...\n.......\n.......\n",
                "aaa\nbbb\nba\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bbb" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.bbb...\n.aaa...\n.......\n.......\n" ) ) );
    }

    @Test
    void wordBelowParallel() {
        testFeasiblePuzzle(
                ".......\n.......\n.*DD...\n.......\n.......\n",
                "aaa\nbbb\nab\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bbb" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.......\n.aaa...\n.bbb...\n.......\n" ) ) );
    }

    @Test
    void wordLeftParallel() {
        testFeasiblePuzzle(
                ".......\n..*....\n..D....\n..D....\n.......\n",
                "aaa\nbbb\nba\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bbb" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.ba....\n.ba....\n.ba....\n.......\n" ) ) );
    }

    @Test
    void wordRightParallel() {
        testFeasiblePuzzle(
                ".......\n..*....\n..D....\n..D....\n.......\n",
                "aaa\nbbb\nab\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bbb" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n..ab...\n..ab...\n..ab...\n.......\n" ) ) );
    }

    @Test
    void fullPrepend() {
        // horizontal

        testFeasiblePuzzle(
                ".......\n.......\nD.*DD..\n.......\n.......\n",
                "aaa\nbb\nbbaaa\naaabb\nbba\nabb\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bb" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.......\nbbaaa..\n.......\n.......\n" ) ) );

        // vertical

        testFeasiblePuzzle(
                "..D....\n.......\n..*....\n..D....\n..D....\n",
                "aaa\nbb\nbbaaa\naaabb\nbba\nabb\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bb" ) ),
                null,
                new HashSet<>( Arrays.asList( "..b....\n..b....\n..a....\n..a....\n..a....\n" ) ) );

    }

    @Test
    void fullAppend() {
        // horizontal
        testFeasiblePuzzle(
                ".......\n.......\n..*DD.D\n.......\n.......\n",
                "aaa\nbb\nbbaaa\naaabb\nbba\nabb\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bb" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.......\n..aaabb\n.......\n.......\n" ) ) );

        // vertical
        testFeasiblePuzzle(
                "..D....\n..D....\n..*....\n.......\n..D....\n",
                "aaa\nbb\nbbaaa\naaabb\nbba\nabb\n",
                "a\t1\nb\t1\n",
                new ArrayList<>( Arrays.asList( "aaa", "bb" ) ),
                null,
                new HashSet<>( Arrays.asList( "..a....\n..a....\n..a....\n..b....\n..b....\n" ) ) );

    }

    @Test
    void bridgeWords() {
        testFeasiblePuzzle(
                ".......\n.......\n.*DD...\n.D.....\n.......\n.......\n.......\n",
                "abcd\naef\ndgh\nof\nhi\njk\nofjkhi\n",
                "a\t1\nb\t1\nc\t1\nd\t1\ne\t1\nf\t1\ng\t1\nh\t1\ni\t1\nj\t1\nk\t1\no\t1\n",
                new ArrayList<>( Arrays.asList( "abcd", "aef", "dgh", "of", "hi", "jk" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.......\n.abcd..\n.e..g..\nofjkhi.\n.......\n.......\n" ) ) );
    }

    @Test
    void badBridge() {
        // Set up to bridge words, but the fully-bridged word isn't in the dictionary
        testFeasiblePuzzle(
                ".......\n.......\n.*DD...\n.D.....\n.......\n.......\n.......\n",
                "abcd\naef\ndgh\nof\nhi\njk\nofjkh\n",
                "a\t1\nb\t1\nc\t1\nd\t1\ne\t1\nf\t1\ng\t1\nh\t1\ni\t1\nj\t1\nk\t1\no\t1\n",
                new ArrayList<>( Arrays.asList( "abcd", "aef", "dgh", "of", "hi", "jk" ) ),
                null,
                new HashSet<>( Arrays.asList( ".......\n.......\n.*DD...\n.D.....\n.......\n.......\n.......\n" ) ) );
    }

}
