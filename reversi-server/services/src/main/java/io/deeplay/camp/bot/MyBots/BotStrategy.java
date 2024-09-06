package io.deeplay.camp.bot.MyBots;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Абстрактный класс для стратегии бота.
 */
public abstract class BotStrategy {
    public final int id;
    public final String name;

    /**
     * Конструктор для создания стратегии бота.
     *
     * @param id   идентификатор бота
     * @param name имя бота
     */
    protected BotStrategy(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Получает ход для текущего игрока.
     *
     * @param currentPlayerId идентификатор текущего игрока
     * @param boardLogic      логика доски игры
     * @return выбранный ход для текущего игрока
     */
    public abstract Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic);

    /**
     * Получает все допустимые ходы для текущего игрока.
     *
     * @param currentPlayerId идентификатор текущего игрока
     * @param boardLogic      логика доски игры
     * @return список всех допустимых ходов для текущего игрока
     */
    public abstract List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic);
}