package com.tech.smal.turkaf.data.models;

import java.io.Serializable;

/**
 * Created by smoct on 22/03/2019.
 */

public class Rating_{
    private String userId;
    private String questionId;
    private int rating; //1-5

    public Rating_() {
    }

    public Rating_(String userId, String questionId, int rating) {
        this.userId = userId;
        this.questionId = questionId;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "userId = " + userId + " / questionId = " + questionId + " / rating = " + rating;
    }
}
