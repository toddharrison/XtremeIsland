package com.goodformentertainment.canary.xis.dao;

import net.canarymod.database.Column;
import net.canarymod.database.Column.ColumnType;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XHighScore extends DataAccess {
    public static final String PLAYER_UUID = "player_uuid";
    public static final String PLAYER_NAME = "player_name";
    public static final String HIGH_SCORE = "high_score";

    public static List<XHighScore> getAllXHighScores() throws DatabaseReadException {
        final List<XHighScore> highScores = new ArrayList<XHighScore>();

        final List<DataAccess> daos = new ArrayList<DataAccess>();
        final XHighScore xHighScore = new XHighScore();
        Database.get().loadAll(xHighScore, daos, new HashMap<String, Object>());
        for (final DataAccess dao : daos) {
            highScores.add((XHighScore) dao);
        }

        return highScores;
    }

    public XHighScore() {
        super("xis_xhighscore");
    }

    @Override
    public XHighScore getInstance() {
        return new XHighScore();
    }

    @Column(columnName = PLAYER_UUID, dataType = DataType.STRING, columnType = ColumnType.UNIQUE)
    public String playerUuid;

    @Column(columnName = PLAYER_NAME, dataType = DataType.STRING)
    public String playerName;

    @Column(columnName = HIGH_SCORE, dataType = DataType.INTEGER)
    public int highScore;

    public void update() throws DatabaseWriteException {
        final Map<String, Object> filters = new HashMap<String, Object>();
        filters.put(PLAYER_UUID, playerUuid);
        Database.get().update(this, filters);
    }
}
