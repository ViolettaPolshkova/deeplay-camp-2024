package io.deeplay.camp.bot.MyBots;

import io.deeplay.camp.bot.MyBots.Components.Move;
import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * MonteCarloBot реализует стратегию Монте-Карло для принятия решений в игре.
 */
public class MonteCarloBot extends BotStrategy {

    private final int simulations;
    private final Random random;

    /**
     * Конструктор для создания бота Монте-Карло.
     *
     * @param id идентификатор бота
     * @param name имя бота
     * @param simulations количество симуляций, выполняемых для каждого хода
     */
    public MonteCarloBot(int id, String name, int simulations) {
        super(id, name);
        this.simulations = simulations;
        this.random = new Random();
    }

    /**
     * Получает оптимальный ход для текущего игрока, основываясь на симуляциях.
     *
     * @param currentPlayerId идентификатор текущего игрока
     * @param boardLogic логика доски игры
     * @return оптимальный ход для текущего игрока
     */
    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);
        if (allTiles.isEmpty()) {
            return null;
        }

        Move root = new Move(null, currentPlayerId, boardLogic.getCopy(), null);

        for (Tile tile : allTiles) {
            BoardService clonedBoard = boardLogic.getCopy();
            clonedBoard.makeMove(currentPlayerId, tile);
            Move child = new Move(tile, currentPlayerId, clonedBoard, root);
            root.addChild(child);
            runSimulations(child, currentPlayerId);
        }

        Move bestMove = selectBestMove(root);
        return bestMove.getMove();
    }

    private void runSimulations(Move move, int currentPlayerId) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < simulations; i++) {
            executor.submit(() -> {
                BoardService simulatedBoard = move.getBoardService().getCopy();
                int player = currentPlayerId;

                while (!simulatedBoard.checkForWin().isGameFinished()) {
                    List<Tile> validMoves = simulatedBoard.getAllValidTiles(player);
                    if (validMoves.isEmpty()) break;

                    Tile randomMove = validMoves.get(random.nextInt(validMoves.size()));
                    simulatedBoard.makeMove(player, randomMove);
                    player = (player == 1) ? 2 : 1;
                }

                int winnerId = simulatedBoard.checkForWin().getUserIdWinner();

                synchronized (move) {
                    updateResults(move, winnerId, currentPlayerId);
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void updateResults(Move move, int winnerId, int currentPlayerId) {
        if (winnerId == currentPlayerId) {
            move.incrementWins();
        }
        move.incrementVisits();
    }

    private Move selectBestMove(Move root) {
        Move bestMove = null;
        double bestWinRate = Double.NEGATIVE_INFINITY;

        for (Move child : root.getChildren()) {
            double winRate = (double) child.getWins() / child.getVisits();
            if (child.getVisits() > 0 && winRate > bestWinRate) {
                bestWinRate = winRate;
                bestMove = child;
            }
        }
        return bestMove;
    }

    /**
     * Получает все допустимые ходы для текущего игрока.
     *
     * @param currentPlayerId идентификатор текущего игрока
     * @param boardLogic логика доски игры
     * @return список всех допустимых ходов для текущего игрока
     */
    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}