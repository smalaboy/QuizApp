package com.tech.smal.turkaf.data.models;

import java.io.Serializable;

/**
 * Created by smoct on 31/03/2019.
 */

public class Score implements Serializable {
    private String id;
    private String userId;
    private String gameType;
    private int score;
    private int reputationGained;
    private int energyGained;
    private int diamondsGained;

    public Score(String id, String userId, String gameType, int score) {
        this.id = id;
        this.userId = userId;
        this.gameType = gameType;
        this.score = score;

        computeBonus();
    }

    public Score(int score) {
        this.score = score;
    }

    public Score() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getReputationGained() {
        return reputationGained;
    }

    public void setReputationGained(int reputationGained) {

        this.reputationGained = reputationGained;
    }

    public int getEnergyGained() {
        return energyGained;
    }

    public void setEnergyGained(int energyGained) {
        this.energyGained = energyGained;
    }

    public int getDiamondsGained() {
        return diamondsGained;
    }

    public void setDiamondsGained(int diamondsGained) {
        this.diamondsGained = diamondsGained;
    }


    public void computeBonus () {
        int rep = 0;
        int en = 0;
        int diamonds = 0;
        if (getScore() < 25) {
            rep = 2;
        }
        if (getScore() >= 25 && getScore() <= 35) {
            rep = 5;
            en = 3;
        }
        if (getScore() > 35 && getScore() <= 40){
            rep = 7;
            en = 3;
        }
        if (getScore() > 40) {
            rep = 10;
            en = 3;
        }
        setDiamondsGained(diamonds);
        setEnergyGained(en);
        setReputationGained(rep);
    }
}
