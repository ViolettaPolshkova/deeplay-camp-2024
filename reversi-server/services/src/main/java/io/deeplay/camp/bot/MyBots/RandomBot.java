package io.deeplay.camp.bot.MyBots;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.List;

/**
 * RandomBot реализует стратегию случайного выбора ходов.
 */
public class RandomBot extends BotStrategy {

    /**
     * Конструктор для создания случайного бота.
     *
     * @param id идентификатор бота
     * @param name имя бота
     */
    public RandomBot(int id, String name) {
        super(id, name);
    }

    /**
     * Получает случайный ход для текущего игрока.
     *
     * @param currentPlayerId идентификатор текущего игрока
     * @param boardLogic логика доски игры
     * @return случайный ход для текущего игрока или null, если допустимых ходов нет
     */
    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);

        if (allTiles.isEmpty()) {
            return null;
        }

        SecureRandom secureRandom = new SecureRandom();
        return allTiles.get(secureRandom.nextInt(allTiles.size()));
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