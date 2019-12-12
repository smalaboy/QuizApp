package com.tech.smal.turkaf.data.models;

/**
 * Created by smoct on 27/03/2019.
 */

public class Vote_ {
    private String userId;
    private String questionId;
    private int vote = 0; //0 for none, 1 for positive and -1 for negative vote

    public Vote_() {
    }

    public Vote_(String userId, String questionId, int vote) {
        this.userId = userId;
        this.questionId = questionId;
        this.vote = vote;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "userId = " + userId + " / questionId = " + questionId + " / vote = " + vote;
    }
}
