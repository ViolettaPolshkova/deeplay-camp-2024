package io.deeplay.camp.bot.OldBots;

import io.deeplay.camp.bot.MyBots.BotStrategy;
import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KaylebeeBot extends BotStrategy {
    protected KaylebeeBot(int id, String name) {
        super(id, name);
    }

    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        return null;
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}