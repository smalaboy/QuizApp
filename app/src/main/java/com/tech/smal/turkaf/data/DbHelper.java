package com.tech.smal.turkaf.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoct on 23/02/2019.
 */

public class DbHelper extends SQLiteOpenHelper {

    public final static String TAG = DbHelper.class.getSimpleName();

    private Context mContext;

    public DbHelper(Context context) {
        super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbContract.USER.CREATE_TABLE);
        sqLiteDatabase.execSQL(DbContract.QUESTIONS.CREATE_TABLE);
        sqLiteDatabase.execSQL(DbContract.QUESTION_GRADES.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DbContract.USER.DROP_TABLE);
        sqLiteDatabase.execSQL(DbContract.QUESTIONS.DROP_TABLE);
        sqLiteDatabase.execSQL(DbContract.QUESTION_GRADES.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public List<Question> getAllQuestions () {
        ArrayList<Question> questionsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String columns[] = {DbContract.QUESTIONS.COL_ID, DbContract.QUESTIONS.COL_QUESTION, DbContract.QUESTIONS.COL_OPT_A,
                DbContract.QUESTIONS.COL_OPT_B, DbContract.QUESTIONS.COL_OPT_C,
                DbContract.QUESTIONS.COL_OPT_D, DbContract.QUESTIONS.COL_ANSWER, DbContract.QUESTIONS.COL_USER_ID};
        Cursor cursor = db.query(DbContract.QUESTIONS.TABLE_NAME, columns, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            Question qst = new Question();
            qst.setQuestion(cursor.getString(1));
            qst.setOptA(cursor.getString(2));
            qst.setOptB(cursor.getString(3));
            qst.setOptC(cursor.getString(4));
            qst.setOptD(cursor.getString(5));
            qst.setAnswer(cursor.getString(6));
            qst.setUserId(cursor.getInt(7));

            questionsList.add(qst);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();

        return questionsList;
    }


    public boolean addQuestion(Question qst) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DbContract.QUESTIONS.COL_QUESTION, qst.getQuestion());
        values.put(DbContract.QUESTIONS.COL_OPT_A, qst.getOptA());
        values.put(DbContract.QUESTIONS.COL_OPT_B, qst.getOptB());
        values.put(DbContract.QUESTIONS.COL_OPT_C, qst.getOptC());
        values.put(DbContract.QUESTIONS.COL_OPT_D, qst.getOptD());
        values.put(DbContract.QUESTIONS.COL_ANSWER, qst.getAnswer());
        values.put(DbContract.QUESTIONS.COL_USER_ID, qst.getUserId());

        db.insert(DbContract.QUESTIONS.TABLE_NAME, null, values);
        db.close();

        return true;
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DbContract.USER.COL_USERNAME, user.getUsername());
        values.put(DbContract.USER.COL_EMAIL, user.getEmail());

        db.insert(DbContract.USER.TABLE_NAME, null, values);
        db.close();

        return true;
    }

    public boolean addGrade(Grade grade) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DbContract.QUESTION_GRADES.COL_USER_ID, grade.getUserId());
        values.put(DbContract.QUESTION_GRADES.COL_QUESTION_ID, grade.getQuestionId());
        values.put(DbContract.QUESTION_GRADES.COL_GRADE, grade.getGrade());

        db.insert(DbContract.QUESTION_GRADES.TABLE_NAME, null, values);
        db.close();

        return true;
    }


    public void addUsers() {
        User u1 = new User("DEFAULT_USER", "smal.tech");
        User u2 = new User("TEST_USER", "test@test.com");

        addUser(u1);
        addUser(u2);
    }



    public void addQuestions () {
        ArrayList<Question> list = new ArrayList<>();
        list.add(new Question("Qui est le Président du Burkina Faso?", "Blaise Compaoré",
                "Roch Marc Christian Kaboré", "Michel Kafando", "Simon Compaoré",
                "Roch Marc Christian Kaboré"));
        list.add(new Question("Qui est le Premier Ministre du Burkina?", "Blaise Compaoré",
                "Roch Marc Christian Kaboré", "Marie Dabiré", "Simon Compaoré",
                "Marie Dabiré"));
        list.add(new Question("De quelle couleur est l'étoile au centre du drapeau du Burkina Faso?",
                "Rouge", "Jaune", "Vert", "Blanc", "Jaune"));
        list.add(new Question("Combien de broches composent l'étoile sur le drapeau du Burkina?",
                "3", "4", "5", "6", "5"));

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        for (Question qst: list) {
            values.put(DbContract.QUESTIONS.COL_QUESTION, qst.getQuestion());
            values.put(DbContract.QUESTIONS.COL_OPT_A, qst.getOptA());
            values.put(DbContract.QUESTIONS.COL_OPT_B, qst.getOptB());
            values.put(DbContract.QUESTIONS.COL_OPT_C, qst.getOptC());
            values.put(DbContract.QUESTIONS.COL_OPT_D, qst.getOptD());
            values.put(DbContract.QUESTIONS.COL_ANSWER, qst.getAnswer());

            db.insert(DbContract.QUESTIONS.TABLE_NAME, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }


    public List<QuestionDetails> getUserQuestions (int userId) {
        ArrayList<QuestionDetails> qDetailsList = new ArrayList<>();

        ArrayList<Question> questionsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String columns[] = {DbContract.QUESTIONS.COL_ID, DbContract.QUESTIONS.COL_QUESTION, DbContract.QUESTIONS.COL_OPT_A,
                DbContract.QUESTIONS.COL_OPT_B, DbContract.QUESTIONS.COL_OPT_C,
                DbContract.QUESTIONS.COL_OPT_D, DbContract.QUESTIONS.COL_ANSWER, DbContract.QUESTIONS.COL_USER_ID};

        //String selection = DbContract.QUESTIONS.COL_USER_ID + " = ?";
        //String[] selectionArgs = {userId+""};

        Cursor cursor = db.query(DbContract.QUESTIONS.TABLE_NAME, columns, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            Question qst = new Question();
            qst.setId(cursor.getInt(0));
            qst.setQuestion(cursor.getString(1));
            qst.setOptA(cursor.getString(2));
            qst.setOptB(cursor.getString(3));
            qst.setOptC(cursor.getString(4));
            qst.setOptD(cursor.getString(5));
            qst.setAnswer(cursor.getString(6));
            qst.setUserId(1 /*cursor.getInt(7)*/);

            questionsList.add(qst);
        }
        Log.d(TAG, "qList " + questionsList.size());


        ////////////////////////////////////////////
        User user = new User();
        String[] userColumns = {DbContract.USER.COL_USERNAME, DbContract.USER.COL_EMAIL};
        String userSelection = DbContract.USER.COL_ID + " = ?";
        String userSelectionArgs[] = {userId+""};
        Cursor userCursor = db.query(DbContract.USER.TABLE_NAME, userColumns, userSelection,
                userSelectionArgs, null, null, null);
        while (userCursor.moveToNext()) {
            user.setId(userId);
            user.setUsername(userCursor.getString(0));
            user.setEmail(userCursor.getString(1));
        }
        Log.d(TAG, "user " + user.toString());


        ///////////////////////////////////////////
        String[] gradeCols = {DbContract.QUESTION_GRADES.COL_USER_ID, DbContract.QUESTION_GRADES.COL_GRADE};
        String gradeSelection = DbContract.QUESTION_GRADES.COL_QUESTION_ID + " = ?";
        for (Question q: questionsList) {
            ArrayList<Grade> gradesList = new ArrayList<>();
            int qId = q.getId();
            String[] gradeSelectionArgs = {qId+""};
            Cursor gradeCursor = db.query(DbContract.QUESTION_GRADES.TABLE_NAME, gradeCols,
                    gradeSelection, gradeSelectionArgs, null, null, null);

            QuestionDetails dDetails = new QuestionDetails();
            //settting the current user
            dDetails.setUser(user);
            //setting the question
            dDetails.setQuestion(q);


            while (gradeCursor.moveToNext()) {
                Grade grade = new Grade();
                grade.setQuestionId(qId);
                grade.setUserId(gradeCursor.getInt(0));
                grade.setGrade(gradeCursor.getInt(1));

                gradesList.add(grade);
            }
            //setting the gradesList of the question
            dDetails.setGradesList(gradesList);
            qDetailsList.add(dDetails);
        }

        Log.d(TAG, "detailsList " + qDetailsList.size());
        return qDetailsList;
    }

    public boolean updateQuestion(Question qst) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DbContract.QUESTIONS.COL_QUESTION, qst.getQuestion());
        values.put(DbContract.QUESTIONS.COL_OPT_A, qst.getOptA());
        values.put(DbContract.QUESTIONS.COL_OPT_B, qst.getOptB());
        values.put(DbContract.QUESTIONS.COL_OPT_C, qst.getOptC());
        values.put(DbContract.QUESTIONS.COL_OPT_D, qst.getOptD());
        values.put(DbContract.QUESTIONS.COL_ANSWER, qst.getOptA());
        //values.put(DbContract.QUESTIONS.COL_USER_ID, qst.getUserId());

        String where = DbContract.QUESTIONS.COL_ID + " = ?";
        String[] whereArgs = {qst.getId()+""};

        int index = db.update(DbContract.QUESTIONS.TABLE_NAME, values, where, whereArgs);
        Log.d(TAG, "index = " + index + " / id = " + qst.getId());
        db.close();
        if (index > 0)
            return true;
        return false;
    }

}
