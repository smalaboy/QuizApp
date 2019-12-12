package com.tech.smal.turkaf.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by smoct on 23/02/2019.
 */

public class QuestionDetails implements Serializable{
    private String id;

    private Question question;

    private User user;

    private ArrayList<Grade> gradesList;


    public QuestionDetails(Question question, User user, ArrayList<Grade> gradesList) {
        this.question = question;
        this.user = user;
        this.gradesList = gradesList;
    }

    public QuestionDetails(String id, Question question, User user, ArrayList<Grade> gradesList) {
        this.id = id;
        this.question = question;
        this.user = user;
        this.gradesList = gradesList;
    }

    public QuestionDetails() {
        this.question = new Question();
        this.user = new User();
        this.gradesList = new ArrayList<>();
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Grade> getGradesList() {
        return gradesList;
    }

    public void setGradesList(ArrayList<Grade> gradesList) {
        this.gradesList = gradesList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
