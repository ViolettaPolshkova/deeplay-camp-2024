package io.deeplay.camp.bot.MyBots.UtilityFunction;

import io.deeplay.camp.board.BoardService;

/**
 * Класс, реализующий функцию полезности для оценки состояния игры.
 */
public class ClassicUtilityFunction implements UtilityFunction {

    private static final double[][] WEIGHTS = {
            {100, -10, 5, 5, 5, 5, -10, 100},
            {-10, -20, 1, 1, 1, 1, -20, -10},
            {5, 1, 3, 3, 3, 3, 1, 5},
            {5, 1, 3, 3, 3, 3, 1, 5},
            {5, 1, 3, 3, 3, 3, 1, 5},
            {5, 1, 3, 3, 3, 3, 1, 5},
            {-10, -20, 1, 1, 1, 1, -20, -10},
            {100, -10, 5, 5, 5, 5, -10, 100}
    };

    /**
     * Оценивает состояние доски для текущего игрока.
     *
     * @param board           текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return оценка состояния игры
     */
    @Override
    public double evaluate(BoardService board, int currentPlayerId) {
        int opponentId = (currentPlayerId == 1) ? 2 : 1;

        if (board.checkForWin().isGameFinished()) {
            if (board.checkForWin().getUserIdWinner() == currentPlayerId) {
                return 1000;
            } else if (board.checkForWin().getUserIdWinner() == opponentId) {
                return -1;
            } else if (board.checkForWin().getUserIdWinner() == 3) {
                return 0.0;
            }
        }
        double score = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board.isBlackPiece(x, y)) {
                    score += WEIGHTS[x][y];
                } else if (board.isWhitePiece(x, y)) {
                    score -= WEIGHTS[x][y];
                }
            }
        }

        score += evaluateCorners(board, currentPlayerId) * 50;
        score += evaluateEdges(board, currentPlayerId) * 8;
        score += evaluateStableDiscs(board, currentPlayerId) * 7;
        score += (board.score()[currentPlayerId - 1] - board.score()[opponentId - 1]) * 8;
        score += evaluateThreats(board, currentPlayerId) * 1.5;
        score += evaluateAvailableMoves(board, currentPlayerId) * 2;
        return score;
    }

    /**
     * Оценивает стабильные фишки на доске.
     *
     * @param board           текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return оценка стабильных фишек
     */
    private double evaluateStableDiscs(BoardService board, int currentPlayerId) {
        double stableScore = 0;

        stableScore = countStableDiscs(board, currentPlayerId);
        stableScore -= countStableDiscs(board, currentPlayerId == 1 ? 2 : 1);

        return stableScore;
    }

    /**
     * Подсчитывает количество стабильных фишек для игрока.
     *
     * @param board           текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return количество стабильных фишек
     */
    public int countStableDiscs(BoardService board, int currentPlayerId) {
        int stableDiscs = 0;

        stableDiscs += checkCorner(board, 0, 0, currentPlayerId);
        stableDiscs += checkCorner(board, 0, 7, currentPlayerId);
        stableDiscs += checkCorner(board, 7, 0, currentPlayerId);
        stableDiscs += checkCorner(board, 7, 7, currentPlayerId);

        stableDiscs += checkAdjacentToCorner(board, 0, 0, currentPlayerId);
        stableDiscs += checkAdjacentToCorner(board, 0, 7, currentPlayerId);
        stableDiscs += checkAdjacentToCorner(board, 7, 0, currentPlayerId);
        stableDiscs += checkAdjacentToCorner(board, 7, 7, currentPlayerId);

        return stableDiscs;
    }

    /**
     * Проверяет, занята ли угловая клетка текущим игроком.
     *
     * @param board    текущее состояние доски
     * @param x        координата по оси X
     * @param y        координата по оси Y
     * @param playerId идентификатор игрока
     * @return 1, если угловая фишка стабильна, иначе 0
     */
    private int checkCorner(BoardService board, int x, int y, int playerId) {
        if (playerId == 1) {
            if (board.isBlackPiece(x, y)) {
                return 1;
            }
        } else if (playerId == 2) {
            if (board.isWhitePiece(x, y)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Проверяет клетки рядом с углом на стабильность.
     *
     * @param board    текущее состояние доски
     * @param x        координата по оси X
     * @param y        координата по оси Y
     * @param playerId идентификатор игрока
     * @return количество стабильных соседних фишек
     */
    private int checkAdjacentToCorner(BoardService board, int x, int y, int playerId) {
        int stableCount = 0;

        int[][] adjacentCells = {
                {x, y + 1}, // Вправо
                {x + 1, y}, // Вниз
                {x + 1, y + 1}, // Вправо и вниз
                {x, y - 1}, // Влево
                {x - 1, y}, // Вверх
                {x - 1, y - 1} // Влево и вверх
        };

        for (int[] cell : adjacentCells) {
            int adjX = cell[0];
            int adjY = cell[1];
            if (playerId == 1) {
                if (isInBounds(adjX, adjY) && board.isBlackPiece(adjX, adjY)) {
                    stableCount++; // Соседняя фишка стабильна
                }
            } else if (playerId == 2) {
                if (isInBounds(adjX, adjY) && board.isWhitePiece(adjX, adjY)) {
                    stableCount++;
                }
            }
        }

        return stableCount;
    }

    /**
     * Проверяет, находится ли клетка в пределах доски.
     *
     * @param x координата по оси X
     * @param y координата по оси Y
     * @return true, если клетка в пределах доски, иначе false
     */
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    /**
     * Оценивает контроль краев для текущего игрока.
     *
     * @param board           текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return оценка контроля краев
     */
    private double evaluateEdges(BoardService board, int currentPlayerId) {
        double edgeScore = 0;

        for (int i = 0; i < 8; i++) {
            if (currentPlayerId == 1) {
                if (board.isBlackPiece(0, i)) edgeScore++; // Верхний край
                if (board.isBlackPiece(7, i)) edgeScore++; // Нижний край
                if (board.isBlackPiece(i, 0)) edgeScore++; // Левый край
                if (board.isBlackPiece(i, 7)) edgeScore++; // Правый край
            } else {
                if (board.isWhitePiece(0, i)) edgeScore++; // Верхний край
                if (board.isWhitePiece(7, i)) edgeScore++; // Нижний край
                if (board.isWhitePiece(i, 0)) edgeScore++; // Левый край
                if (board.isWhitePiece(i, 7)) edgeScore++; // Правый край
            }
        }
        return edgeScore;
    }

    /**
     * Оценивает контроль углов для текущего игрока.
     *
     * @param board           текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return оценка контроля углов
     */
    private double evaluateCorners(BoardService board, int currentPlayerId) {
        double cornerScore = 0;

        if (currentPlayerId == 1) {
            if (board.isBlackPiece(0, 0)) cornerScore++; // Верхний левый угол
            if (board.isBlackPiece(7, 0)) cornerScore++; // Нижний левый угол
            if (board.isBlackPiece(0, 7)) cornerScore++; // Верхний правый угол
            if (board.isBlackPiece(7, 7)) cornerScore++; // Нижний правый угол
        } else {
            if (board.isWhitePiece(0, 0)) cornerScore++; // Верхний левый угол
            if (board.isWhitePiece(7, 0)) cornerScore++; // Нижний левый угол
            if (board.isWhitePiece(0, 7)) cornerScore++; // Верхний правый угол
            if (board.isWhitePiece(7, 7)) cornerScore++; // Нижний правый угол
        }
        return cornerScore;
    }

    /**
     * Оценивает угрозы для текущего игрока.
     *
     * @param board           текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return оценка угроз
     */
    private double evaluateThreats(BoardService board, int currentPlayerId) {
        double threatScore = 0;
        int opponentId = (currentPlayerId == 1) ? 2 : 1;

        threatScore -= checkAdjacentToCorner(board, 0, 0, opponentId);
        threatScore -= checkAdjacentToCorner(board, 0, 7, opponentId);
        threatScore -= checkAdjacentToCorner(board, 7, 0, opponentId);
        threatScore -= checkAdjacentToCorner(board, 7, 7, opponentId);

        return threatScore;
    }

    /**
     * Оценивает доступные ходы для текущего игрока.
     *
     * @param board           текущее состояние доски
     * @param currentPlayerId идентификатор текущего игрока
     * @return количество доступных ходов
     */
    private double evaluateAvailableMoves(BoardService board, int currentPlayerId) {
        return board.getAllValidTiles(currentPlayerId).size();
    }
}
