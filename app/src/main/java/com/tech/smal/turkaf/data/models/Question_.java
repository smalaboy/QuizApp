package com.tech.smal.turkaf.data.models;

import java.io.Serializable;

/**
 * Created by smoct on 27/03/2019.
 */

public class Question_ implements Serializable{
    private String id;
    private String userId; //id of the user who suggested the question
    private String ratingsId; //id of the grades list
    private String votesId; //id of the votes list
    private String question;
    private String category = "Undefined";
    private String ansA; //correct answer
    private String ansB;
    private String ansC;
    private String ansD;
    private String status = "pending";

    public Question_() {
    }

    public Question_(String id, String userId, String ratingsId, String votesId, String question,
                     String category, String ansA, String ansB, String ansC, String ansD, String status) {
        this.id = id;
        this.userId = userId;
        this.ratingsId = ratingsId;
        this.votesId = votesId;
        this.question = question;
        this.category = category;
        this.ansA = ansA;
        this.ansB = ansB;
        this.ansC = ansC;
        this.ansD = ansD;
        this.status = status;
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

    public String getRatingsId() {
        return ratingsId;
    }

    public void setRatingsId(String ratingsId) {
        this.ratingsId = ratingsId;
    }

    public String getVotesId() {
        return votesId;
    }

    public void setVotesId(String votesId) {
        this.votesId = votesId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAnsA() {
        return ansA;
    }

    public void setAnsA(String ansA) {
        this.ansA = ansA;
    }

    public String getAnsB() {
        return ansB;
    }

    public void setAnsB(String ansB) {
        this.ansB = ansB;
    }

    public String getAnsC() {
        return ansC;
    }

    public void setAnsC(String ansC) {
        this.ansC = ansC;
    }

    public String getAnsD() {
        return ansD;
    }

    public void setAnsD(String ansD) {
        this.ansD = ansD;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "id = " + id + " / question = " + question + " / status = " + status;
    }
}
