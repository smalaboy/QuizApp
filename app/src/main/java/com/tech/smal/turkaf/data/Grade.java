package com.tech.smal.turkaf.data;

import java.io.Serializable;

/**
 * Created by smoct on 22/03/2019.
 */

public class Grade implements Serializable{
    private int userId;
    private int questionId;
    private int grade;

    public Grade() {
    }

    public Grade(int userId, int questionId, int grade) {
        this.userId = userId;
        this.questionId = questionId;
        this.grade = grade;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
