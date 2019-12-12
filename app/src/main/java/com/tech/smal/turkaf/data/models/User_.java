package com.tech.smal.turkaf.data.models;

/**
 * Created by smoct on 27/03/2019.
 */

public class User_ {
    private String id;
    private String displayName;
    private String email;
    private int energy;
    private int diamonds;
    private int reputation;
    private int highestScore;

    public User_() {
    }

    public User_(String id, String displayName, String email, int energy, int diamonds, int reputation) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.energy = energy;
        this.diamonds = diamonds;
        this.reputation = reputation;
    }


    public User_(String id, String displayName, String email, int energy, int diamonds, int reputation, int highestScore) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.energy = energy;
        this.diamonds = diamonds;
        this.reputation = reputation;
        this.highestScore = highestScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    @Override
    public String toString() {
        return "id = " + id + " / displayName = " + displayName + " / email = " + email
                + " / energy = " + energy + " / diamonds = " + diamonds + " / reputation = " + reputation;
    }
}
