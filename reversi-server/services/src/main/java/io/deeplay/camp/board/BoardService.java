package io.deeplay.camp.board;

import io.deeplay.camp.entity.Board;
import io.deeplay.camp.entity.GameFinished;
import io.deeplay.camp.entity.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * The BoardService class manages the game logic for a board game,
 * handling player moves, validating moves, and determining game outcomes.
 */
public class BoardService {

    private final Board board;
    private long blackChips;
    private long whiteChips;
    private long blackValidMoves;
    private long whiteValidMoves;
    /**
     * Constructs a BoardService instance with the specified board.
     * @param board The initial game board.
     */
    public BoardService(Board board) {
        this.board = board;
        this.blackChips = board.getBlackChips();
        this.whiteChips = board.getWhiteChips();
    }

    /**
     * Places a piece on the board at the specified coordinates for the specified player.
     * @param x The x-coordinate on the board.
     * @param y The y-coordinate on the board.
     * @param player The player making the move.
     */
    public void setPiece(int x, int y, int player) {
        long piece = 1L << (x + 8 * y);
        long tempChips;
        long currentChips = 0; long invertedChips = 0;
        int a; int b;
        if (player == 1) {
            currentChips = blackChips;
            invertedChips = whiteChips;
        } else if (player == 2) {
            currentChips = whiteChips;
            invertedChips = blackChips;
        } else System.out.println("Who are moving?");

        if (isValidMove(x, y, player)) {
            currentChips |= piece;
            // Горизонтали
            if(x > 0){
                tempChips = 0;
                for (int i = x - 1; i >= 0; i--) {
                    if ((invertedChips & (1L << (i + 8 * y))) != 0) {
                        tempChips |= (1L << (i + 8 * y));
                    } else if ((currentChips & (1L << (i + 8 * y))) != 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            if(x < 7){
                tempChips = 0;
                for (int i = x + 1; i < 8; i++) {
                    if ((invertedChips & (1L << (i + 8 * y))) != 0) {
                        tempChips |= (1L << (i + 8 * y));
                    } else if ((currentChips & (1L << (i + 8 * y))) != 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            // Вертикали
            if(y > 0){
                tempChips = 0;
                for (int j = y - 1; j >= 0; j--) {
                    if ((invertedChips & (1L << (x + 8 * j))) != 0) {
                        tempChips |= (1L << (x + 8 * j));
                    } else if ((currentChips & (1L << (x + 8 * j))) != 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            if(y < 7){
                tempChips = 0;
                for (int j = y + 1; j < 8; j++) {
                    if ((invertedChips & (1L << (x + 8 * j))) != 0) {
                        tempChips |= (1L << (x + 8 * j));
                    } else if ((currentChips & (1L << (x + 8 * j))) != 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            // Диагонали
            if(y > 0 && x > 0){
                tempChips = 0; a = x - 1;
                for (int j = y - 1; j >= 0; j--) {
                    if ((invertedChips & (1L << (a + 8 * j))) != 0 && a >= 0) {
                        tempChips |= (1L << (a + 8 * j));
                        a--;
                    } else if ((currentChips & (1L << (a + 8 * j))) != 0 && a >= 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            if(y > 0 && x < 7){
                tempChips = 0; b = x + 1;
                for (int j = y - 1; j >= 0; j--) {
                    if ((invertedChips & (1L << (b + 8 * j))) != 0) {
                        tempChips |= (1L << (b + 8 * j));
                        b++;
                    } else if ((currentChips & (1L << (b + 8 * j))) != 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            if(y < 7 && x > 0){
                tempChips = 0; a = x - 1;
                for (int j = y + 1; j < 8; j++) {
                    if ((invertedChips & (1L << (a + 8 * j))) != 0) {
                        tempChips |= (1L << (a + 8 * j));
                        a--;
                    } else if ((currentChips & (1L << (a + 8 * j))) != 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            if(y < 7 && x < 7){
                tempChips = 0; b = x + 1;
                for (int j = y + 1; j < 8; j++) {
                    if ((invertedChips & (1L << (b + 8 * j))) != 0) {
                        tempChips |= (1L << (b + 8 * j));
                        b++;
                    } else if ((currentChips & (1L << (b + 8 * j))) != 0) {
                        invertedChips &= ~tempChips;
                        currentChips |= tempChips;
                        break;
                    } else break;
                }
            }
            if (player == 1) {
                blackChips = currentChips;
                whiteChips = invertedChips;
                board.setWhiteChips(whiteChips);
                board.setBlackChips(blackChips);
            } else {
                whiteChips = currentChips;
                blackChips = invertedChips;
                board.setWhiteChips(whiteChips);
                board.setBlackChips(blackChips);
            }
        }
    }

    /**
     * Removes a piece from the specified coordinates on the board.
     * @param x The x-coordinate of the piece to be removed.
     * @param y The y-coordinate of the piece to be removed.
     */
    public void removePiece(int x, int y) {
        long mask = ~(1L << (x + 8 * y));
        blackChips &= mask;
        whiteChips &= mask;
    }

    /**
     * Checks if there is a piece at the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if a piece exists at the specified coordinates, false otherwise.
     */
    public boolean hasPiece(int x, int y) {
        long mask = 1L << (x + 8 * y);
        return ((blackChips & mask) != 0) || ((whiteChips & mask) != 0);
    }

    /**
     * Returns the current board.
     * @return The current board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Checks if the move at the specified coordinates is valid for the specified player.
     * @param x The x-coordinate of the move.
     * @param y The y-coordinate of the move.
     * @param player The player making the move.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValidMove(int x, int y, int player) {
        createValidMoves();
        if (player == 1) {
            return (blackValidMoves & (1L << (x + 8 * y))) != 0;
        } else if (player == 2) {
            return (whiteValidMoves & (1L << (x + 8 * y))) != 0;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the valid moves for the specified player.
     * @param player The player for whom to get valid moves.
     * @return A bitmask of valid moves.
     */
    public long getValidMoves(int player) {
        createValidMoves();
        long validMoves = 0x0000000000000000L;
        if (player == 1) {
            validMoves |= blackValidMoves;
        } else if (player == 2) {
            validMoves |= whiteValidMoves;
        }
        return validMoves;
    }

    /**
     * Calculates and returns the scores for both players.
     * @return An array containing the scores of black and white players.
     */
    public int[] score() {
        int[] score = new int[2];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                long mask = 1L << (x + 8 * y);
                if ((blackChips & mask) != 0) {
                    score[0]++;
                } else if ((whiteChips & mask) != 0) {
                    score[1]++;
                }
            }
        }
        return score;
    }

    /**
     * Generates valid moves for the current player.
     */
    public void createValidMoves(){
        long allChips = blackChips | whiteChips;
        blackValidMoves = 0;
        whiteValidMoves = 0;
        long targetChip;
        long validMoves = 0;
        int a, b;
        boolean breakA, breakB;

        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(hasPiece(x, y)){
                    if((blackChips & (1L << (x + 8 * y))) != 0){
                        targetChip = whiteChips;
                    } else if ((whiteChips & (1L << (x + 8 * y))) != 0){
                        targetChip = blackChips;
                    } else {
                        System.out.println("Who are you?");
                        break;
                    }
                    for(int i = x + 1; i < 8; i++){
                        if((targetChip & (1L << (i + 8 * y))) != 0){
                            if(!hasPiece(i + 1, y) && (i + 1) < 8){
                                validMoves |= (1L << (i + 1 + 8 * y));
                                break;
                            }
                        } else break;
                    }
                    for(int i = x - 1; i >= 0; i--){
                        if((targetChip & (1L << (i + 8 * y))) != 0){
                            if(!hasPiece(i - 1, y) && (i - 1) >= 0){
                                validMoves |= (1L << (i - 1 + 8 * y));
                                break;
                            }
                        } else break;
                    }
                    for(int j = y + 1; j < 8; j++){
                        if((targetChip & (1L << (x + 8 * j))) != 0){
                            if(!hasPiece(x, j + 1) && (j + 1) < 8){
                                validMoves |= (1L << (x + 8 * (j + 1)));
                                break;
                            }
                        } else break;
                    }
                    for(int j = y - 1; j >= 0; j--){
                        if((targetChip & (1L << (x + 8 * j))) != 0){
                            if(!hasPiece(x, j - 1) && (j - 1) >= 0){
                                validMoves |= (1L << (x + 8 * (j - 1)));
                                break;
                            }
                        } else break;
                    }
                    a = x - 1; b = x + 1;
                    breakA = true; breakB = true;
                    for (int j = y - 1; j >= 0; j--) {
                        if ((targetChip & (1L << (a + 8 * j))) != 0 && breakA) {
                            if (!hasPiece(a - 1, j - 1) && (j - 1) >= 0 && (a - 1) >= 0){
                                validMoves |= (1L << (a - 1 + 8 * (j - 1)));
                                breakA = false;
                            }
                            a--;
                        } else breakA = false;
                        if ((targetChip & (1L << (b + 8 * j))) != 0 && breakB) {
                            if (!hasPiece(b + 1, j - 1) && (j - 1) >= 0 && (b + 1) < 8) {
                                validMoves |= (1L << (b + 1 + 8 * (j - 1)));
                                breakB = false;
                            }
                            b++;
                        } else breakB = false;
                        if (!breakA && !breakB) break;
                    }
                    a = x - 1; b = x + 1;
                    breakA = true; breakB = true;
                    for (int j = y + 1; j < 8; j++) {
                        if ((targetChip & (1L << (a + 8 * j))) != 0 && breakA) {
                            if (!hasPiece(a - 1, j + 1) && (j + 1) < 8 && (a - 1) >= 0){
                                validMoves |= (1L << (a - 1 + 8 * (j + 1)));
                                breakA = false;
                            }
                            a--;
                        } else breakA = false;
                        if ((targetChip & (1L << (b + 8 * j))) != 0 && breakB) {
                            if (!hasPiece(b + 1, j + 1) && (j + 1) < 8 && (b + 1) < 8) {
                                validMoves |= (1L << (b + 1 + 8 * (j + 1)));
                                breakB = false;
                            }
                            b++;
                        } else breakB = false;
                        if (!breakA && !breakB) break;
                    }
                    if ((blackChips & (1L << (x + 8 * y))) != 0) blackValidMoves |= validMoves;
                    else whiteValidMoves |= validMoves;
                    validMoves = 0;
                }
            }
        }
        blackValidMoves &= ~allChips;
        whiteValidMoves &= ~allChips;
    }

    /**
     * Retrieves the current state of the board for the specified player.
     * @param player The player for whom to get the board state.
     * @return A StringBuilder representing the current state of the board.
     */
    public StringBuilder getBoardState(int player) {
        createValidMoves();
        StringBuilder state = new StringBuilder();
        state.append("\n\n");
        for (int y = 0; y < 8; y++) {
            state.append((8 - y) + " ");
            for (int x = 0; x < 8; x++) {
                if (hasPiece(x, y)) {
                    if ((blackChips & (1L << (x + 8 * y))) != 0) {
                        state.append("X ");
                    } else {
                        state.append("0 ");
                    }
                } else {
                    long validMoves = (player == 1) ? blackValidMoves : whiteValidMoves;
                    long mask = 1L << (x + 8 * y);
                    if ((validMoves & mask) != 0) {
                        state.append("* ");
                    } else {
                        state.append(". ");
                    }
                }
            }
            state.append("\n");
        }
        state.append(" ");
        for (char i = 'a'; i <='h'; i++)
            state.append(" " + i);

        state.append("\n\n");
        return state;
    }

    /**
     * Retrieves the board state as a DTO for the specified player.
     * @param player The player for whom to get the DTO.
     * @return A string representation of the board state.
     */
    public String getBoardStateDTO(int player){
        StringBuilder state = new StringBuilder("");
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (hasPiece(x, y)) {
                    if ((blackChips & (1L << (x + 8 * y))) != 0) {
                        state.append("X ");
                    } else {
                        state.append("0 ");
                    }
                } else {
                    long validMoves = (player == 1) ? blackValidMoves : whiteValidMoves;
                    long mask = 1L << (x + 8 * y);
                    if ((validMoves & mask) != 0) {
                        state.append("* ");
                    } else {
                        state.append(". ");
                    }
                }
            }
        }

        return "Board{" + state + '}';
    }

    /**
     * Retrieves valid moves for the black player.
     * @return A bitmask of valid moves for the black player.
     */
    public long getBlackValidMoves() {
        createValidMoves();
        return blackValidMoves;
    }

    /**
     * Retrieves valid moves for the white player.
     * @return A bitmask of valid moves for the white player.
     */
    public long getWhiteValidMoves() {
        createValidMoves();
        return whiteValidMoves;
    }

    /**
     * Retrieves all valid tiles for the specified player.
     * @param player The player for whom to get valid tiles.
     * @return A list of valid tiles.
     */
    public List<Tile> getAllValidTiles(int player) {
        List<Tile> validTiles = new ArrayList<>();
        long validMoves = getValidMoves(player);
        for (int i = 0; i < 64; i++) {
            long mask = 1L << i;
            if ((validMoves & mask) != 0) {
                int x = i % 8;
                int y = i / 8;
                validTiles.add(new Tile(x, y));
            }
        }
        return validTiles;
    }

    /**
     * Retrieves all tiles occupied by black chips.
     * @return A list of tiles occupied by black chips.
     */
    public List<Tile> getAllBlackChips(){
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            long mask = 1L << i;
            if ((blackChips & mask) != 0) {
                int x = i % 8;
                int y = i / 8;
                tiles.add(new Tile(x, y));
            }
        }
        return tiles;
    }

    /**
     * Retrieves all tiles occupied by white chips.
     * @return A list of tiles occupied by white chips.
     */
    public List<Tile> getAllWhiteChips(){
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            long mask = 1L << i;
            if ((whiteChips & mask) != 0) {
                int x = i % 8;
                int y = i / 8;
                tiles.add(new Tile(x, y));
            }
        }
        return tiles;
    }

    /**
     * Executes a move for the specified player to the given tile.
     * @param player The player making the move.
     * @param tile The tile to which the player is moving.
     * @return True if the move was successful, false otherwise.
     */
    public boolean makeMove(int player, Tile tile) {
        int x = tile.getX();
        int y = tile.getY();
        setPiece(x, y, player);

        return true;
    }

    /**
     * Retrieves the chips for the specified player.
     * @param player The player for whom to retrieve the chips.
     * @return A list of tiles occupied by the player's chips.
     */
    public List<Tile> getChips(int player) {
        List<Tile> playerChips = new ArrayList<>();
        long chips;
        if(player == 1) {
            chips = blackChips;
        } else chips = whiteChips;
        for (int i = 0; i < 64; i++) {
            long mask = 1L << i;
            if ((chips & mask) != 0) {
                int x = i % 8;
                int y = i / 8;
                playerChips.add(new Tile(x, y));
            }
        }
        return playerChips;
    }

    /**
     * Checks for a win condition and returns the result.
     * @return A GameFinished object indicating if the game is finished and the winner.
     */
    public GameFinished checkForWin() {
        GameFinished gameFinished;
        int winner = 0;

        long blackValidMoves = getValidMoves(1);
        long whiteValidMoves = getValidMoves(2);

        if (getChips(1).size() + getChips(2).size() == 64 || (blackValidMoves == 0 && whiteValidMoves == 0)) {
            if (getChips(1).size() > getChips(2).size()) {
                winner = 1;
                return gameFinished = new GameFinished(true, winner);
            } else if (getChips(2).size() > getChips(1).size()) {
                winner = 2;
                return gameFinished = new GameFinished(true, winner);
            } else {
                winner = 3;
                return gameFinished = new GameFinished(true, winner);
            }
        }
        return new GameFinished(false, -1);
    }

    /**
     * Retrieves the piece at the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return 1 for a black piece, 2 for a white piece, and 0 for an empty cell.
     */
    public int getPiece(int x, int y) {
        long mask = 1L << (x + 8 * y);

        if ((blackChips & mask) != 0) {
            return 1; // Черная фишка
        } else if ((whiteChips & mask) != 0) {
            return 2; // Белая фишка
        } else {
            return 0; // Пустая клетка
        }
    }

    /**
     * Calculates the current round based on the scores.
     * @return The current round number.
     */
    public  int getRound(){
        return ((score()[0] + score()[1]) - 4);
    }

    /**
     * Constructs a new BoardService instance as a copy of the provided BoardService.
     * @param oldBoardService The BoardService instance to copy.
     */
    public BoardService(BoardService oldBoardService) {
        this.board = new Board(oldBoardService.board);
        this.blackChips = oldBoardService.blackChips;
        this.whiteChips = oldBoardService.whiteChips;
        this.blackValidMoves = oldBoardService.blackValidMoves;
        this.whiteValidMoves = oldBoardService.whiteValidMoves;
    }

    /**
     * Creates a copy of this BoardService instance.
     * @return A new BoardService instance that is a copy of this one.
     */
    public BoardService getCopy() {
        return new BoardService(this);
    }

    public void setBlackValidMoves(long blackValidMoves) {
        this.blackValidMoves = blackValidMoves;
    }

    public void setWhiteValidMoves(long whiteValidMoves) {
        this.whiteValidMoves = whiteValidMoves;
    }

    /**
     * Retrieves the bitmask of black chips.
     * @return The bitmask representing the black chips.
     */
    public long getBlackChips() {
        return blackChips;
    }

    /**
     * Retrieves the bitmask of white chips.
     * @return The bitmask representing the white chips.
     */
    public long getWhiteChips() {
        return whiteChips;
    }

    /**
     * Checks if there is a black piece at the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if there is a black piece, false otherwise.
     */
    public boolean isBlackPiece(int x, int y) {
        long mask = 1L << (x + 8 * y);
        return ((blackChips & mask) != 0);
    }

    /**
     * Checks if there is a white piece at the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if there is a white piece, false otherwise.
     */
    public boolean isWhitePiece(int x, int y) {
        long mask = 1L << (x + 8 * y);
        return ((whiteChips & mask) != 0);
    }
}