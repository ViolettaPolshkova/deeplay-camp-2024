package io.deeplay.camp.bot.MyBots;

import io.deeplay.camp.bot.MyBots.UtilityFunction.ClassicUtilityFunction;
import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * MiniMaxBot реализует стратегию минимакс для игры.
 */
public class MiniMaxBot extends BotStrategy {

    private final int depth;
    private final ClassicUtilityFunction utilityFunction;
    private List<Tile> moves;
    private List<Integer> rounds;
    /**
     * Конструктор MiniMaxBot.
     *
     * @param id    идентификатор бота
     * @param name  имя бота
     * @param depth максимальная глубина поиска
     */
    public MiniMaxBot(int id, String name, int depth) {
        super(id, name);
        this.depth = depth;
        this.utilityFunction = new ClassicUtilityFunction();
        this.moves = new ArrayList<>();
        this.rounds = new ArrayList<>();
    }
    /**
     * Получает лучший ход для текущего игрока.
     *
     * @param currentPlayerId идентификатор текущего игрока
     * @param boardLogic      логика доски игры
     * @return лучший ход или null, если допустимых ходов нет
     */
    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);
        if (allTiles.isEmpty()) {
            return null;
        }

        Tile bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Tile tile : allTiles) {
            BoardService clonedBoard = boardLogic.getCopy();
            clonedBoard.makeMove(currentPlayerId, tile);
            double score = minimax(clonedBoard, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (score > bestScore) {
                bestScore = score;
                bestMove = tile;
            }
        }
        rounds.add(boardLogic.getRound());
        moves.add(bestMove);
        BoardService clonedBoard = boardLogic.getCopy();
        clonedBoard.makeMove(currentPlayerId, bestMove);
        return bestMove;
    }
    /**
     * Рекурсивный метод минимакс с альфа-бета отсечением.
     *
     * @param board           текущее состояние доски
     * @param depth           оставшаяся глубина поиска
     * @param isMaximizing    флаг, указывающий, максимизирует ли текущий игрок свои очки
     * @param currentPlayerId идентификатор текущего игрока
     * @param alpha           минимально возможное значение для максимизирующего игрока
     * @param beta            максимально возможное значение для минимизирующего игрока
     * @return оценка текущего состояния игры
     */
    private double minimax(BoardService board, int depth, boolean isMaximizing, int currentPlayerId, double alpha, double beta) {
        int opponentPlayerId = (currentPlayerId == 1) ? 2 : 1;
        if (depth == 0 || board.checkForWin().isGameFinished()) {
            double score = utilityFunction.evaluate(board, currentPlayerId);
            return score;
        }

        List<Tile> validMoves = board.getAllValidTiles(isMaximizing ? currentPlayerId : opponentPlayerId);
        if (validMoves.isEmpty()) {
            return minimax(board, depth - 1, !isMaximizing, currentPlayerId, alpha, beta);
        }

        double bestScore = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (Tile move : validMoves) {
            BoardService clonedBoard = board.getCopy();
            clonedBoard.makeMove(isMaximizing ? currentPlayerId : opponentPlayerId, move);
            moves.add(move);
            double score = minimax(clonedBoard, depth - 1, !isMaximizing, currentPlayerId, alpha, beta);

            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }
            moves.remove(moves.size() - 1);
            if (beta <= alpha) {
                break;
            }
        }
        return bestScore;
    }
    /**
     * Получает все допустимые ходы для текущего игрока.
     *
     * @param currentPlayerId идентификатор текущего игрока
     * @param boardLogic      логика доски игры
     * @return список допустимых ходов для текущего игрока
     */
    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}