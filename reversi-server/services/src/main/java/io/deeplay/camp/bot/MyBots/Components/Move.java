package io.deeplay.camp.bot.MyBots.Components;

import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.entity.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий ход в игре.
 */
public class Move {
    private Tile move;
    private int player;
    private BoardService boardService;
    private Move parent;
    private List<Move> children;
    private int wins;
    private int visits;

    /**
     * Конструктор для создания нового хода.
     *
     * @param move ход
     * @param player игрок, который сделал ход
     * @param boardService текущее состояние доски
     * @param parent родительский узел
     */
    public Move(Tile move, int player, BoardService boardService, Move parent) {
        this.move = move;
        this.player = player;
        this.boardService = boardService;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.wins = 0;
        this.visits = 0;
    }

    /**
     * Получает ход.
     *
     * @return ход
     */
    public Tile getMove() {
        return move;
    }

    /**
     * Получает игрока, который сделал ход.
     *
     * @return идентификатор игрока
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Получает текущее состояние доски.
     *
     * @return состояние доски
     */
    public BoardService getBoardService() {
        return boardService;
    }

    /**
     * Получает родительский узел.
     *
     * @return родительский узел
     */
    public Move getParent() {
        return parent;
    }

    /**
     * Получает дочерние узлы.
     *
     * @return список дочерних узлов
     */
    public List<Move> getChildren() {
        return children;
    }

    /**
     * Добавляет дочерний узел.
     *
     * @param child дочерний узел
     */
    public void addChild(Move child) {
        this.children.add(child);
    }

    /**
     * Получает количество побед.
     *
     * @return количество побед
     */
    public int getWins() {
        return wins;
    }

    /**
     * Устанавливает количество побед.
     *
     * @param wins количество побед
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Увеличивает количество побед на 1.
     */
    public void incrementWins() {
        this.wins++;
    }

    /**
     * Получает количество посещений.
     *
     * @return количество посещений
     */
    public int getVisits() {
        return visits;
    }

    /**
     * Устанавливает количество посещений.
     *
     * @param visits количество посещений
     */
    public void setVisits(int visits) {
        this.visits = visits;
    }

    /**
     * Увеличивает количество посещений на 1.
     */
    public void incrementVisits() {
        this.visits++;
    }
}
