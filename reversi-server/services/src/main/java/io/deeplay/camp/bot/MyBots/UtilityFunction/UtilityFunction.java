package io.deeplay.camp.bot.MyBots.UtilityFunction;

import io.deeplay.camp.board.BoardService;

public interface UtilityFunction {
    /**
     * Оценка состояния.
     * Свойства:
     * 1 - победа первого игрока.
     * 0 - ничья.
     * -1 - победа второго игрока.
     **/
    double evaluate(final BoardService board, int currentPlayerId);
}