package com.tech.smal.turkaf.data;

import java.util.ArrayList;

/**
 * Created by smoct on 24/03/2019.
 */

public class GradesReport {
    private int qID; //question id
    private int nGrades;
    private double average;
    private int posGradesCount;
    private int negGradesCount;
    private ArrayList<Grade> gradeList;

    public GradesReport(int qID, ArrayList<Grade> gradeList) {
        this.qID = qID;
        this.gradeList = gradeList;
    }

    public void makeReport() {
        if (gradeList != null) {
            this.nGrades = gradeList.size();
            this.posGradesCount = 0;
            this.negGradesCount = 0;
            this.average = this.computeAverage();
        }
    }

    private double computeAverage() {
        double sum = 0;
        for (Grade grade : gradeList) {
            sum += grade.getGrade();
            if (grade.getGrade() >= 3)
                this.posGradesCount++;
            else
                this.negGradesCount++;
        }
        return sum/gradeList.size();
    }

    public int getqID() {
        return qID;
    }

    public void setqID(int qID) {
        this.qID = qID;
    }

    public int getnGrades() {
        return nGrades;
    }

    public double getAverage() {
        return average;
    }

    public int getPosGradesCount() {
        return posGradesCount;
    }

    public int getNegGradesCount() {
        return negGradesCount;
    }

}
