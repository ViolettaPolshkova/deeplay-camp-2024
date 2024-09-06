package io.deeplay.camp.bot.MyBots.UtilityFunction;

import io.deeplay.camp.board.BoardService;

import java.util.Random;

/**
 * Класс, реализующий случайную функцию полезности для оценки состояния игры.
 */
public class RandomUtilityFunction implements UtilityFunction {

    private final Random random;

    /**
     * Конструктор класса RandomUtilityFunction.
     * Инициализирует генератор случайных чисел.
     */
    public RandomUtilityFunction() {
        this.random = new Random();
    }

    /**
     * Оценивает состояние доски для текущего игрока.
     *
     * @param board текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return оценка состояния игры: 1, если текущий игрок выиграл; -1, если проиграл; 0 в случае ничьей или случайное значение от -1 до 1.
     */
    @Override
    public double evaluate(final BoardService board, int currentPlayerId) {
        int opponentPlayer = currentPlayerId == 1 ? 2 : 1;

        if (board.checkForWin().isGameFinished()) {
            if (board.checkForWin().getUserIdWinner() == currentPlayerId) {
                return 1;
            } else if (board.checkForWin().getUserIdWinner() == opponentPlayer) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return random.nextDouble() * 2 - 1;
        }
    }
}