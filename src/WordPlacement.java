import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class WordPlacement {
    private static final char REGULAR_CELL = '.';
    private static final char START_CELL = '*';
    private static final char LETTER_MULTIPLIER_2 = '2';
    private static final char LETTER_MULTIPLIER_3 = '3';
    private static final char DOUBLE_WORD_CELL = 'D';
    private static final char TRIPLE_WORD_CELL = 'T';
    int totalScore = 0;
    private  int startingRow = -1;
    private  int startingCol = -1;
    private int rows;
    private int columns;
    private char[][] scrabbleBoard;
    private Set<String> validWords;
    private Map<Character, Integer> newLetterValues;
    Map<String, Placewordsinfo> Placedwords;
    public class IntersectionInfo {
        private int row;
        private int col;
        private boolean isHorizontal;

        public IntersectionInfo(int row, int col, boolean isHorizontal) {
            this.row = row;
            this.col = col;
            this.isHorizontal = isHorizontal;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public boolean isHorizontal() {
            return isHorizontal;
        }
    }

    /**
     *
     */

    class Placewordsinfo {
        int startrowsafterfirstword;
        int startcolafterfirstword;
        boolean isHorizontal;

        public Placewordsinfo(int startrowsafterfirstword,int startcolafterfirstword,boolean isHorizontal){

            this.startrowsafterfirstword=startrowsafterfirstword;
            this.startcolafterfirstword=startcolafterfirstword;
            this.isHorizontal=isHorizontal;
        }

    }



    public WordPlacement() {

        validWords = new HashSet<>();
        newLetterValues = new HashMap<>();
        Placedwords=new HashMap<>();

    }


    public static final int Unsolved = -1;

    /**
     * Read a board in from the given
     * stream of data. Further description on the puzzle
     * input structure appears below. Return true if the puzzle is
     * read and ready to be solved. Ret
     *
     * @param puzzleStream
     * @return true if loads the board correctly false otherwise
     * @throws IOException
     */

    public boolean loadBoard(BufferedReader puzzleStream) throws IOException {

        if (puzzleStream != null) {
            String line;

            try {
                int startingcell = 0;
                List<String> Scrabblerows = new ArrayList<>();

                while ((line = puzzleStream.readLine()) != null) {
                    Scrabblerows.add(line);
                }

                // Dimensions of the puzzle board
                int rows = Scrabblerows.size();
                int columns = Scrabblerows.get(0).length(); // Assuming at least one row exists
                scrabbleBoard = new char[rows][columns];

                // Check if the number of rows exceeds the board size
                if (rows > scrabbleBoard.length) {
                    return false;
                }

                for (int i = 0; i < rows; i++) {
                    String row = Scrabblerows.get(i);
                    // Check if the length of the line matches the board's width
                    if (row.length() != columns) {
                        return false;
                    }
                    for (int j = 0; j < columns; j++) {
                        char cell = row.charAt(j);

                        // Check if the cell character is one of the allowed values
                        if (!(cell == REGULAR_CELL || cell == START_CELL ||
                                cell == LETTER_MULTIPLIER_2 || cell == LETTER_MULTIPLIER_3 ||
                                cell == DOUBLE_WORD_CELL || cell == TRIPLE_WORD_CELL)) {

                            // Invalid character in the puzzle
                            return false;
                        }
                        if (cell == '*') {
                            startingcell++;
                            startingRow = i;
                            startingCol = j;
                        }
                        scrabbleBoard[i][j] = cell;
                    }
                }

                // Check if there is exactly one starting cell
                if (startingcell != 1) {
                    return false;
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        // Null input stream
        return false;
    }


    /**
     * accepts a sequence of lines, with one word per line, as the dictionary of the words that will be allowed on the board. A word not in the dictionary must not appear on the board.
     * Return true if this dictionary is ready to be used for the puzzle. Return false if some error happened.
     *
     * @param wordStream
     * @return
     */
    public boolean dictionary(BufferedReader wordStream) {
        BoardInput br = new BoardInput();
        if (br.boarddictonary(wordStream, validWords)) {

            validWords.addAll(getValidWords());
            return true;
        }
        return false;
    }

    public Set<String> getValidWords() {
        return validWords;
    }

    /**
     *  accepts a sequence of lines that identify the value of a letter when scoring.
     *  A line contains two tab-separated values: the first is a letter and the second is
     *  an integer that is the letter’s value when scoring. Treat a repeated call to the method as
     *  a complete replacement of letter values. Return true if the given letter values are ready to be used for the puzzle.
     *  Return false if the value assignment for the letter will not be used.
     * @param valueStream
     * @return
     */
    public boolean letterValue(BufferedReader valueStream) {
        BoardInput br = new BoardInput();

        if (br.charactervalues(valueStream, newLetterValues)) {

                newLetterValues.putAll(getNewLettervalues());
                return true;
            }

            return false;
        }


    public Map<Character, Integer> getNewLettervalues() {
        return newLetterValues;
    }

    /**
     * print the solution of the puzzle to the output stream Print the
     * solution as an actual puzzle whose format matches that of the board input...
     * with the exception that cells
     * having letters will contain the letter rather than special notation for the board cell like “double letter”
     *
     *
     * @param outstream
     */
    public void print(PrintWriter outstream) {

        if (outstream == null) {
            return;
        }
        if (scrabbleBoard == null){
            return;
        }
        for (char[] chars : scrabbleBoard) {
            for (int j = 0; j < scrabbleBoard[0].length; j++) {
                char cell = chars[j];
                outstream.print(cell);

            }
            outstream.println();
        }
    }

    /**
     *always starting from an empty puzzle board, place each of the words in the “words” parameter into the puzzle,
     * inserted in the order of the list, so that each word generates the maximum number of points from the given
     * board as the word is placed. This solution is not necessarily the overall optimal word placement for the whole
     * list of words. The placed words accumulate in the WordPlacement object so the puzzle can be printed later. Return the final score of your solution accumulated as you added each word in turn. Return -1
     * if the sequence of words doesn’t lend itself to a solution with this one-at-a-time placement of words into the puzzle.
     *
     * @param words
     * @return  the final score of your solution accumulated as you added each word in turn. Return -1
     *  if the sequence of words doesn’t lend itself to a solution with this one-at-a-time placement of words into the puzzle.
     */
    public int placeWords(List<String> words) {


        if (words == null || words.isEmpty()) {
            return -1;
        }

        int index = 0;
        if (!validWords.contains(words.get(index))) {
            return -1; // The first word is not in the dictionary
        }

        int maxScore = Integer.MIN_VALUE;
        int rowIndex = -1;
        int colIndex = -1;
        boolean isHorizontal = true;

        int firstword = 0;
        int horizontalscorefirstword = calculateScore(words.get(firstword), startingRow, startingCol, true);

        int verticalscorefirstword = calculateScore(words.get(firstword), startingRow, startingCol, false);

        if (horizontalscorefirstword >= verticalscorefirstword) {
            boolean insertedhorizontally = canPlaceWordAt(words.get(firstword), startingRow, startingCol, true);
            if (insertedhorizontally) {
                rowIndex = startingRow;
                colIndex = startingCol;
                isHorizontal = true;
                placeWordAt(words.get(firstword), rowIndex, colIndex, isHorizontal);
                totalScore += horizontalscorefirstword;

            }
        } else {
            boolean insertedvertically = canPlaceWordAt(words.get(firstword), startingRow, startingCol, false);
            if (insertedvertically) {
                rowIndex = startingRow;
                colIndex = startingCol;
                isHorizontal = false;
                placeWordAt(words.get(firstword), rowIndex, colIndex, isHorizontal);
                totalScore += verticalscorefirstword;
            }
        }
        firstword++;

        for (int wordsposition = firstword; wordsposition < words.size(); wordsposition++) {
            String word = words.get(wordsposition);

            if (!validWords.contains(word)) {
                return -1; // Word is not in the dictionary
            }

            boolean ifplacedaftercrossing= placeWordCrossingFirst(words,word);

            for (String placedWord : Placedwords.keySet()) {
                // Finding the instersection with the already placed word

                for (int i = 0; i < placedWord.length(); i++) {
                    int crossingindex = word.indexOf(placedWord.charAt(i));
//
                    }

            }

        }
        return totalScore;
    }

    /**
     *
     * @param words
     * @param newWord is the string of word which has to be placed after the first word
     * @return true if the second word is ready to be placed onto the board , false otherwise
     */
    public boolean placeWordCrossingFirst( List<String> words,String newWord) {
        // Check for intersection between newWord and the first word

        for (String placedword : Placedwords.keySet()) {

            for (int i = 0; i < newWord.length(); i++) {
                for (int j = 0; j < placedword.length(); j++) {

                    boolean ifhorizontallyplaced = Placedwords.get(placedword).isHorizontal;
                    int crossingrows = ifhorizontallyplaced ? Placedwords.get(placedword).startrowsafterfirstword : Placedwords.get(placedword).startrowsafterfirstword + j;
                    int crossingcol = ifhorizontallyplaced ? Placedwords.get(placedword).startcolafterfirstword + j: Placedwords.get(placedword).startcolafterfirstword;

                        // Intersection found, decide the direction and position to place the word
                        if (canPlaceWordAt(newWord, startingRow, startingCol, true)) {
                            placeWordAt(newWord, crossingrows, crossingrows,true);
                            return true;
                        } else if (canPlaceWordAt(newWord,crossingrows,crossingcol,false)) {
                            placeWordAt(newWord, crossingrows, crossingrows,false);
                            return true;
                        }
                    }
                }
            }
            return false; // No intersection found to place the word
        }


    /**
     *
     * this method check the valid positions of the word to be placed on to the scrabble board.
     * If horizontally placed or if vertically placed
     * @param word
     * @param row
     * @param col
     * @param isHorizontal
     * @return true if word can be placed horizontally or vertically false otherwise
     */
    public boolean canPlaceWordAt(String word, int row, int col, boolean isHorizontal) {
        // Check if the word can be placed at the specified position
        if (isHorizontal) {
            if (col + word.length() > scrabbleBoard[0].length)  {
                return false; // Word exceeds the board boundaries
            }
            for (int i = 0; i < word.length(); i++) {
                  scrabbleBoard[row][col + i] = word.charAt(i);

                    // Conflict with existing letters on the board
            }
        } else {
            if (row + word.length() > scrabbleBoard.length) {
                return false; // Word exceeds the board boundaries
            }
            for (int i = 0; i < word.length(); i++) {
                scrabbleBoard[row + i][col]=word.charAt(i);

            }
        }
        return true;
    }

    /**
     *
     * @param word
     * @param row
     * @param col
     * @param isHorizontal
     * @return this method returns the total score of the word placed after calculating score with all the valid conditions
     */
    public int calculateScore(String word, int row, int col, boolean isHorizontal) {
        int score = 0;
        int wordMultiplier = 1;
        boolean isFirstWord = true;

        if (isHorizontal) {
            for (int i = 0; i < word.length(); i++) {
                char cell = scrabbleBoard[row][col + i];
                if(!newLetterValues.containsKey(word.charAt(i)))
                {
                    return 0;
                }


                int letterValue = newLetterValues.get(word.charAt(i));

                if(isFirstWord && cell == START_CELL){
                    wordMultiplier *=2;  //Double word score
                }
                if (cell == DOUBLE_WORD_CELL) {
                    wordMultiplier *= 2; // Double word score
                }
                else if (cell == TRIPLE_WORD_CELL) {
                    wordMultiplier *= 3; // Triple word score
                }

                if (cell == LETTER_MULTIPLIER_2) {
                    letterValue *= 2; // Double letter score
                } else if (cell == LETTER_MULTIPLIER_3) {
                    letterValue *= 3; // Triple letter score
                }
                score += letterValue;
                isFirstWord = false;
            }
        } else {
            for (int i = 0; i < word.length(); i++) {
                char cell = scrabbleBoard[row + i][col];
                int letterValue = newLetterValues.get(word.charAt(i));

                if(isFirstWord && cell == START_CELL) {
                    wordMultiplier *= 2;
                }
                if (cell == DOUBLE_WORD_CELL) {
                    wordMultiplier *= 2; // Double letter score
                } else if (cell == TRIPLE_WORD_CELL) {
                    wordMultiplier *= 3;; // Triple letter score
                }

                if (cell == LETTER_MULTIPLIER_2) {
                    letterValue *= 2; // Double word score
                } else if (cell == LETTER_MULTIPLIER_3) {
                    letterValue *= 3; // Triple word score
                }
                score += letterValue;
                isFirstWord= false;
            }
        }

        return score * wordMultiplier;

    }

    /**
     *
     *
     * This methods places the word onto the scrabble board if any possible position is true to place the word .
     * It uses a map to store the information of the placed word.
     * @param word
     * @param row
     * @param col
     * @param isHorizontal
     */

    public void placeWordAt(String word, int row, int col, boolean isHorizontal) {
        if (isHorizontal) {
            for (int i = 0; i < word.length(); i++) {
                scrabbleBoard[row][col + i] = word.charAt(i);
            }
        } else {
            for (int i = 0; i < word.length(); i++) {
                scrabbleBoard[row + i][col] = word.charAt(i);
            }
        }
        Placedwords.put(word, new Placewordsinfo(row,col,isHorizontal));
    }







    // Methods for the bonus parts of the assignment

    public int solve( Set<String> words ) {
        return Unsolved;
    }

    public List<String> wordOrder() {
        return null;
    }
}

