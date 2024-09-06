package io.deeplay.camp.bot.MyBots;

import io.deeplay.camp.bot.MyBots.UtilityFunction.ClassicUtilityFunction;
import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

/**
 * ExpectiMaxBot реализует стратегию ExpectiMax для принятия решений в игре.
 */
public class ExpectiMaxBot extends BotStrategy {

    private final int depth;
    private final ClassicUtilityFunction utilityFunction;
    private Random random;

    /**
     * Конструктор ExpectiMaxBot.
     *
     * @param id    идентификатор бота
     * @param name  имя бота
     * @param depth максимальная глубина поиска
     */
    public ExpectiMaxBot(int id, String name, int depth) {
        super(id, name);
        this.depth = depth;
        this.utilityFunction = new ClassicUtilityFunction();
        this.random = new Random();
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
            double score = expectiMaxValue(clonedBoard, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).second;
            if (score > bestScore) {
                bestScore = score;
                bestMove = tile;
            }
        }

        return bestMove;
    }

    /**
     * Рекурсивный метод для оценки состояния игры с использованием ExpectiMax.
     *
     * @param board           текущее состояние доски
     * @param depth           оставшаяся глубина поиска
     * @param isMaximizing    флаг, указывающий, максимизирует ли текущий игрок свои очки
     * @param currentPlayerId идентификатор текущего игрока
     * @param alpha           минимально возможное значение для максимизирующего игрока
     * @param beta            максимально возможное значение для минимизирующего игрока
     * @return пара, содержащая лучший ход и его оценку
     */
    private Pair<Tile, Double> expectiMaxValue(BoardService board, int depth, boolean isMaximizing, int currentPlayerId, double alpha, double beta) {
        int opponentPlayerId = (currentPlayerId == 1) ? 2 : 1;

        if (depth == 0 || board.checkForWin().isGameFinished()) {
            return new Pair<>(null, utilityFunction.evaluate(board, currentPlayerId));
        }

        List<Tile> validMoves = board.getAllValidTiles(isMaximizing ? currentPlayerId : opponentPlayerId);
        if (validMoves.isEmpty()) {
            return expectiMaxValue(board, depth - 1, !isMaximizing, currentPlayerId, alpha, beta);
        }

        if (isMaximizing) {
            double bestScore = Double.NEGATIVE_INFINITY;
            Tile bestMove = null;

            for (Tile move : validMoves) {
                BoardService clonedBoard = board.getCopy();
                clonedBoard.makeMove(currentPlayerId, move);
                Pair<Tile, Double> result = expectiMaxValue(clonedBoard, depth - 1, false, currentPlayerId, alpha, beta);
                double score = result.second;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return new Pair<>(bestMove, bestScore);
        } else {
            double expectedValue = 0;
            for (Tile move : validMoves) {
                BoardService clonedBoard = board.getCopy();
                clonedBoard.makeMove(opponentPlayerId, move);
                Pair<Tile, Double> result = expectiMaxValue(clonedBoard, depth - 1, true, currentPlayerId, alpha, beta);
                expectedValue += result.second;
            }
            expectedValue /= validMoves.size();
            return new Pair<>(null, expectedValue);
        }
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

    /**
     * Вспомогательный класс для пар.
     */
    private static class Pair<K, V> {
        public final K first;
        public final V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }
}